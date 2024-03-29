package warped.realms.entity

import generated.systems.createCmp
import kotlin.reflect.KClass

open class Entity {
    val cmps: MutableMap<KClass<*>, Any> = mutableMapOf()
    protected inline fun <reified T : Any> addCmp(noinline p: T.() -> T) {
        println("${T::class}")
//        cmps.getOrPut(T::class){
//            createCmp<T>(p)
//        }
        if (!cmps.equals(T::class)) cmps.put(T::class, createCmp<T>(p))
    }
    inline fun <reified T> getCmp(): T {
        return (cmps.get(T::class) as T)!!
    }
}
