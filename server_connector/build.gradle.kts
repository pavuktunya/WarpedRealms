val ktor_version: String by project
val ktxVersion: String by project
val kotlinxCoroutinesVersion: String by project
val gdxVersion: String by project
val kotlinVersion: String by project

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    api("io.github.libktx:ktx-log:$ktxVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinxCoroutinesVersion}")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")

    implementation(project(":server_builder"))
}

