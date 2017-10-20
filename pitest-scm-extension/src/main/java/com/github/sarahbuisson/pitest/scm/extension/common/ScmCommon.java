package com.github.sarahbuisson.pitest.scm.extension.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.maven.scm.*;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalList;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.SmartSourceLocator;
import org.pitest.util.Log;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

@Getter
@Setter
public class ScmCommon {
    private static final Logger LOG = Log
            .getLogger();


    protected InterceptorParameters interceptorParameters;
    protected ReportOptions data;


    protected ClassByteArraySource source;

    protected SmartSourceLocator locator;
    protected String destinationReference;
    protected String originReference;


    protected File scmRootDirectory =  new File(".");
    protected ScmManager scmManager;
    protected String scmConnection;
    protected Set<ScmFileStatus> statusToInclude = new HashSet<ScmFileStatus>(Arrays.asList(ScmFileStatus.ADDED, ScmFileStatus.MODIFIED));

    protected List<String> acceptableClasses;

    public ScmCommon(ReportOptions data, ClassByteArraySource source, SmartSourceLocator locator) {
        this.data = data;
        this.source = source;
        this.locator = locator;
    }

    public void init() {
        scmRootDirectory = new File(data.getFreeFormProperties().getProperty("scmRootDirectory"));
        initScm();

        destinationReference = data.getFreeFormProperties().getProperty("destinationReference");
        if (destinationReference == null) {
            destinationReference = "origin/master";
        }
        originReference = data.getFreeFormProperties().getProperty("originReference");
        if (originReference == null) {


        }



    }

    protected List<String> findModifiedFilesNames() throws ScmException {
        final ScmRepository repository = scmManager.makeScmRepository(scmConnection);

        return FCollection.flatMap(data.getSourceDirs(), sourceRoot -> {


            try {
                List<String> modifiedPaths = changesBetweenBranchs(originReference, destinationReference, statusToInclude, repository, new ScmFileSet(scmRootDirectory));
                FunctionalList<String> modifiedFullPaths = FCollection.flatMap(modifiedPaths, s -> Arrays.asList(scmRootDirectory.getAbsolutePath() + "/" + s));
                return modifiedFullPaths;
            } catch (ScmException e) {
                LOG.throwing("", "", e);
            }

            return Collections.emptyList();

        });

    }

    /**
     * visible for test
     * @param origine
     * @param destination
     * @param statusToInclude
     * @param repository
     * @param scmRoot
     * @return
     * @throws ScmException
     */

    List<String> changesBetweenBranchs(String origine, String destination, Set<ScmFileStatus> statusToInclude, ScmRepository repository, ScmFileSet scmRoot) throws ScmException {
        List<String> modifiedPaths = new ArrayList();
        ChangeLogScmRequest scmRequest = new ChangeLogScmRequest(repository, scmRoot);
        String branchName = destination;
        if (origine != null)
            branchName += ".." + origine;

        scmRequest.setScmBranch(new ScmBranch(branchName));

        ChangeLogScmResult changeLogScmResult = scmManager.changeLog(scmRequest);
        if (changeLogScmResult.isSuccess()) {
            List<ChangeSet> changeSets = changeLogScmResult.getChangeLog().getChangeSets();
            if (!changeSets.isEmpty()) {
                for (ChangeSet change : changeSets) {
                    fillModifiedPath(statusToInclude, modifiedPaths, change);
                }
            } else {
                LOG.warning("no ChangeSet");
            }
        } else {
            LOG.warning(changeLogScmResult.getProviderMessage());
            LOG.warning(changeLogScmResult.getCommandOutput());
            LOG.warning("fail of changelog");
        }

        LOG.warning("updated File:" + modifiedPaths);
        return modifiedPaths;
    }

    private void fillModifiedPath(Set<ScmFileStatus> statusToInclude, List<String> modifiedPaths, ChangeSet change) {
        List<ChangeFile> files = change.getFiles();

        if (files == null || files.isEmpty()) {
            LOG.warning("no files updated");

        } else {
            LOG.info("found files:" + files.size());
        }

        for (final ChangeFile changeFile : files) {
            if (statusToInclude.contains(changeFile.getAction())) {
                modifiedPaths.add(changeFile.getName());
                LOG.info("file modified:" + changeFile.getName());
            }else{
                LOG.info("file ignored:" + changeFile.getName());
            }
        }
    }

    private void fillModifiedPath(Set<ScmFileStatus> statusToInclude, List<String> modifiedPaths,String fileName) {

    }

    private void initScm(){
        scmManager = new BasicScmManager();

        //Add all SCM providers we want to use
        scmManager.setScmProvider("git", new GitExeScmProvider());

        scmConnection = data.getFreeFormProperties().getProperty("scmConnection");
        if (StringUtils.isEmpty(scmConnection)) {

            throw new RuntimeException("SCM Connection is not set.");
        }

    }
}
