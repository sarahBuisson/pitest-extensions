package com.github.sarahbuisson.pitest.annotation.generated.extension.interceptor;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by sarahbuisson on 01/11/2017.
 */
public class LineAnnotationExtractorTest {

    LineAnnotationExtractor extractor;

    @Before
    public void setup() {
        extractor = new LineAnnotationExtractor();
    }

    @Test
    public void should_exclude_line() throws Exception {
        //Given
        JavaToken begin = buildJavaToken(1);
        JavaToken end = buildJavaToken(2);
        MarkerAnnotationExpr annotationMarker = new MarkerAnnotationExpr(new TokenRange(begin, end), new Name());

        //When
        extractor.visit(annotationMarker, null);

        //Then
        assertTrue(extractor.getExcludedLines().contains(1));
        assertTrue(extractor.getExcludedLines().contains(2));

    }

    private JavaToken buildJavaToken(int line) {
        return new JavaToken(new Range(new Position(line, 2), new Position(line, 2)), 0, "", null, null);
    }

    @Test
    public void should_keep_line() throws Exception {
        //Given
        JavaToken begin = buildJavaToken(1);
        JavaToken end = buildJavaToken(2);
        ClassExpr marker = new ClassExpr(new TokenRange(begin, end), new ClassOrInterfaceType());

        //When
        extractor.visit(marker, null);

        //Then
        assertFalse(extractor.getExcludedLines().contains(Integer.valueOf(1)));
        assertFalse(extractor.getExcludedLines().contains(Integer.valueOf(2)));

    }

}