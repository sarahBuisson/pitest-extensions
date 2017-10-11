package org.sbuisson.pitest.extention.kotlin.intercept.grammar;

import java.util.ArrayList;
import java.util.List;

public class KotlinLinesToIgnoreListener extends KotlinParserBaseListener {
    List<Integer> linesToIgnore=new ArrayList<Integer>();


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
    public List<Integer> getLinesToIgnore() {
        return linesToIgnore;
    }
}