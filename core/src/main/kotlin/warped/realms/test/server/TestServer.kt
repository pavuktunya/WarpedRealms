package warped.realms.test.server

import kotlinx.coroutines.Runnable
import warped.realms.world.IDispose
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

class TestServer : IDispose {
    private val t0: Thread
    private val t1: Thread
    private val t2: Thread
    private var flag = true

    init {
        t0 = Thread {
            //val serverGameLogicSystem = GameLogicSystem()
            while (flag) {
                println("== Server: Game Logic")
                sleep(200)
            }
            //serverGameLogicSystem.dispose()
        }
        t1 = Thread {
            while (flag) {
                println("-- Server: Get Coord")
                sleep(200)
            }
        }
        t2 = Thread {
            while (flag) {
                println("-- Server: Push Coord")
                sleep(1000)
            }
        }
        t0.start()
        t1.start()
        t2.start()
    }

    override fun dispose() {
        flag = false
        t2.join()
        t0.join(50)
        t1.join(100)
    }
}

class Example {
    init {
        val list = mutableListOf<Int>()

        val lock = ReentrantReadWriteLock()
        val writeLock = lock.writeLock()

    }
}

fun MutableList<Int>.safeAdd(lock: Lock) {
    lock.lock()
    repeat(3) {
        add(it)
        sleep(1000)
    }
    lock.unlock()
}

fun MutableList<Int>.monitor() {
    repeat(6) {
        println(this)
        sleep(500)
    }
}

class Tasks {
    val executor = Executors.newSingleThreadExecutor().apply {
        val executor = this
        repeat(5) {
            val task = RunnableTask()
            executor.execute(task)
        }
        executor.shutdown()
    }
}

class RunnableTask() : Runnable {
    override fun run() {
        repeat(5) {
            println("Task $it")
            sleep(100)
        }
    }
}

class Counter {
    private var count = 0;
    private val lock = Any()

    fun increment() {
        synchronized(lock) {
            count++
        }
    }
}

class Lock {
    val lock = ReentrantLock()

    init {
        lock.lock()
        try {
            println("Only one thread can enter this block")
            println(lock.isLocked)
        } finally {
            lock.unlock()
        }
    }
}

class SafeStack {
    val stack = mutableListOf<Int>()
    val capacity = 5

    val lock = ReentrantLock()
    val stackEmptyCondition = lock.newCondition()
    val stackFullCondition = lock.newCondition()

    fun push(num: Int) {
        lock.lock()
        while (stack.size == capacity) {
            stackFullCondition.await()
        }
        stack.add(num)
        stackEmptyCondition.signalAll()
        lock.unlock()
    }

    fun pop() {
        lock.lock()
        while (stack.size == 0) {
            stackEmptyCondition.await()
        }
        stack.removeLast()
        stackFullCondition.signalAll()
        lock.unlock()
    }
}
