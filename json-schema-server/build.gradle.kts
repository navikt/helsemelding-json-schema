plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("io.ktor.plugin")
    id("org.jlleitschuh.gradle.ktlint")
}

tasks {
    shadowJar {
        archiveFileName.set("app.jar")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=arrow.fx.coroutines.await.ExperimentalAwaitAllApi")
    }
}

dependencies {
    api(project(":json-schema-core"))
    implementation(libs.arrow.core)
    implementation(libs.arrow.functions)
    implementation(libs.arrow.suspendapp)
    implementation(libs.arrow.suspendapp.ktor)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.prometheus)
    implementation(libs.hoplite.core)
    implementation(libs.hoplite.hocon)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.openapi)
    implementation(libs.ktor.swagger.ui)
    implementation(libs.schema.kenerator.serialization)
    implementation(libs.ktor.server.netty)
    implementation(libs.kotlin.logging)
    testImplementation(testLibs.bundles.kotest)
    testImplementation(testLibs.kotest.assertions.arrow)
    testImplementation(testLibs.kotest.extensions.jvm)
    testImplementation(testLibs.kotest.extensions.testcontainers)
    testImplementation(testLibs.ktor.server.test.host)
    testImplementation(testLibs.testcontainers)
    testImplementation(testLibs.testcontainers.postgresql)
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("no.nav.helsemelding.jsonschema.server.AppKt")
}
