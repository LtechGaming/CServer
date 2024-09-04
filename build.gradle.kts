plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.0.1"
}

group = "com.ltit.cserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.minestom:minestom-snapshots:789befee31")
    implementation("de.articdive:jnoise-pipeline:4.1.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Minestom has a minimum Java version of 21
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "com.ltit.cserver.Main" // Change this to your main class
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix on the shadowjar file.
    }
}