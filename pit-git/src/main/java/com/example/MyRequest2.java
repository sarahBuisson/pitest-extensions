package com.example;

import com.google.common.base.Preconditions;

import java.io.Serializable;


public class MyRequest2 implements Serializable {

   private static final long serialVersionUID = -3548858114709541512L;
   private Long userId;

   public void validate() throws IllegalStateException {
      Preconditions.checkState(userId != null);
   }

   /**
    * Getter for property 'userId'.
    *
    * @return Value for property 'userId'.
    */
   public Long getUserId() {
      return userId;
   }

   /**
    * Setter for property 'userId'.
    *
    * @param userId Value to set for property 'userId'.
    */
   public void setUserId(Long userId) {
      this.userId = userId;
   }

   class Sub{}
}