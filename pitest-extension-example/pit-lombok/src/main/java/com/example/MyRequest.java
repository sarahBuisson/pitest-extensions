package com.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRequest implements Serializable {

    private static final long serialVersionUID = -3548858114709541512L;
    private Long userId;

    public void validate() throws IllegalStateException {
        if (userId == null) {
            throw new IllegalStateException();
        }
    }

    class Sub {
    }
}