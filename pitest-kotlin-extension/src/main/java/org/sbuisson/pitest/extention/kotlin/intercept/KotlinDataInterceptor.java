package org.sbuisson.pitest.extention.kotlin.intercept;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.pitest.bytecode.analysis.ClassTree;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.InterceptorParameters;
import org.pitest.mutationtest.build.InterceptorType;
import org.pitest.mutationtest.build.MutationInterceptor;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Mutater;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.tooling.SmartSourceLocator;
import org.sbuisson.pitest.extention.kotlin.intercept.grammar.KotlinLexer;
import org.sbuisson.pitest.extention.kotlin.intercept.grammar.KotlinParser;
import org.sbuisson.pitest.extention.kotlin.intercept.grammar.KotlinLinesToIgnoreListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


class KotlinDataInterceptor implements MutationInterceptor {

    private final SmartSourceLocator locator;
    ReportOptions data;
    ClassByteArraySource source;

    Map<String, List<Integer>> excludedLinesByFile = new HashMap<String, List<Integer>>();


    KotlinDataInterceptor(InterceptorParameters interceptorParameters) {
        this.data = interceptorParameters.data();
        this.source = interceptorParameters.source();
        this.locator = new SmartSourceLocator(data.getSourceDirs());

    }


    public Collection<MutationDetails> intercept(Collection<MutationDetails> mutations, Mutater m) {
        return FCollection.filter(mutations, Prelude.not(isGeneratedLine()));
    }


    private F<MutationDetails, Boolean> isGeneratedLine() {

        return new F<MutationDetails, Boolean>() {

            public Boolean apply(MutationDetails mutationDetails) {
                return isMutationExcluded(mutationDetails);

            }
        };

    }

    private void extractExcludedLine( String fileName ) {
        KotlinLexer KotlinLexer = null;
        try {
            KotlinLexer = new KotlinLexer(CharStreams.fromStream(new FileInputStream(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }


        CommonTokenStream commonTokenStream = new CommonTokenStream(KotlinLexer);
        KotlinParser kotlinParser = new KotlinParser(commonTokenStream);


        ParseTree tree = kotlinParser.kotlinFile();
        ParseTreeWalker walker = new ParseTreeWalker();

        KotlinLinesToIgnoreListener listener = new KotlinLinesToIgnoreListener();
        walker.walk(listener, tree);
        excludedLinesByFile.put(fileName, listener.getLinesToIgnore());
    }

    private boolean isMutationExcluded(MutationDetails mutationDetails) {
        KotlinLexer KotlinLexer = null;
        String fileName = null;
        for (File sourceDir : this.data.getSourceDirs()) {
            fileName = sourceDir.getAbsolutePath() + "/" + mutationDetails.getClassName().getPackage().asInternalName() + "/" + mutationDetails.getFilename();
            File f = new File(fileName);
            if (f.exists()) {
               break;

            }
        }
        if(!excludedLinesByFile.containsKey(fileName)){
            extractExcludedLine(fileName);
        }
        List<Integer> excludedLines = excludedLinesByFile.get(fileName);
        if (excludedLines != null && !excludedLines.isEmpty()) {
            return excludedLines.contains(Integer.valueOf(mutationDetails.getLineNumber()));
        }

        return false;
    }


    public InterceptorType type() {
        return InterceptorType.FILTER;
    }

    public void begin(ClassTree classTree) {


    }

    public void end() {

    }
}
