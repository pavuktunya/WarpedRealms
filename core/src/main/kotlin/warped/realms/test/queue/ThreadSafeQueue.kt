package warped.realms.test.queue

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

//thread-safe queue
class ThreadSafeQueue {
    private val lock: ReentrantLock = ReentrantLock()

    private val queue = mutableListOf<Int>()
    private val capacity = 200

    private val stackEmptyCondition: Condition = lock.newCondition()
    private val stackFullCondition: Condition = lock.newCondition()

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

    fun pop(): Int? {
        lock.lock()
        //some data
        var data: Int? = null
        try {
            if (queue.size == 0) {
                stackEmptyCondition.await()
            }
            if (queue.size != 0) {
                data = queue.last()
                queue.removeLast()
                stackFullCondition.signalAll()

            }
        } finally {
            lock.unlock()
        }
        return data
    }
}
