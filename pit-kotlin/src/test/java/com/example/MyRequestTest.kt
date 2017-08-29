package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.fail

import org.junit.Test

import com.example.MyRequest

class MyRequestTest {

    @Test
    fun testValidateEmpty() {

            val req = MyRequest(2L)
            req.validate()


    }


}