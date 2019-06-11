package ssg.front.ssgsearchtestwebapp

import org.junit.Test

class test {

    val lambda09 = {par1: Int, par2: Int -> par1 + par2}

    val lambda09b = {
        println("no parameters, a")
        val lamV1 = 10
        val lamV2 = 20
        println("no parameters, ${lamV1 + lamV2}")
    }


    @Test
    fun test() {
        var empty = ""
        println("${empty.isEmpty()} __ ${empty.isBlank()}")
        var blank = " "
        println("${blank.isEmpty()} __ ${blank.isBlank()}")

    }

}
