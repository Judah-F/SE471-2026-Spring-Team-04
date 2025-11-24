plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.weatherboys"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("com.weatherboys.Launcher")
}

dependencies {
    // JSON parsing library
    implementation("org.json:json:20240303")

    // ZXing QR Code library
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    // MongoDB Java driver
    implementation("org.mongodb:mongodb-driver-sync:5.2.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.json:json:20240303")
}

tasks.test {
    useJUnitPlatform()
}