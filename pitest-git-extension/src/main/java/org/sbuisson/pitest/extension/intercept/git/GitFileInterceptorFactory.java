package org.sbuisson.pitest.extension.intercept.git;

import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.build.MutationInterceptorFactory;
import org.pitest.mutationtest.config.ReportOptions;

public class GitFileInterceptorFactory implements
        MutationInterceptorFactory {

  public String description() {
    return "disable the mutation coverage for the line generatedOld by any annotations";
  }


  public MutationInterceptor createInterceptor(ReportOptions data, ClassByteArraySource source) {
    if(Boolean.parseBoolean(data.getFreeFormProperties().getProperty("fullFile"))) {
      return new GitFileInterceptor(data, source);
    }else{
      return new GitModifInterceptor(data, source);
    }
  }
}
