import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class System

@Target(AnnotationTarget.CLASS)
annotation class Update(val priority: Int)

@Target(AnnotationTarget.CLASS)
annotation class Component

@Target(AnnotationTarget.CLASS)
annotation class Factory(vararg val component: KClass<*>)

@Target(AnnotationTarget.CLASS)
annotation class PutComponent(vararg val component: KClass<*>)

@Target(AnnotationTarget.CLASS)
annotation class SystemScan(val path: String)

@Target(AnnotationTarget.CLASS)
annotation class FactoryScan(val path: String)
