plugins {
   kotlin("jvm") version "1.8.10"
   kotlin("plugin.serialization") version "1.8.10"
   kotlin("plugin.allopen") version "1.8.10"
   id("java")
   id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dream"
version = "0.0.1-dev"

val implLibs = listOf(
   "org.jetbrains.kotlin:kotlin-stdlib:1.8.10",
   "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.10",
   "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10",
   "org.jetbrains.kotlin:kotlin-reflect:1.8.10"
)

val downloadLibs = listOf(
   
   // kotlin
   "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta",
   "org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0",
   "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0",
   
   // serialization
   "com.charleskorn.kaml:kaml:0.48.0",
   
   // arrow
   "io.arrow-kt:arrow-core:1.1.2",
   "io.arrow-kt:arrow-fx-coroutines:1.0.1",
   "io.arrow-kt:arrow-fx-stm:1.0.1",
   "io.arrow-kt:arrow-optics:1.1.2",
   
   // korlibs
   "com.soywiz.korlibs.korma:korma-jvm:4.0.0-alpha-6",
   "com.soywiz.korlibs.korim:korim-jvm:4.0.0-alpha-6",
   "com.soywiz.korlibs.krypto:krypto-jvm:4.0.0-alpha-6",
   "com.soywiz.korlibs.kmem:kmem-jvm:4.0.0-alpha-6",
   "com.soywiz.korlibs.kds:kds-jvm:4.0.0-alpha-6",
   "com.soywiz.korlibs.korio:korio-jvm:4.0.0-alpha-6",
   
   // apache
   "org.apache.commons:commons-lang3:3.12.0",
   "org.apache.commons:commons-collections4:4.4",
   "org.apache.commons:commons-math3:3.6.1",
   "org.apache.commons:commons-compress:1.22",
   "org.apache.commons:commons-text:1.9",
   "commons-io:commons-io:2.11.0",
   
   // google
   "com.google.code.gson:gson:2.9.0",
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
   "org.lz4:lz4-java:1.8.0",
   
   // time
   "joda-time:joda-time:2.11.1",
)

repositories {
   mavenLocal()
   mavenCentral()
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
}

allOpen {
   annotations("dream.misc.Open")
}
