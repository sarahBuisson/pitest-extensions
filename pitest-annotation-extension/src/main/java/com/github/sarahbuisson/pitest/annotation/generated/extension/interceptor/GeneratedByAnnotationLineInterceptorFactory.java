package com.github.sarahbuisson.pitest.annotation.generated.extension.interceptor;

import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.build.MutationInterceptorFactory;
import org.pitest.mutationtest.tooling.SmartSourceLocator;
import org.pitest.plugin.Feature;

public class GeneratedByAnnotationLineInterceptorFactory implements
        MutationInterceptorFactory {
    @Override
    public String description() {
        return "disable the mutation coverage for the line generatedOld by any annotations";
    }


    @Override
    public MutationInterceptor createInterceptor(InterceptorParameters params) {
        return new GeneratedByAnnotationLineInterceptor(new SmartSourceLocator(params.data().getSourceDirs()), params.data(), params.source());
    }

    @Override
    public Feature provides() {
        return Feature.named("GeneratedCodeFilter")
                .withOnByDefault(true)
                .withDescription(description());

    }
}
