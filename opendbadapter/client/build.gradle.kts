plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    id("org.openapi.generator").version("5.4.0")
}

group = "com.aalpov"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

fun <T : Task> TaskProvider<T>.addTaskToBuildDependencies() =
    this.also {
        sourceSets.main {
            java.srcDirs(tasks.named(it.name))
        }
    }

repositories {
    mavenCentral()
}

dependencies {
    implementation("tanvd.aorm:aorm:1.1.7")
    implementation(project(":opendbadapter:core"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named("compileJava") {
    inputs.files(tasks.named("processResources"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
