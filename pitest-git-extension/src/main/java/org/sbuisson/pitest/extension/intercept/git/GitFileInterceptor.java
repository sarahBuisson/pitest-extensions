package org.sbuisson.pitest.extension.intercept.git;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.eclipse.jgit.lib.Ref;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.ClassTree;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.eclipse.jgit.lib.Constants.encodeASCII;


class GitFileInterceptor implements MutationInterceptor {

  private final SmartSourceLocator locator;
  ReportOptions data;
  ClassByteArraySource source;
  String gitRepositoryPath;
  Git git;
  String destinationReference;
  String originReference;
  String srcPath;
  Repository repository;
  List<DiffEntry> diff = null;
  Map<String, List<Integer>> excludedLinesByFile = new HashMap<String, List<Integer>>();
  private List<String> updatedFiles = new ArrayList<String>();
  private Map<String,DiffEntry> entryByUpdatedFiles = new HashMap<String,DiffEntry> ();

  GitFileInterceptor(ReportOptions data, ClassByteArraySource source) {
    this.data = data;
    this.source = source;
    this.locator = new SmartSourceLocator(data.getSourceDirs());

    gitRepositoryPath = data.getFreeFormProperties().getProperty("gitRepositoryPath");
    srcPath = data.getFreeFormProperties().getProperty("srcPath");
    if (srcPath == null) {
      srcPath = "src/main/java/";
    }
    if(!srcPath.endsWith("/")){
      srcPath += "/";
    }
    try {
      System.out.println(gitRepositoryPath);
      repository = openRepository(gitRepositoryPath);
      System.out.println("rr");
      System.out.println("rr");
      destinationReference = data.getFreeFormProperties().getProperty("destinationReference");
      if (destinationReference == null) {
        destinationReference = "refs/heads/master";
      }
      originReference = data.getFreeFormProperties().getProperty("originReference");
      if (originReference == null) {
        originReference = "refs/remotes/origin/HEAD";

      }
      System.out.println("diff between "+originReference+" and "+destinationReference);
      extractUpdatedFiles();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

  }

  @Override
  public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
    System.out.println("mutations.size()");
    System.out.println(mutations.size());
    return FCollection.filter(mutations, Prelude.not(isInScope()));
  }


  private F<MutationDetails, Boolean> isInScope() {


    return new F<MutationDetails, Boolean>() {
      @Override
      public Boolean apply(MutationDetails mutationDetails) {
        return allowedFile(mutationDetails);

      }
    };

  }

  private boolean allowedFile(MutationDetails mutationDetails) {
    System.out.println(updatedFiles.contains(mutationDetails.getFilename()));
    return updatedFiles.contains(mutationDetails.getFilename());
  }

  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {

    String fileName = srcPath + clazz.name().getPackage().asInternalName() +"/"+ clazz.rawNode().sourceFile;
    DiffEntry diffEntry = this.entryByUpdatedFiles.get(fileName);
    if(diffEntry!=null){
    DiffFormatter diffF = new DiffFormatter(System.out);
    try {
      diffF.format(Arrays.asList(diffEntry));
    } catch (IOException e) {
      e.printStackTrace();
    }}

  }

  private void extractUpdatedFiles() {
    try {

      AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, this.originReference);

      AbstractTreeIterator newTreeParser = prepareTreeParser(repository, this.destinationReference);

// then the procelain diff-command returns a list of diff entries


      diff = new Git(repository).diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).call();
      for (DiffEntry entry : diff) {
        System.out.println("-Entry: " + entry);
        updatedFiles.add(entry.getNewPath());
        entryByUpdatedFiles.put(entry.getNewPath(),entry);
      }


    } catch (IOException e) {
      e.printStackTrace();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }
  }


  private static AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
    // from the commit we can build the tree which allows us to construct the TreeParser
    Ref head = repository.exactRef(ref);

    RevWalk walk = new RevWalk(repository);
    RevCommit commit = walk.parseCommit(head.getObjectId());
    RevTree tree = walk.parseTree(commit.getTree().getId());

    CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();

    ObjectReader oldReader = repository.newObjectReader();
    oldTreeParser.reset(oldReader, tree.getId());


    walk.dispose();

    return oldTreeParser;

  }

  public Repository openRepository(String repoPath) throws IOException, GitAPIException {
    // first create a test-repository, the return is including the .get directory here!


    // now open the resulting repository with a FileRepositoryBuilder
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    File repoDir = new File(repoPath);
    Repository repository = Git.init().setDirectory( new File(repoPath)).call().getRepository();
    System.out.println("Having repository: " + repository.getDirectory());

    // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
    Ref head = repository.exactRef("refs/heads/master");
    System.out.println("Ref of refs/heads/master: " + head);
    return repository;
  }


  @Override
  public void end() {

    repository.close();
  }

}
