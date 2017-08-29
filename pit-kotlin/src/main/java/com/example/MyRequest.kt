package com.example

import java.io.Serializable

data class MyRequest(val userId:Long?) : Serializable {


    @Throws(IllegalStateException::class)
    fun validate() {

    }

    internal inner class Sub

    companion object {

        private const val serialVersionUID = -3548858114709541512L
    }
}