package org.pitest.mutationtest.build.intercept.generated;

import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.build.MutationInterceptorFactory;
import org.pitest.mutationtest.config.ReportOptions;

public class GeneratedByAnnotationLineInterceptorFactory implements
        MutationInterceptorFactory {
  @Override
  public String description() {
    return "disable the mutation coverage for the line generatedOld by any annotations";
  }

  @Override
  public MutationInterceptor createInterceptor(ReportOptions data, ClassByteArraySource source) {
    return new GeneratedByAnnotationLineInterceptor( data, source);
  }
}
