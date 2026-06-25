plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.gradleup.shadow")
    id("maven-publish")
}

dependencies {
    ksp(libs.kotlinx.schema.ksp)
    implementation(libs.arrow.core)
    implementation(libs.arrow.functions)
    implementation(libs.hoplite.core)
    implementation(libs.hoplite.hocon)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlin.logging)
    implementation(libs.kotlinx.schema.annotations)
    implementation(libs.kotlinx.schema.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.json.schema.validator)
    testImplementation(testLibs.bundles.kotest)
    testImplementation(testLibs.kotest.assertions.arrow)
    testImplementation(kotlin("test"))
}

tasks.register<JavaExec>("publishJsonSchemas") {
    dependsOn("compileKotlin")

    mainClass.set("no.nav.helsemelding.jsonschema.core.SchemaExporterKt")

    classpath =
        files(
            tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin").map {
                it.destinationDirectory
            }
        ) + sourceSets.main.get().compileClasspath

    args(
        layout.projectDirectory
            .dir("src/main/resources/schemas")
            .asFile
            .absolutePath
    )
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "no.nav.helsemelding"
            artifactId = "json-schema-core"
            version = "0.0.1-SNAPSHOT"
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/${rootProject.name}")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
