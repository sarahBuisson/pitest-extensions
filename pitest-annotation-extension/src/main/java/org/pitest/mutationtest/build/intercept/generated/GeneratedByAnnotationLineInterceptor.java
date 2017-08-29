package org.pitest.mutationtest.build.intercept.generated;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.Option;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.ClassTree;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.Reader;
import java.util.*;


class GeneratedByAnnotationLineInterceptor implements MutationInterceptor {

  private final SmartSourceLocator locator;
  ReportOptions data;
  ClassByteArraySource source;

  Map<String, List<Integer>> excludedLinesByFile = new HashMap<String, List<Integer>>();

  GeneratedByAnnotationLineInterceptor(ReportOptions data, ClassByteArraySource source) {
    this.data = data;
    this.source = source;
    this.locator = new SmartSourceLocator(data.getSourceDirs());

  }

  @Override
  public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
    return FCollection.filter(mutations, Prelude.not(isGeneratedLine()));
  }


  private F<MutationDetails, Boolean> isGeneratedLine() {

    return new F<MutationDetails, Boolean>() {
      @Override
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
        try {
          cu = JavaParser.parse(reader.value(), false);
          new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(MarkerAnnotationExpr n, Object arg) {
              super.visit(n, arg);
              for (int i = n.getBeginLine(); i <= n.getEndLine(); i++) {
                excludedLines.add(new Integer(i));
              }
            }
          }.visit(cu, null);
        } catch (ParseException e) {
          e.printStackTrace();

        }
      }
    }
    return excludedLinesByFile.get(fileId).contains(new Integer(mutationDetails.getLineNumber()));
  }

  @Override
  public InterceptorType type() {
    return InterceptorType.FILTER;
  }

  @Override
  public void begin(ClassTree clazz) {

  }


  @Override
  public void end() {

  }
}
