package com.example;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRequest2 implements Serializable {

   private static final long serialVersionUID = -3548858114709541512L;
   private Long userId;

   public void validate() throws IllegalStateException {
      Preconditions.checkState(userId != null);
   }

   class Sub{}
}