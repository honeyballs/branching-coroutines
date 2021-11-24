package com.example.branchingcoroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TestComponent {

    final var jobExecutor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()
    final var controllerExecutor: ThreadPoolTaskExecutor = ThreadPoolTaskExecutor()

    init {
        jobExecutor.corePoolSize = 4
        jobExecutor.maxPoolSize = 4
        jobExecutor.setQueueCapacity(10)
        jobExecutor.initialize()

        controllerExecutor.corePoolSize = 4
        controllerExecutor.maxPoolSize = 4
        controllerExecutor.setQueueCapacity(10)
        controllerExecutor.initialize()
    }


    @PostConstruct
    fun startWork() {
        for (i in 1..5) {
            println(controllerMethodAsync())
        }
        println("OK!")
    }

    fun controllerMethodAsync() = GlobalScope.launch(controllerExecutor.asCoroutineDispatcher()) {
        for (i in 1..5) {
            // Remove this executor to create a deadlock. The parent executor will be used which blocks threads from being
            // used in child jobs
            launch(jobExecutor.asCoroutineDispatcher()) {
                doWork("Job $i")
            }
        }
    }

    suspend fun doWork(text: String) {
        delay(1000L)
        println("running in thread ${Thread.currentThread()}")
        println(" $text done!")
    }

}