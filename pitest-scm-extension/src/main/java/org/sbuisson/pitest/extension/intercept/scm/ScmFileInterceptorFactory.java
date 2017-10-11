package org.sbuisson.pitest.extension.intercept.scm;

import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.build.MutationInterceptorFactory;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.plugin.Feature;

public class ScmFileInterceptorFactory implements
        MutationInterceptorFactory {

  public String description() {
    return "disable the mutation coverage for the line generatedOld by any annotations";
  }

  public MutationInterceptor createInterceptor(InterceptorParameters interceptorParameters) {
    return new ScmFileInterceptor(interceptorParameters);
  }

  public Feature provides() {
    return Feature.named("SCM_FileFilter")
            .withOnByDefault(true)
            .withDescription("Filters mutations of the srcBranche that was already in the destinationBranche");

  }
}
