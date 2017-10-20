package org.sbuisson.pitestscmtest.multilanguagegreeting;

import org.sbuisson.pitestscmtest.Greeting;

/**
 * Created by sbuisson on 07/10/2017.
 */
public class DutchGrussen_NoCoverageOnMaster extends Greeting {

    @Override
    public String greet(String who) {
        return grussen(who);
    }

    public String grussen(String die) {

        if(die==null || die.isEmpty()){
            return "Y'a t-il quelqu'un?";
        }

        char firstChar = die.charAt(0);
        String message;
        if (Character.isUpperCase(firstChar)) {
            message = "GuttenTag, " + die;
        } else {
            message = "hallo, " + die;
        }
        return message;
    }


}
