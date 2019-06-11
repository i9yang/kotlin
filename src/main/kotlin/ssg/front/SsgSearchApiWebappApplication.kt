package ssg.front

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration(exclude= [JestAutoConfiguration::class])
class SsgSearchApiWebappApplication

fun main(args: Array<String>) {
    runApplication<SsgSearchApiWebappApplication>(*args)
}