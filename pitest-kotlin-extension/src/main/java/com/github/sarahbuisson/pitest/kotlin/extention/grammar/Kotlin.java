package com.github.sarahbuisson.pitest.kotlin.extention.grammar;

import com.github.sarahbuisson.kotlinparser.KotlinLexer;
import com.github.sarahbuisson.kotlinparser.KotlinParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Kotlin {
    public static void main(String [] args) throws Exception
    {

        ANTLRInputStream antlrInputStream = new ANTLRInputStream("Kotlin world");

        KotlinLexer lexer = new KotlinLexer(antlrInputStream);

        CommonTokenStream tokens = new CommonTokenStream( lexer );
        KotlinParser parser = new KotlinParser( tokens );
        ParseTree tree = parser.classBody();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk( new KotlinLinesToIgnoreListener(), tree );
    }
}