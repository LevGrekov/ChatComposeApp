plugins {
    kotlin("jvm") version "1.9.23"
}

group = "ru.levgrekov"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":Communicator"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}