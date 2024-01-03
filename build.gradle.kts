import org.gradle.kotlin.dsl.support.listFilesOrdered

plugins {
    kotlin("jvm") version "1.9.10"
    `maven-publish`
}

group = "dev.gluton"

repositories {
    mavenCentral()
    mavenLocal()
    google()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(libs.revanced.patcher)
    implementation(libs.smali)
    // TODO: Required because build fails without it. Find a way to remove this dependency.
    implementation(libs.guava)
    // Used in JsonGenerator.
    implementation(libs.gson)

    // A dependency to the Android library unfortunately fails the build, which is why this is required.
    compileOnly(project("dummy"))
}

kotlin {
    jvmToolchain(11)
}

private val gradleProperty = object {
    operator fun get(name: String): String = extra[name] as String
}

tasks.withType(Jar::class) {
    manifest {
        attributes["Name"] = gradleProperty["patches.name"]
        attributes["Description"] = gradleProperty["patches.description"]
        attributes["Version"] = version
        attributes["Timestamp"] = System.currentTimeMillis().toString()
        attributes["Source"] = gradleProperty["patches.source"]
        attributes["Author"] = gradleProperty["patches.author"]
        attributes["Contact"] = gradleProperty["patches.email"]
        attributes["Origin"] = gradleProperty["patches.url"]
        attributes["License"] = gradleProperty["patches.license"]
    }
}

tasks {
    val generateBundle by registering(DefaultTask::class) {
        description = "Generate DEX files and add them in the JAR file"

        dependsOn(build)

        doLast {
            val d8 = File(System.getenv("ANDROID_HOME")).resolve("build-tools")
                .listFilesOrdered().last().resolve("d8").absolutePath

            val artifacts = configurations.archives.get().allArtifacts.files.files.first().absolutePath
            val workingDirectory = layout.buildDirectory.dir("libs").get().asFile

            exec {
                workingDir = workingDirectory
                commandLine = listOf(d8, artifacts)
            }

            exec {
                workingDir = workingDirectory
                commandLine = listOf("zip", "-u", artifacts, "classes.dex")
            }
        }
    }

    // Required to run tasks because Gradle semantic-release plugin runs the publish task.
    // Tracking: https://github.com/KengoTODA/gradle-semantic-release-plugin/issues/435
    named("publish") {
        dependsOn(generateBundle)
    }
}

publishing {
    publications {
        create<MavenPublication>("revanced-patches-publication") {
            from(components["java"])

            pom {
                name = gradleProperty["patches.name"]
                description = gradleProperty["patches.description"]
                url = gradleProperty["patches.url"]

                licenses {
                    license {
                        name = gradleProperty["patches.license"]
                        url = gradleProperty["patches.licenseUrl"]
                    }
                }
                developers {
                    developer {
                        id = gradleProperty["maven.userId"]
                        name = gradleProperty["patches.author"]
                        email = gradleProperty["patches.email"]
                    }
                }
                scm {
                    connection = "scm:git:git${gradleProperty["patches.githubUrl"].removePrefix("https")}.git"
                    developerConnection = "scm:git:${gradleProperty["patches.source"]}"
                    url = gradleProperty["patches.githubUrl"]
                }
            }
        }
    }
}
