package org.sbuisson.pitest.extension.intercept.scm;


import org.apache.maven.scm.*;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.manager.BasicScmManager;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.git.gitexe.GitExeScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.classinfo.ClassByteArraySource;
import org.apache.maven.plugins.annotations.Component;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalList;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.File;
import java.util.*;

import static org.eclipse.jgit.lib.Constants.encodeASCII;


class ScmFileInterceptor  extends ScmCommon implements MutationInterceptor {
    public ScmFileInterceptor(InterceptorParameters interceptorParameters) {
        super(interceptorParameters);
    }


    public InterceptorType type() {
        return InterceptorType.FILTER;
    }

    public void begin(ClassTree classTree) {
        try {
            acceptableClasses = findModifiedClassNames();
        } catch (ScmException e) {
            e.printStackTrace();
        }
    }


    public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater mutater) {
        return FCollection.filter(mutations, new F<MutationDetails, Boolean>() {
            public Boolean apply(MutationDetails mutationDetails) {
                return acceptableClasses.contains(toFileName(mutationDetails)) ;
            }
        });
    }

    private String toFileName(MutationDetails mutationDetails) {
        //TODO : .iterator().next() à revoir
        File f = new File(mutationDetails.getClassName().asInternalName().replace(".", File.separator));
        return   this.data.getSourceDirs().iterator().next()+"/"+f.getParent() + File.separator + mutationDetails.getFilename();
    }

    public void end() {

    }

}
