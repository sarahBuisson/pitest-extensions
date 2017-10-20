package com.github.sarahbuisson.pitest.kotlin.extention.grammar;


import com.github.sarahbuisson.kotlinparser.KotlinParser;
import com.github.sarahbuisson.kotlinparser.KotlinParserBaseListener;

import java.util.*;

public class KotlinLinesToIgnoreListener extends KotlinParserBaseListener {
    Set<Integer> linesToIgnore=new HashSet();


    @Override
    public void enterClassModifier(KotlinParser.ClassModifierContext ctx) {
        if (ctx.DATA() != null) {
            linesToIgnore.add(Integer.valueOf(ctx.DATA().getSymbol().getLine()));
        }
    }

 @Override
    public void enterClassDeclaration(KotlinParser.ClassDeclarationContext ctx) {
        if (ctx.CLASS() != null) {
            linesToIgnore.add(Integer.valueOf(ctx.CLASS().getSymbol().getLine()));
        }
    }

    /**
     * Getter for property 'lineToIgnore'.
     *
     * @return Value for property 'lineToIgnore'.
     */
    public Collection<Integer> getLinesToIgnore() {
        return linesToIgnore;
    }
}