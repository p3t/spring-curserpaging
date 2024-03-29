plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
//    maven {
//        url = uri("https://repo.spring.io/release")
//    }
}

dependencies {
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.9")
    implementation("io.freefair.gradle:lombok-plugin:8.6")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.2.4")
//    implementation("io.spring.dependency-management:1.1.4")

//    implementation("io.spring.dependency-management:1.0.11.RELEASE")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.4")
//    implementation("io.spring.boot:dependency-management-plugin:1.1.4")
//    implementation("io.spring:dependency-management-plugin:1.1.4")
//    implementation("org.springframework.boot.gradle.plugin.SpringBootPlugin:3.2.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
}

