package com.pm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ProjectManagerApplication

fun main(args: Array<String>) {
	runApplication<ProjectManagerApplication>(*args)
}
