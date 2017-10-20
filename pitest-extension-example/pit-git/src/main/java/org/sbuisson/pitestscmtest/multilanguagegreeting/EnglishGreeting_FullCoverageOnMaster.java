package org.sbuisson.pitestscmtest.multilanguagegreeting;

import org.sbuisson.pitestscmtest.Greeting;

/**
 * Created by sbuisson on 07/10/2017.
 */
public class EnglishGreeting_FullCoverageOnMaster extends Greeting {

    @Override
    public String greet(String who) {

        if(who==null || who.isEmpty()){
            return "is there someone here?";
        }

        char firstChar = who.charAt(0);
        String message;
        if (Character.isUpperCase(firstChar)) {
            message = "Hello, " + who;
        } else {
            message = "hy, " + who;
        }
        return message;
    }
}
