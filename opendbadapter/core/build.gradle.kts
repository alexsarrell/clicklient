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
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("com.clickhouse:clickhouse-jdbc:0.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.springframework:spring-context")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.springframework.boot:spring-boot-starter-parent:3.1.0")
    implementation("org.springframework:spring-core")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.1.0")
    implementation("org.springframework.boot:spring-boot:3.1.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.1")
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
