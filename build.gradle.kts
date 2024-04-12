plugins {
  kotlin("jvm") version "2.0.0-Beta2"
  kotlin("plugin.serialization") version "2.0.0-Beta2"
  kotlin("plugin.allopen") version "2.0.0-Beta2"
  id("java")
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("idea")
}

group = "dream"
version = "0.0.1-dev"

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}


val implLibs = listOf(
  "org.jetbrains.kotlin:kotlin-stdlib:2.0.0-Beta2",
  "org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.0-Beta2",
  "org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.0-Beta2",
  "org.jetbrains.kotlin:kotlin-reflect:2.0.0-Beta2"
)

val downloadLibs = listOf(
  
  // kotlin
  "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2",
  "org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.2",
  "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2",
  "org.jetbrains.exposed:exposed-core:0.46.0",
  "org.jetbrains.exposed:exposed-dao:0.46.0",
  "org.jetbrains.exposed:exposed-jdbc:0.46.0",
  
  // serialization
  //"com.charleskorn.kaml:kaml:0.48.0",
  
  // arrow
  //"io.arrow-kt:arrow-core:1.1.2",
  //"io.arrow-kt:arrow-fx-coroutines:1.0.1",
  //"io.arrow-kt:arrow-fx-stm:1.0.1",
  //"io.arrow-kt:arrow-optics:1.1.2",
  
  // korlibs
  "com.soywiz.korlibs.korma:korma-jvm:4.0.10",
  "com.soywiz.korlibs.korim:korim-jvm:4.0.10",
  "com.soywiz.korlibs.krypto:krypto-jvm:4.0.10",
  "com.soywiz.korlibs.kmem:kmem-jvm:4.0.10",
  "com.soywiz.korlibs.kds:kds-jvm:4.0.10",
  "com.soywiz.korlibs.korio:korio-jvm:4.0.10",
  
  // apache
  "org.apache.commons:commons-lang3:3.14.0",
  "org.apache.commons:commons-collections4:4.4",
  "org.apache.commons:commons-math3:3.6.1",
  "org.apache.commons:commons-compress:1.25.0",
  "org.apache.commons:commons-text:1.11.0",
  "commons-io:commons-io:2.11.0",
  
  // google
  //"com.google.code.gson:gson:2.9.0",
  "com.google.guava:guava:31.1-jre",
  
  // database
  "org.xerial:sqlite-jdbc:3.39.3.0",
  "mysql:mysql-connector-java:8.0.28",
  "com.zaxxer:HikariCP:5.0.1",
  
  // collections
  "com.github.ben-manes.caffeine:caffeine:3.0.6",
  "it.unimi.dsi:fastutil:8.5.8",
  
  // connection
  "io.netty:netty-all:4.1.74.Final",
  
  // mojang
  "com.mojang:authlib:3.11.50",
  
  // math
  "net.jafama:jafama:2.3.2",
  
  // compression
  //"org.lz4:lz4-java:1.8.0",
  
  // time
  "joda-time:joda-time:2.11.1",
)

repositories {
  mavenLocal()
  mavenCentral()
  //maven(url = "https://jitpack.io")
  maven(url = "https://libraries.minecraft.net/")
  maven(url = "https://repo.kotlin.link")
  maven(url = "https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
  implLibs.forEach(::implementation)
  downloadLibs.forEach(::implementation)
}

tasks {
  compileKotlin {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime,kotlin.ExperimentalStdlibApi,kotlinx.coroutines.DelicateCoroutinesApi,kotlinx.coroutines.ExperimentalCoroutinesApi,kotlinx.serialization.ExperimentalSerializationApi,kotlinx.serialization.InternalSerializationApi,com.google.devtools.ksp.KspExperimental,kotlin.contracts.ExperimentalContracts"
  }

  shadowJar {
    destinationDirectory.set(file("C:\\Users\\Cliente\\Jetbrains\\Servidores\\Dream"))
    archiveFileName.set("ProjectDream.jar")
    manifest {
      attributes["Main-Class"] = "dream.app.MainKt"
    }
  }
}

allOpen {
  annotations("dream.misc.Open")
}

kotlin {
  sourceSets.all {
    languageSettings {
      languageVersion = "2.0"
    }
  }
}
