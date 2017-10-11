package org.sbuisson.pitest.extension.intercept.scm;

import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.scm.*;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalList;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.File;
import java.util.*;

/**
 * Created by sbuisson on 09/10/2017.
 */
public class ScmCommon {



    protected InterceptorParameters interceptorParameters;
    protected final ReportOptions data;


    protected ClassByteArraySource source;

    protected final SmartSourceLocator locator;
    protected String destinationReference;
    protected String originReference;


    protected File scmRoot;
    protected ScmManager scmManager;
    protected Set<ScmFileStatus> statusToInclude = new HashSet<ScmFileStatus>(Arrays.asList(ScmFileStatus.ADDED, ScmFileStatus.MODIFIED));

    protected List<String> acceptableClasses;

    public ScmCommon(InterceptorParameters interceptorParameters) {
        this.interceptorParameters = interceptorParameters;
        this.data = interceptorParameters.data();
        this.source = interceptorParameters.source();
        this.locator = new SmartSourceLocator(data.getSourceDirs());
        scmRoot=new File(data.getFreeFormProperties().getProperty("scmRoot"));


        destinationReference = data.getFreeFormProperties().getProperty("destinationReference");
        if (destinationReference == null) {
            destinationReference = "origin/master";
        }
        originReference = data.getFreeFormProperties().getProperty("originReference");
        if (originReference == null) {
            originReference = "refs/remotes/origin/HEAD";


        }

        initScm();
    }

    protected List<String> findModifiedClassNames() throws ScmException {
        final ScmRepository repository = scmManager.makeScmRepository(getSCMConnection());

        return FCollection.flatMap(data.getSourceDirs(), new F<File, List<String>>() {

            public List<String> apply(final File sourceRoot) {


                List<String> modifiedPaths = new ArrayList<String>();
                try {

                    modifiedPaths = changesBetweenBranchs(originReference, destinationReference, statusToInclude, repository, scmRoot);
                    FunctionalList<String> a = FCollection.flatMap(modifiedPaths, new F<String, Iterable<String>>() {
                        public Iterable<String> apply(String s) {

                            return Arrays.asList(scmRoot.getAbsolutePath()+"/"+s);
                        }
                    });
                    return a;
                } catch (ScmException e) {
                    e.printStackTrace();
                }

                return null;

            }
        });

    }

    private List<String> changesBetweenBranchs(String origine, String destination, Set<ScmFileStatus> statusToInclude, ScmRepository repository, File scmRoot) throws ScmException {
        List<String> modifiedPaths = new ArrayList<String>();
        ChangeLogScmRequest scmRequest = new ChangeLogScmRequest(repository, new ScmFileSet(scmRoot));
        scmRequest.setScmBranch(new ScmBranch(destination + ".." + origine));

        ChangeLogScmResult changeLogScmResult = this.scmManager.changeLog(scmRequest);
        if (changeLogScmResult.isSuccess()) {
            List<ChangeSet> changeSets = changeLogScmResult.getChangeLog().getChangeSets();
            if (!changeSets.isEmpty()) {
                for (ChangeSet change : changeSets) {
                    List<ChangeFile> files = change.getFiles();

                    if (files == null || files.isEmpty()) {
                        System.out.println("no files updated");

                    }

                    for (final ChangeFile changeFile : files) {
                        if (statusToInclude.contains(changeFile.getAction())) {
                            modifiedPaths.add(changeFile.getName());
                        }
                    }
                }
            }else{
                System.out.println("no ChangeSet");
            }
        }else{
            System.out.println("fail of changelog");
        }

        System.out.println("updated File:"+modifiedPaths);
        return modifiedPaths;
    }

    private void initScm() {
        scmManager = new BasicScmManager();

        //Add all SCM providers we want to use
        scmManager.setScmProvider("git", new GitExeScmProvider());


    }

    private String getSCMConnection() throws ScmException {

        final String scmConnection = data.getFreeFormProperties().getProperty("scmConnection");
        if (StringUtils.isNotEmpty(scmConnection)) {
            return scmConnection;
        }


        throw new ScmException("SCM Connection is not set.");

    }
}
