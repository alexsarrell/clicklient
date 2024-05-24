plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.clickhouse:clickhouse-jdbc:0.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.springframework:spring-context")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.springframework.boot:spring-boot-starter-parent:3.1.0")
    implementation("org.springframework:spring-core")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.1.0")
    implementation("org.springframework.boot:spring-boot:3.1.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

/*
tasks.withType<JavaCompile>() {
    options.compilerArgs.addAll(listOf("-processor", "com.aalpov.opendbadapter.table.ClickhouseTableProcessor"))
}
*/

tasks.withType<Test> {
    useJUnitPlatform()
}

