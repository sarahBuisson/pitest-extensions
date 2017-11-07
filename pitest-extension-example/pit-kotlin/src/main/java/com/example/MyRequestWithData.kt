package com.example

import java.io.Serializable

data class MyRequestWithData(val userId: Long?) : Serializable {


    @Throws(IllegalStateException::class)
    fun validate() {

    }

    internal inner class Sub

    companion object {

        private const val serialVersionUID = -3548858114709541512L
    }
}

class MyRequest2(val userId: Long) : Serializable {


    @Throws(IllegalStateException::class)
    fun validate(): Boolean {
        return userId > 100

    }

    internal inner class Sub

    companion object {

        private const val serialVersionUID = -3548858114709541512L
    }
}
