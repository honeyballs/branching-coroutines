package com.example.branchingcoroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BranchingCoroutinesApplication

fun main(args: Array<String>) {
    runApplication<BranchingCoroutinesApplication>(*args)
}
