package com.github.sarahbuisson.pitest.scm.extension.interceptor;

import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.scm.manager.ScmManager;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.build.MutationInterceptorFactory;
import org.pitest.mutationtest.tooling.SmartSourceLocator;
import org.pitest.plugin.Feature;

public class ScmFileInterceptorFactory implements
        MutationInterceptorFactory {

    @Override
    public String description() {
        return "disable the mutation coverage for the line generatedOld by any annotations";
    }

    @Override
    public MutationInterceptor createInterceptor(InterceptorParameters params) {
        ScmFileInterceptor interceptor = new ScmFileInterceptor(params.data(), params.source(), new SmartSourceLocator(params.data().getSourceDirs()));
        interceptor.init();
        return interceptor;
    }

    @Override
    public Feature provides() {
        return Feature.named("SCM_FileFilter")
                .withOnByDefault(true)
                .withDescription("Filters mutations of the srcBranche that was already in the destinationBranche");

    }
}
