package org.sbuisson.pitestscmtest.multilanguagegreeting;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sbuisson on 07/10/2017.
 */
public class EnglishGreetingTest_FullCoverageOnMaster {

    EnglishGreeting_FullCoverageOnMaster greeting = new EnglishGreeting_FullCoverageOnMaster();

    @Test
    public void should_say_hello_to_world() {
        assertEquals("Hello, World", greeting.greet("World"));
    }

    @Test
    public void should_say_hy_to_dog() {
        assertEquals("hy, dog", greeting.greet("dog"));
    }

    @Test
    public void should_ask_where_no_one() {
        assertEquals("is there someone here?", greeting.greet(""));
    }
}
