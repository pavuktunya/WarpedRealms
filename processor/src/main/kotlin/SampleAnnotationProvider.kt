package ksp.test.provider

import Component
import Factory
import PutComponent
import System
import Update
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import java.io.OutputStreamWriter

class SampleAnnotationProcessor(val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    val processed_systems = mutableListOf<ClassName>()
    var processed_update = mutableMapOf<ClassName, Int>()
    val processed_not_update = mutableListOf<ClassName>()
    //val processed_scan_root_system = mutableListOf<ClassName>()

    val processed_components = mutableListOf<ClassName>()
    val processed_factories = mutableMapOf<String, ClassName>()

    val processed_component_factory = mutableMapOf<ClassName, ClassName>()
    val processed_component_system = mutableMapOf<ClassName, List<ClassName>>()

    val processed_systems_put = mutableMapOf<ClassName, List<String>>()
    //val processed_scan_root_factory = mutableListOf<ClassName>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val systems = resolver.getSymbolsWithAnnotation(System::class.qualifiedName!!, inDepth = false)
            .filterIsInstance<KSClassDeclaration>()
        val update_systems = resolver.getSymbolsWithAnnotation(Update::class.qualifiedName!!, inDepth = false)
            .filterIsInstance<KSClassDeclaration>()

        val put_systems = resolver.getSymbolsWithAnnotation(PutComponent::class.qualifiedName!!, inDepth = false)
            .filterIsInstance<KSClassDeclaration>()
        val components = resolver.getSymbolsWithAnnotation(Component::class.qualifiedName!!, inDepth = false)
            .filterIsInstance<KSClassDeclaration>()
        val factories = resolver.getSymbolsWithAnnotation(Factory::class.qualifiedName!!, inDepth = false)
            .filterIsInstance<KSClassDeclaration>()

        systems.forEach { system ->
            val classSpec = system.asType(listOf()).toClassName()

            if (!processed_systems.contains(classSpec)) {
                processed_systems.add(classSpec)

                if (update_systems.contains(system)) {
                    val property = system.annotations.find {
                        it.shortName.asString() == Update::class.java.simpleName
                    }!!
                    val s = property.arguments.find { it.name?.asString() == "priority" }!!
                    val priority = s.toString().removePrefix("priority:").toInt()

                    processed_update[classSpec] = priority
                    processed_update = processed_update.toSortedMap()
                } else {
                    processed_not_update.add(classSpec)
                }

                if (put_systems.contains(system)) {
                    val property = system.annotations.find {
                        it.shortName.asString() == PutComponent::class.java.simpleName
                    }!!
                    val s = property.arguments.find { it.name?.asString() == "component" }!!
                    val value = s.toString().removePrefix("component:").removePrefix("[").removeSuffix("]").split(", ")

                    processed_systems_put[classSpec] = value
                }
//                val packageName = classSpec.packageName
//                val properties = system.getAllProperties()
//                val poetProperties = mutableListOf<PropertySpec>()
//                val constructorParams = mutableListOf<ParameterSpec>()
//                val resultTemplate = mutableListOf<String>()
//                properties.forEach {
//                 val name = it.simpleName.getShortName()
            }
        }

        factories.forEach { factory ->
            val classSpec = factory.asType(listOf()).toClassName()

            val property = factory.annotations.find {
                it.shortName.asString() == Factory::class.java.simpleName
            }!!
            val s = property.arguments.find { it.name?.asString() == "component" }!!
            val values = s.toString().removePrefix("component:").removePrefix("[").removeSuffix("]").split(", ")
            values.forEach { value ->
                if (!processed_factories.contains(value)) {
                    processed_factories[value] = classSpec
                }
            }
        }
        components.forEach { component ->
            val classSpec = component.asType(listOf()).toClassName()
            if (!processed_components.contains(classSpec)) {
                processed_components.add(classSpec)
                if (processed_factories.get(classSpec.simpleName.toString()) != null)
                    processed_component_factory[classSpec] = processed_factories.get(classSpec.simpleName.toString())!!

                val systems1 = mutableListOf<ClassName>()
                processed_systems_put.forEach {
                    if (it.value.contains(classSpec.simpleName.toString()))
                        systems1.add(it.key)
                }
                if (systems1.isNotEmpty())
                    processed_component_system[classSpec] = systems1
            }
        }
        /*
                for (scan in scan_root_system) {
                    processed_scan_root_system.add(scan.asType(listOf()).toClassName())
                }
                for(scan in scan_root_factory){
                    processed_scan_root_factory.add(scan.asType(listOf()).toClassName())
                }
        */
        val output = Output(
            OutputSystems(
                processed_systems,
                processed_update,
                processed_not_update,
                //processed_scan_root_system.first()
            ),
            OutputFactories(
                processed_components,
                processed_factories,
                processed_component_factory,
                processed_component_system,
                processed_systems_put,
                //processed_scan_root_factory.first()
            )
        )

        val writer1 = createFile("generated.systems", "Systems")
        output.writeSystems(writer1)
        writer1.flush()
        writer1.close()

        val writer2 = createFile("generated.systems", "Factories")
        output.writeFabrics(writer2)
        writer2.flush()
        writer2.close()

        return systems
            .plus(components)
            .plus(factories)
            //.plus(scan_root_system)
            //.plus(scan_root_factory)
            .toList()
    }

    fun createFile(
        packageName: String,
        fileName: String
    ): OutputStreamWriter {
        val file = try {
            environment.codeGenerator.createNewFile(
                packageName = packageName,
                dependencies = Dependencies(false),
                fileName = fileName,
                extensionName = "kt"
            )
        } catch (e: FileAlreadyExistsException) {
            e.file.outputStream()
        }
        return file.writer()
    }

    override fun finish() {
        super.finish()
    }
}

class SampleAnnotationProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SampleAnnotationProcessor(environment)
}

class OutputSystems(
    val processed_systems: List<ClassName>,
    var processed_update: Map<ClassName, Int>,
    val processed_not_update: List<ClassName>,
    //val scan_root: ClassName,
)

class OutputFactories(
    val processed_components: List<ClassName>,
    val processed_factories: Map<String, ClassName>,
    val processed_component_factory: Map<ClassName, ClassName>,
    val processed_component_system: Map<ClassName, List<ClassName>>,
    val processed_systems_put: Map<ClassName, List<String>>,
    //val scan_root: ClassName,
)

class Output(
    val systems: OutputSystems,
    val factories: OutputFactories
) {
    fun writeFabrics(writer: OutputStreamWriter) {
        factories.apply {
            writer.appendLine("package generated.systems\n")
            writer.appendLine("import ktx.app.gdxError")
            writer.appendLine("import kotlin.reflect.KClass")
            //writer.appendLine("import systems.test.injectCmpSys")
//
            writer.appendLine("import warped.realms.entity.Entity")
            val factories = mutableListOf<ClassName>()
            for (processedFactory in processed_factories) {
                if (!factories.contains(processedFactory.value)) {
                    factories.add(processedFactory.value)
                }
            }
            factories.forEach {
                writer.appendLine("import ${it.canonicalName}")
                writer.appendLine("// ${it}")
            }
            processed_systems_put.forEach {
                writer.appendLine("import ${it.key.canonicalName}")
                writer.appendLine("// ${it.value}")
            }
            processed_components.forEach {
                writer.appendLine("import ${it.canonicalName}")
            }
///
            //writer.appendLine("import ${scan_root.canonicalName}")

            writer.appendLine("\nobject Factories{")
//
            for (i in 1..processed_component_factory.size) {
                writer.appendLine("    val factory${i} = ${processed_component_factory.toList()[i - 1].second.simpleName}()")
            }
///
            writer.appendLine("    val factories: HashMap<KClass<*>, *> = hashMapOf(")
//
            for (i in 1..processed_component_factory.size) {
                writer.appendLine("        ${processed_component_factory.toList()[i - 1].first.simpleName}::class to factory${i},")
            }
///
            writer.appendLine("    )\n")
//
            for (i in 1..processed_component_system.size) {
                writer.appendLine("    val ${processed_component_system.toList()[i - 1].first.simpleName}Systems: List<*> = listOf(")
                for (j in 1..processed_component_system.toList()[i - 1].second.size) {
                    writer.appendLine("        ${processed_component_system.toList()[i - 1].second[j - 1].simpleName}::class,")
                }
                writer.appendLine("    )\n")
            }
///
            writer.appendLine("    val components: HashMap<KClass<*>, List<*>> = hashMapOf(")
//
            for (i in 1..processed_component_system.size) {
                writer.appendLine("        ${processed_component_system.toList()[i - 1].first.simpleName}::class to ${processed_component_system.toList()[i - 1].first.simpleName}Systems,")
            }
///
            writer.appendLine("    )\n")
//
            writer.appendLine("    fun put(cmps: MutableMap<KClass<*>, Any>){")
            for (i in 0..<processed_components.size) {
                writer.appendLine("        val ${processed_components.toList()[i].simpleName}Cmps = mutableListOf<${processed_components.toList()[i].simpleName}>()")
            }
            writer.appendLine("        cmps.forEach { kcls, cmp ->")
            writer.appendLine("            when(kcls){")
            for (i in 0..<processed_components.size) {
                writer.appendLine("                ${processed_components.toList()[i].simpleName}::class -> ${processed_components.toList()[i].simpleName}Cmps.add((cmp as ${processed_components.toList()[i].simpleName})!!)")
            }
            writer.appendLine("                else -> error(\"No such KClass<*> in your system\")")
            writer.appendLine("            }")
            writer.appendLine("        }")

            val systems2 = mutableListOf<ClassName>()
            for (i in 1..processed_component_system.size) {
                for (j in 1..processed_component_system.toList()[i - 1].second.size) {
                    if (!systems2.contains(processed_component_system.toList()[i - 1].second[j - 1])) {
                        val components = mutableListOf<String>()
                        var str1 = ""
                        var str2 = ""
                        var str3 = ""
                        processed_component_system.filter { it.value.contains(processed_component_system.toList()[i - 1].second[j - 1]) }.keys.reversed()
                            .forEach {
                                components.add(it.simpleName)
                                if (str1 == "")
                                    str1 += it.simpleName + "Cmps.isNotEmpty()"
                                else
                                    str1 += " && " + it.simpleName + "Cmps.isNotEmpty()"
                                if (str2 == "")
                                    str2 += it.simpleName + "Cmps.size"
                                else str2 += ", " + it.simpleName + "Cmps.size"
                                if (str3 == "")
                                    str3 += it.simpleName + "Cmps[i]"
                                else str3 += ", " + it.simpleName + "Cmps[i]"
                            }
                        writer.appendLine("        if(${str1}) {")
                        writer.appendLine("            val system = injectSys<${processed_component_system.toList()[i - 1].second[j - 1].simpleName}>()")
                        writer.appendLine("            val maxSize = minOf(${str2})")
                        writer.appendLine("            for(i in 0..<maxSize)")
                        writer.appendLine("                system.PutComponent(${str3})")
                        writer.appendLine("        }")
                        systems2.add(processed_component_system.toList()[i - 1].second[j - 1])
                    }
                }
            }
            writer.appendLine("    }\n")
///
            writer.appendLine("    inline fun<reified T> delete(cmp: T){")
            writer.appendLine("        injectCmpSys<T>().forEach {")
            writer.appendLine("            when(it){")
//
            val systems3 = mutableListOf<ClassName>()
            for (i in 1..processed_component_system.size) {
                for (j in 1..processed_component_system.toList()[i - 1].second.size) {
                    if (!systems3.contains(processed_component_system.toList()[i - 1].second[j - 1])) {
                        writer.appendLine("                ${processed_component_system.toList()[i - 1].second[j - 1].simpleName}::class -> when(T::class){")
                        processed_component_system.filter { it.value.contains(processed_component_system.toList()[i - 1].second[j - 1]) }
                            .forEach {
                                writer.appendLine("                    ${it.key.simpleName}::class -> injectSys<${processed_component_system.toList()[i - 1].second[j - 1].simpleName}>().DeleteComponent((cmp as ${it.key.simpleName})!!)")
                            }
                        writer.appendLine("                }")
                        systems3.add(processed_component_system.toList()[i - 1].second[j - 1])
                    }
                }
            }
///
            writer.appendLine("            }")
            writer.appendLine("        }")
            writer.appendLine("    }")

            writer.appendLine("}\n")

            //writer.appendLine("public val ${scan_root.simpleName}.factories")
            //writer.appendLine("    get() = Factories\n")

            writer.appendLine("inline fun <reified T> createCmp(noinline lambda: T.()-> T): T{")
            writer.appendLine("    val factory = injectFac<T>()")
            writer.appendLine("    val cmp = when(factory){")

//
            for (i in 1..factories.size) {
                writer.appendLine("        is ${factories[i - 1].simpleName} -> {")
                writer.appendLine("            when(T::class){")
                val components = processed_component_factory.filter { it.value == factories[i - 1] }
                components.forEach {
                    writer.appendLine("                ${it.key.simpleName}::class -> Factories.factory${i}.Factory(lambda as (${it.key.simpleName}.()-> ${it.key.simpleName}))")
                }
                writer.appendLine("                else -> error(\"No such components for factory \${T::class}\")")
                writer.appendLine("            }")
                writer.appendLine("        }")
            }
///
            writer.appendLine("        else -> null")
            writer.appendLine("    }")
//                "!!.also {")
//            writer.appendLine("        Factories.put<T>((it as T)!!)")
//            writer.appendLine("    }")
            writer.appendLine("    return (cmp as T)!!")
            writer.appendLine("}\n")

//
            writer.appendLine("inline fun Entity.initEntity(){")
            writer.appendLine("//    this.cmps.forEach { t, u ->")
            writer.appendLine("//        Factories.delete(u)")
            writer.appendLine("//    }")
            writer.appendLine("    Factories.put(this.cmps)")
            writer.appendLine("}\n")
///
            writer.appendLine("inline fun <reified T> deleteCmp(cmp: T){")
            writer.appendLine("    Factories.delete<T>(cmp as T)")
            writer.appendLine("    val factory = injectFac<T>()")
            writer.appendLine("    when(factory){")

//
            for (i in 1..factories.size) {
                writer.appendLine("        is ${factories[i - 1].simpleName} -> {")
                writer.appendLine("            when(T::class){")
                val components = processed_component_factory.filter { it.value == factories[i - 1] }
                components.forEach {
                    writer.appendLine("                ${it.key.simpleName}::class -> Factories.factory${i}.Delete((cmp as ${it.key.simpleName})!!)")
                }
                writer.appendLine("                else -> error(\"No such components for factory \${T::class}\")")
                writer.appendLine("            }")
                writer.appendLine("        }")
            }
///
            writer.appendLine("        else -> error(\"No such Factory with this component: \${T::class}\")")
            writer.appendLine("    }")
            writer.appendLine("}\n")


            writer.appendLine("inline fun <reified T> injectCmpSys(): List<*> {")
            writer.appendLine("    val cl: KClass<*> = (T::class)")
            writer.appendLine("    val systems = Factories.components.get(cl)!!")
            writer.appendLine("    return systems")
            writer.appendLine("}\n")

            writer.appendLine("inline fun <reified T> injectFac(): Any {")
            writer.appendLine("    val cl: KClass<*> = (T::class)")
            writer.appendLine("    val factory= Factories.factories.get(cl)!!")
            writer.appendLine("    return factory")
            writer.appendLine("}")
        }
    }

    fun writeSystems(writer: OutputStreamWriter) {
        systems.apply {
            writer.appendLine("package generated.systems\n")
            writer.appendLine("import ktx.app.gdxError")
            writer.appendLine("import kotlin.reflect.KClass")
            processed_systems.forEach {
                writer.appendLine("import ${it.canonicalName}")
            }
            //writer.appendLine("import ${scan_root.canonicalName}")
            writer.appendLine("\nobject Systems{")

            writer.appendLine("    const val size = ${processed_update.size}")
            for (i in 0..<processed_update.size) {
                val system = processed_update.toList()[i].first
                writer.appendLine("    private val system${i + 1} = ${system.simpleName}()")
            }
            writer.appendLine("\n    val systems: HashMap<KClass<*>, *> = hashMapOf(")
            for (i in 0..<processed_update.size) {
                val system = processed_update.toList()[i].first
                writer.appendLine("        ${system.simpleName}::class to system${i + 1},")
            }
            writer.appendLine()
            for (i in 0..<processed_not_update.size) {
                val system = processed_not_update[i]
                writer.appendLine("        ${system.simpleName}::class to ${system.simpleName}(),")
            }
            writer.appendLine("    )")

            writer.appendLine("    fun Update(deltaTime: Float){")
            writer.appendLine("        for(i in 1..size){")
            writer.appendLine("            when(i){")
            for (i in 0..<processed_update.size) {
                writer.appendLine("                ${i + 1} -> system${i + 1}.Update(deltaTime)")
            }
            writer.appendLine("            }")
            writer.appendLine("        }")
            writer.appendLine("    }")

            writer.appendLine("    fun Dispose(){")
            writer.appendLine("        for(system in systems){")
            writer.appendLine("            when(system::class){")
            for (system in processed_systems) {
                writer.appendLine("                ${system.simpleName}::class -> injectSys<${system.simpleName}>().Dispose()")
            }
            writer.appendLine("            }")
            writer.appendLine("        }")
            writer.appendLine("    }")

            writer.appendLine("}\n")

            //writer.appendLine("public val ${scan_root.simpleName}.systems")
            //writer.appendLine("    get() = Systems\n")

            writer.appendLine("inline fun <reified T> injectSys(): T {")
            writer.appendLine("    val cl: KClass<*> = (T::class)")
            writer.appendLine("    val system = Systems.systems.get(cl)!!")
            writer.appendLine("    return when(system){")
            writer.appendLine("        is T -> system")
            writer.appendLine("        else -> gdxError(\"Doesn't have any system:" + "\$system\")")
            writer.appendLine("    }")
            writer.appendLine("}")
        }
    }
}
