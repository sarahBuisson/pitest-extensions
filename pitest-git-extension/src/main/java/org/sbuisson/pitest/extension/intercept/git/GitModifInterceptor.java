package org.sbuisson.pitest.extension.intercept.git;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.File;
import java.io.IOException;
import java.util.*;


class GitModifInterceptor implements MutationInterceptor {

  private final SmartSourceLocator locator;
  ReportOptions data;
  ClassByteArraySource source;
  String path;
  Git git;
  String destinationReference;
  String originReference;
  Repository repository;
  List<DiffEntry> diff = null;
  private List<String> updatedFiles = new ArrayList<String>();

  GitModifInterceptor(ReportOptions data, ClassByteArraySource source) {
    System.out.println("GitModifInterceptor");
    this.data = data;
    this.source = source;
    this.locator = new SmartSourceLocator(data.getSourceDirs());

    path = data.getFreeFormProperties().getProperty("gitRepositoryPath");
    try {
      System.out.println(path);
      repository = openRepository(path);
      System.out.println(repository.getAllRefs().size());
      System.out.println(repository.getBranch());
      destinationReference = data.getFreeFormProperties().getProperty("destinationReference");
      if (destinationReference == null) {
        destinationReference = "refs/heads/master";
      }
      originReference = data.getFreeFormProperties().getProperty("originReference");
      if (originReference == null) {
        originReference = "refs/heads/" + repository.getBranch();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (GitAPIException e) {
      e.printStackTrace();
    }

  }

  
  public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isInScope()));
  }


  private F<MutationDetails, Boolean> isInScope() {

    return new F<MutationDetails, Boolean>() {
      
      public Boolean apply(MutationDetails mutationDetails) {
        return extratExcludedLine(mutationDetails);

      }
    };

  }

  private boolean extratExcludedLine(MutationDetails mutationDetails) {
    return updatedFiles.contains(mutationDetails.getFilename());
  }

  
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  public void begin(ClassTree clazz) {


    try {

      AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, this.originReference);

      AbstractTreeIterator newTreeParser = prepareTreeParser(repository, this.destinationReference);

// then the procelain diff-command returns a list of diff entries


      diff = new Git(repository).diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).call();
      for (DiffEntry entry : diff) {
        System.out.println("Entry: " + entry);
        updatedFiles.add(entry.getNewPath());
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
    Repository repository = Git.init().setDirectory(new File(repoPath)).call().getRepository();
    System.out.println("Having repository: " + repository.getDirectory());

    // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
    Ref head = repository.exactRef("refs/heads/master");
    System.out.println("Ref of refs/heads/master: " + head);
    return repository;
  }


  
  public void end() {

    repository.close();
  }


}
