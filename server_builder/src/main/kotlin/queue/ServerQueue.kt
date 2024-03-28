package server_builder.queue

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class ServerQueue() {
    //val lock = ReentrantReadWriteLock()
    //val writeLock = lock.writeLock()
    val lock: ReentrantLock = ReentrantLock()

    val queue = mutableListOf<Int>()
    val capacity = 2

    val stackEmptyCondition = lock.newCondition()
    val stackFullCondition = lock.newCondition()

    fun push(num: Int) {
        lock.lock()
        try {
            if (queue.size == capacity) {
                stackFullCondition.await()
            }
            if (queue.size != capacity) {
                queue.add(num)
                stackEmptyCondition.signalAll()
            }
        } finally {
            lock.unlock()
        }
    }

    fun pop(): Int {
        lock.lock()
        var t = 0
        try {
            if (queue.size == 0) {
                stackEmptyCondition.await()
            }
            if (queue.size != 0) {
                t = queue.last()
                queue.removeLast()

                stackFullCondition.signalAll()
            }
        } finally {
            lock.unlock()
        }
        return t
    }
}
