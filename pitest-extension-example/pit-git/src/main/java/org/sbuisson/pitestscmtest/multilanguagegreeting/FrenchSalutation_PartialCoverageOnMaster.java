package org.sbuisson.pitestscmtest.multilanguagegreeting;

import org.sbuisson.pitestscmtest.Greeting;

/**
 * Created by sbuisson on 07/10/2017.
 */
public class FrenchSalutation_PartialCoverageOnMaster extends Greeting {

    @Override
    public String greet(String who) {
        return saluer(who);
    }

    public String saluer(String qui) {

        if (qui == null || qui.isEmpty()) {
            return "Y'a t-il quelqu'un?";
        }

        char firstChar = qui.charAt(0);
        String message;
        if (Character.isUpperCase(firstChar)) {
            message = "Bonjour, " + qui;
        } else {
            message = "salut, " + qui;
        }
        return message;
    }
}
