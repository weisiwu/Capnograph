package com.wldmedical.capnoeasy.kits

import java.util.LinkedList
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class BluetoothTaskQueue {

    private val taskQueue = LinkedList<Runnable>()
    private val lock = ReentrantLock()
    private var isExecuting = false

    /**
     * 添加单个任务到队列
     */
    fun addTask(task: Runnable) {
        lock.lock()
        try {
            taskQueue.offer(task)
        } finally {
            lock.unlock()
        }
    }

    /**
     * 添加多个任务到队列
     */
    fun addTasks(tasks: List<Runnable>) {
        lock.lock()
        try {
            taskQueue.addAll(tasks)
            println("wswTEst taskQueue ${taskQueue.size}")
        } finally {
            lock.unlock()
        }
    }

    /**
     * 执行任务队列中的一个任务
     */
    fun executeTask() {
        lock.lock()
        try {
            if (isExecuting || taskQueue.isEmpty()) return // 正在执行或队列为空，无需执行

            isExecuting = true
            thread {
                val task = taskQueue.peek()
                try {
                    task?.run()
                    taskQueue.poll() // 任务执行成功，从队列中移除
                } catch (e: Exception) {
                    println("wsw 任务执行失败：${e.message}")
                    // 失败计数器（这里简化为3次）
                    var retryCount = 0
                    while (retryCount < 3) {
                        retryCount++
                        try {
                            task?.run()
                            taskQueue.poll() // 任务执行成功，从队列中移除
                            break // 退出重试循环
                        } catch (e: Exception) {
                            println("wswTest 任务重试失败${retryCount}：${e.message}")
                        }
                    }
                    if (retryCount == 3) {
                        taskQueue.poll()
                    }

                } finally {
                    isExecuting = false // 任务执行完成，无论成功与否，都重置标志
                }
            }
        } finally {
            lock.unlock()
        }
    }

    /**
     * 依次执行任务队列中的所有任务
     */
    fun executeAllTasks() {  // 修改方法名，更清晰地表达功能
        lock.lock()
        try {
            if (isExecuting) return // 正在执行，无需重复执行

            isExecuting = true
            thread {
                while (taskQueue.isNotEmpty()) { // 循环执行，直到队列为空
                    val task = taskQueue.peek()
                    try {
                        task?.run()
                        taskQueue.poll() // 任务执行成功，从队列中移除
                    } catch (e: Exception) {
                        println("wsw 任务执行失败：${e.message}")
                        // 失败计数器（这里简化为3次）
                        var retryCount = 0
                        while (retryCount < 3) {
                            retryCount++
                            try {
                                task?.run()
                                taskQueue.poll() // 任务执行成功，从队列中移除
                                break // 退出重试循环
                            } catch (e: Exception) {
                                println("wswTest 任务重试失败${retryCount}：${e.message}")
                            }
                        }
                        if (retryCount == 3) {
                            taskQueue.poll()
                        }

                    }
                }
                isExecuting = false // 所有任务执行完成，重置标志
            }
        } finally {
            lock.unlock()
        }
    }

}
