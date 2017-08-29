package com.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MyRequestTest2 {

   @Test
   public void testValidateEmpty() {
     try {
       MyRequest2 req = new MyRequest2();
      req.validate();
      fail();
     } catch(IllegalStateException ex) {
       // pass
     }
   }
     
  @Test
   public void testValidateOk() {
      final Long userId = 99L;
      MyRequest2 req = new MyRequest2();
      req.setUserId(userId);
      req.validate();

      assertEquals(userId, req.getUserId());
   }
}