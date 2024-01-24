package warped.realms.test.queue

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class ServerQueue() {
    //val lock = ReentrantReadWriteLock()
//val writeLock = lock.writeLock()
    val lock: ReentrantLock = ReentrantLock()

    val queue = mutableListOf<Int>()
    val capacity = 10

    val stackEmptyCondition = lock.newCondition()
    val stackFullCondition = lock.newCondition()

    fun push(num: Int) {
        lock.lock()
        try {
            while (queue.size == capacity) {
                stackFullCondition.await()
            }
            queue.add(num)
            stackEmptyCondition.signalAll()
        } finally {
            lock.unlock()
        }
    }

    fun pop(): Int {
        lock.lock()
        try {
            while (queue.size == 0) {
                stackEmptyCondition.await()
            }
            val t = queue.last()
            queue.removeLast()

            stackFullCondition.signalAll()
            lock.unlock()
            return t
        } finally {
            lock.unlock()
            return 0
        }
    }
}
