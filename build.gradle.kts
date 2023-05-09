plugins {
    kotlin("jvm") version "1.8.20"
    application
    kotlin("plugin.serialization") version "1.8.20"
    id("net.mamoe.mirai-console") version "2.15.0-M1"
}

group = "org.stg.verification.bot"
version = "1.0.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.zxing:core:3.5.1")
}

mirai {
    jvmTarget = JavaVersion.VERSION_17
}
