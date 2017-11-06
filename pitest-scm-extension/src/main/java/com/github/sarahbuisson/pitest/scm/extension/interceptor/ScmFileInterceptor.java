package com.github.sarahbuisson.pitest.scm.extension.interceptor;


import com.github.sarahbuisson.pitest.scm.extension.common.ScmCommon;
import org.apache.maven.scm.ScmException;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.FCollection;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;
import org.pitest.util.Log;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;


class ScmFileInterceptor extends ScmCommon implements MutationInterceptor {
    private static final Logger LOG = Log
            .getLogger();


    public ScmFileInterceptor(ReportOptions data, ClassByteArraySource source, SmartSourceLocator locator) {
        super(data, source, locator);
    }

    @Override
    public InterceptorType type() {
        return InterceptorType.FILTER;
    }

    @Override
    public void begin(ClassTree classTree) {
        try {
            acceptableClasses = findModifiedFilesNames();
        } catch (ScmException e) {
            LOG.throwing("", "", e);
        }
    }

    @Override
    public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater mutater) {
        return FCollection.filter(mutations, mutationDetails -> acceptableClasses.contains(toFileName(mutationDetails)));
    }

    private String toFileName(MutationDetails mutationDetails) {
        //TODO : .iterator().next() à revoir
        File f = new File(mutationDetails.getClassName().asInternalName().replace(".", File.separator));
        return this.data.getSourceDirs().iterator().next() + "/" + f.getParent() + File.separator + mutationDetails.getFilename();
    }

    @Override
    public void end() {
        // nothing to do
    }

}
