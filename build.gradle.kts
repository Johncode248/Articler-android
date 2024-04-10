
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.protobuf") version "0.8.13" apply false
}


buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        //classpath ("com.android.application:com.android.application.gradle.plugin:8.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath ("com.google.protobuf:protobuf-gradle-plugin:0.9.4")

    }
}