package com.example;

import org.junit.Test;

import static org.junit.Assert.fail;


public class MyRequestTest {

    @Test
    public void testValidateEmpty() {
        try {
            MyRequest req = new MyRequest();
            req.validate();
            fail();
        } catch (IllegalStateException ex) {
            // pass
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateOk() {
        final Long userId = 99L;
        MyRequest2 req = new MyRequest2();
        req.setUserId(userId);
        req.validate();

    }
}