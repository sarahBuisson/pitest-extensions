package com.github.sarahbuisson.pitest.kotlin.extention.interceptor;

import com.github.sarahbuisson.kotlinparser.KotlinLexer;
import com.github.sarahbuisson.kotlinparser.KotlinParser;
import com.github.sarahbuisson.pitest.kotlin.extention.grammar.KotlinLinesToIgnoreListener;
import lombok.Getter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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
import org.pitest.util.Log;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Logger;

@Getter
public class KotlinDataInterceptor implements MutationInterceptor {

    private static final Logger LOG = Log
            .getLogger();

    private final SmartSourceLocator locator;
    private final ReportOptions data;
    private final ClassByteArraySource source;

    Map<String, Collection<Integer>> excludedLinesByFile = new HashMap<String, Collection<Integer>>();


    public KotlinDataInterceptor(SmartSourceLocator locator, ReportOptions data, ClassByteArraySource source) {
        this.locator = locator;
        this.data = data;
        this.source = source;
    }


    @Override
    public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
        return FCollection.filter(mutations, Prelude.not(isGeneratedLine()));
    }


    private F<MutationDetails, Boolean> isGeneratedLine() {

        return mutationDetails -> isMutationExcluded(mutationDetails);

    }

    /**
     * visible for test
     *
     * @param mutationDetails
     * @return
     */
    boolean isMutationExcluded(MutationDetails mutationDetails) {
        String fileId = mutationDetails.getClassName().toString() + mutationDetails.getFilename();
        if (!excludedLinesByFile.containsKey(fileId)) {
            excludedLinesByFile.put(fileId, extractExcludedLines(mutationDetails));
        }
        Collection<Integer> excludedLines = excludedLinesByFile.get(fileId);
        if (excludedLines != null && !excludedLines.isEmpty()) {
            return excludedLines.contains(Integer.valueOf(mutationDetails.getLineNumber()));
        }

        return false;
    }

    /**
     * visible for test
     *
     * @param mutationDetails
     * @return
     */
    Collection<Integer> extractExcludedLines(MutationDetails mutationDetails) {
        Option<Reader> reader = locator.locate(Arrays.asList(mutationDetails.getClassName().toString()), mutationDetails.getFilename());

        if (reader.hasSome()) {
            KotlinLexer lexer = null;
            try {
                lexer = new KotlinLexer(CharStreams.fromReader(reader.value()));
            } catch (IOException e) {
                LOG.throwing("", "", e);
            }


            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            KotlinParser parser = new KotlinParser(commonTokenStream);


            ParseTree tree = parser.kotlinFile();
            ParseTreeWalker walker = new ParseTreeWalker();

            KotlinLinesToIgnoreListener listener = new KotlinLinesToIgnoreListener();
            walker.walk(listener, tree);
            return listener.getLinesToIgnore();

        }
        return new ArrayList<>();

    }

    @Override
    public InterceptorType type() {
        return InterceptorType.FILTER;
    }

    @Override
    public void begin(ClassTree classTree) {
        //No action at the begin

    }

    @Override
    public void end() {
        //No action at the end

    }

}
