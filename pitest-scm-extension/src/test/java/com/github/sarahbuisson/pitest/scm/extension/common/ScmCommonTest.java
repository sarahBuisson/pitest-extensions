package com.github.sarahbuisson.pitest.scm.extension.common;

import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.MockitoJUnitRunner;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScmCommonTest {


    @InjectMocks
    ScmCommon scmCommon;

    @Mock
    SmartSourceLocator locator;
    @Mock
    ReportOptions data;
    @Mock
    ClassByteArraySource source;
    @Mock
    ScmManager scmManager;

    @Spy
    File rootDirectory = new File(".");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //scmCommon.setScmManager(scmManager);
        when(data.getSourceDirs()).thenReturn(Arrays.asList(rootDirectory));

    }

    @Test
    public void findModifiedClassNames() throws Exception {

        ScmFileSet scmRoot = mock(ScmFileSet.class);
        ChangeFile changeFile = new ChangeFile("C1.java", "");
        ChangeFile changeFile2 = new ChangeFile("C2.java", "");
        changeFile.setAction(ScmFileStatus.MODIFIED);
        changeFile2.setAction(ScmFileStatus.DELETED);
        ChangeLogScmResult scmResult = new ChangeLogScmResult("", new ChangeLogSet(Arrays.asList(new ChangeSet(null, null, null, Arrays.asList(changeFile))), new Date(), new Date()));
        when(scmManager.changeLog(any())).thenReturn(scmResult);


        //When
        List<String> modifiedClass = scmCommon.findModifiedFilesNames();

        assertThat(modifiedClass, Matchers.containsInAnyOrder(rootDirectory.getAbsoluteFile() + "/C1.java"));

    }

    @Test
    public void changesBetweenBranchs() throws Exception {


        //Given
        ScmRepository repo = mock(ScmRepository.class);

        ScmFileSet scmRoot = mock(ScmFileSet.class);
        ChangeFile changeFile = new ChangeFile("C1.java", "");
        ChangeFile changeFile2 = new ChangeFile("C2.java", "");
        changeFile.setAction(ScmFileStatus.MODIFIED);
        changeFile2.setAction(ScmFileStatus.DELETED);
        ChangeLogScmResult scmResult = new ChangeLogScmResult("", new ChangeLogSet(Arrays.asList(new ChangeSet(null, null, null, Arrays.asList(changeFile))), new Date(), new Date()));
        when(scmManager.changeLog(any())).thenReturn(scmResult);


        //When
        List<String> modifiedFiles = scmCommon.changesBetweenBranchs("branch1", "branchDestination",
                Sets.newSet(ScmFileStatus.MODIFIED), repo, scmRoot);

        //Then
        assertThat(modifiedFiles, Matchers.containsInAnyOrder("C1.java"));
    }

}