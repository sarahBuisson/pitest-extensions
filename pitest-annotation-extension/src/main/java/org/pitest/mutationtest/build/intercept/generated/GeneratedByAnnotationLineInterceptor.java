package org.pitest.mutationtest.build.intercept.generated;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.io.input.ReaderInputStream;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.Option;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;


class GeneratedByAnnotationLineInterceptor implements MutationInterceptor {

  private final SmartSourceLocator locator;
  ReportOptions data;
  ClassByteArraySource source;

  Map<String, List<Integer>> excludedLinesByFile = new HashMap<String, List<Integer>>();

  GeneratedByAnnotationLineInterceptor(InterceptorParameters params) {
    this.data = params.data();
    this.source = params.source();
    this.locator = new SmartSourceLocator(data.getSourceDirs());

  }

  
  public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isGeneratedLine()));
  }


  private F<MutationDetails, Boolean> isGeneratedLine() {

    return new F<MutationDetails, Boolean>() {
      
      public Boolean apply(MutationDetails mutationDetails) {
        return extratExcludedLine(mutationDetails);

      }
    };

  }

  private boolean extratExcludedLine(MutationDetails mutationDetails) {
    Option<Reader> reader = locator.locate(Arrays.asList(mutationDetails.getClassName().toString()), mutationDetails.getFilename());

    String fileId = mutationDetails.getClassName().toString() + mutationDetails.getFilename();
    if (!excludedLinesByFile.containsKey(fileId)) {
      final ArrayList<Integer> excludedLines = new ArrayList<Integer>();
      excludedLinesByFile.put(fileId, excludedLines);
      if (reader.hasSome()) {
        CompilationUnit cu = null;

          cu = JavaParser.parse(new ReaderInputStream(reader.value(), Charset.defaultCharset()));
          new VoidVisitorAdapter<Object>() {
            
            public void visit(MarkerAnnotationExpr n, Object arg) {
              super.visit(n, arg);
              if(n.getRange().isPresent()) {
                Range range = n.getRange().get();
                for (int i = range.begin.line; i <= range.end.line; i++) {
                  excludedLines.add(new Integer(i));
                }
              }
            }
          }.visit(cu, null);

      }
    }
    return excludedLinesByFile.get(fileId).contains(new Integer(mutationDetails.getLineNumber()));
  }

  
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  public void begin(ClassTree clazz) {

  }


  public void end() {

  }
}
