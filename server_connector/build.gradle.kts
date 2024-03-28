val ktor_version: String by project
val kotlinxCoroutinesVersion: String by project

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(project(":server_builder"))
    api("io.github.libktx:ktx-log:1.12.1-rc1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinxCoroutinesVersion}")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
}

