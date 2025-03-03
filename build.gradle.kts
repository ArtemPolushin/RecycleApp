buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.secretsGradlePlugin)
        classpath(libs.kotlinGradlePlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
