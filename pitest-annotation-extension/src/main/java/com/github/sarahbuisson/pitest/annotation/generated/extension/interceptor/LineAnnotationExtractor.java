package com.github.sarahbuisson.pitest.annotation.generated.extension.interceptor;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by sarahbuisson on 01/11/2017.
 */
public class LineAnnotationExtractor extends
        VoidVisitorAdapter<Object> {
    List<Integer> excludedLines = new ArrayList();

    @Override
    public void visit(MarkerAnnotationExpr n, Object arg) {
        super.visit(n, arg);
        Optional<Range> nRange = n.getRange();
        if (nRange.isPresent()) {
            Range range = nRange.get();
            for (int i = range.begin.line; i <= range.end.line; i++) {
                excludedLines.add(Integer.valueOf(i));
            }
        }
    }

    /**
     * Getter for property 'excludedLines'.
     *
     * @return Value for property 'excludedLines'.
     */
    public List<Integer> getExcludedLines() {
        return excludedLines;
    }
}

