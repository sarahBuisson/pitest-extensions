package org.sbuisson.pitestscmtest;


import org.junit.Test;
import org.sbuisson.pitestscmtest.multilanguagegreeting.EnglishGreeting_FullCoverageOnMaster;

import static org.junit.Assert.assertEquals;

/**
 * Created by sbuisson on 07/10/2017.
 */
public class WorldTest {

    EnglishGreeting_FullCoverageOnMaster greeting = new EnglishGreeting_FullCoverageOnMaster();

    @Test
    public void should_say_hello_to_world() {
        assertEquals("Hello, World", greeting.greet("World"));
    }

 }
