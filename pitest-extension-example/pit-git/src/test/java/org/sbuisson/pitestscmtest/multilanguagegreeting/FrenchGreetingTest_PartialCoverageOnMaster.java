package org.sbuisson.pitestscmtest.multilanguagegreeting;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbuisson on 07/10/2017.
 */
public class FrenchGreetingTest_PartialCoverageOnMaster {

    FrenchSalutation_PartialCoverageOnMaster greeting = new FrenchSalutation_PartialCoverageOnMaster();

    @Test
    public void should_say_hello_to_monde() {
        assertEquals("Bonjour, Monde", greeting.greet("Monde"));
    }

    @Test
    public void should_say_hy_to_dog() {
        assertEquals("salut, le chien", greeting.greet("le chien"));
    }

   }
