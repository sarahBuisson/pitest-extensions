package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;


public class MyRequestTest {

   @Test
   public void testValidateEmpty() {
     try {
      MyRequest req = new MyRequest();
      req.validate();
      fail();
     } catch(IllegalStateException ex) {
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