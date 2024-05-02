plugins {
    kotlin("jvm") version "1.9.23"
}

group = "ru.levgrekov"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation ("org.postgresql:postgresql:42.7.3")
    implementation(project(":Communicator"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}