plugins {
    id("java")
}

group = "org.ofus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.+")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks.jar {
    archiveFileName.set("Core.jar")

    providers.gradleProperty("targetDirectory").orNull?.let { targetDirectory ->
        destinationDirectory.set(file(targetDirectory))
    }

}


tasks.test {
    useJUnitPlatform()
}