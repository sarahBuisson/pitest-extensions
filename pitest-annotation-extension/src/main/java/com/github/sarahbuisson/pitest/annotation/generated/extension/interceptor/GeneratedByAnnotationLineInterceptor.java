package com.github.sarahbuisson.pitest.annotation.generated.extension.interceptor;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import lombok.Getter;
import org.apache.commons.io.input.ReaderInputStream;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.Option;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;

@Getter
class GeneratedByAnnotationLineInterceptor implements MutationInterceptor {

    private SmartSourceLocator locator;
    private ReportOptions data;
    private ClassByteArraySource source;

    private Map<String, List<Integer>> generatedLinesByFile = new HashMap();

    public GeneratedByAnnotationLineInterceptor(SmartSourceLocator locator, ReportOptions data, ClassByteArraySource source) {
        this.locator = locator;
        this.data = data;
        this.source = source;
    }

    @Override
    public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
        return FCollection.filter(mutations, isMutationOnGeneratedLineF());
    }

    private F<MutationDetails, Boolean> isMutationOnGeneratedLineF() {
        return Prelude.not(mutationDetails -> isMutationOnGeneratedLine(mutationDetails));
    }


    boolean isMutationOnGeneratedLine(MutationDetails mutationDetails) {

        String fileId = mutationDetails.getClassName().toString() + mutationDetails.getFilename();
        if (!generatedLinesByFile.containsKey(fileId)) {
            final List<Integer> excludedLines = extractExcludedLines(mutationDetails);
            generatedLinesByFile.put(fileId, excludedLines);
        }
        return generatedLinesByFile.get(fileId).contains(mutationDetails.getLineNumber());
    }

    /**
     * extract all the excluded lines of a file
     *
     * @param mutationDetails
     * @return
     */
    List<Integer> extractExcludedLines(MutationDetails mutationDetails) {

        Option<Reader> reader = locator.locate(Arrays.asList(mutationDetails.getClassName().toString()), mutationDetails.getFilename());

        if (reader.hasSome()) {
            CompilationUnit cu = JavaParser.parse(new ReaderInputStream(reader.value(), Charset.defaultCharset()));
            LineAnnotationExtractor extractor = new LineAnnotationExtractor();
            extractor.visit(cu, null);
            return extractor.getExcludedLines();

        }
        return new ArrayList<>();
    }

    @Override
    public InterceptorType type() {
        return InterceptorType.FILTER;
    }

    @Override
    public void begin(ClassTree clazz) {
        //No action at the begin
    }

    @Override
    public void end() {
        //No action at the end
    }
}
