package com.example

import org.junit.Test

class MyRequestTest {

    @Test
    fun testValidateEmpty() {

        val req = MyRequestWithData(2L)
        req.validate()


    }

    @Test
    fun testValidate2Empty() {

        val req = MyRequest2(2L)
        req.validate()


    }


}