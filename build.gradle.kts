import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.0" apply false
    kotlin("plugin.serialization") version "2.1.10" apply false
    id("com.google.devtools.ksp") version "2.3.9" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
    id("com.gradleup.shadow") version "8.3.6" apply false
    id("io.ktor.plugin") version "3.5.0" apply false
}

subprojects {
    plugins.withId("org.jetbrains.kotlin.jvm") {
        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            filter {
                exclude { it.file.path.contains("/ksp/") }
            }
        }

        tasks.named("ktlintCheck") {
            mustRunAfter("ktlintFormat")
        }

        tasks.named("build") {
            dependsOn("ktlintCheck")
        }
    }
}

