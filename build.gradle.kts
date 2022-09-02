import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "1.6.21"
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.3.7"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.gestankbratwurst.core"
version = "1.2.0-SNAPSHOT"
description = "Core plugin für moderne Minecraft Server"
var exVersion = version

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.gestankbratwurst.core"
            artifactId = "MMCore"
            version = exVersion as String?

            from(components["java"])
        }
    }
}

dependencies {
    paperDevBundle("1.19.1-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    api("org.reflections:reflections:0.10.2")
    api("org.jetbrains.kotlin:kotlin-stdlib")
    api("org.jsoup:jsoup:1.15.2")
    api("com.github.ben-manes.caffeine:caffeine:3.1.1")
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    api("org.mongodb:mongodb-driver-sync:4.7.1")
    api("org.redisson:redisson:3.17.5")
    api("org.java-websocket:Java-WebSocket:1.5.3")
    // api("net.dv8tion:JDA:5.0.0-alpha.2")
    // implementation("org.apache.commons:commons-lang3:3.12.0")

    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0-SNAPSHOT")
}

tasks {
    build {
        dependsOn(reobfJar)
        finalizedBy(publishToMavenLocal)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "com.gestankbratwurst.core.mmcore.MMCore"
    apiVersion = "1.19"
    authors = listOf("Gestankbratwurst")
    depend = listOf("ProtocolLib")
    softDepend = listOf("ModelEngine")
}