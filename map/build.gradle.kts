import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val formattedDate = SimpleDateFormat("yyyyMMddHHmm").format(Date())
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    load(FileInputStream(keystorePropertiesFile))
}
val jksDir = keystorePropertiesFile.absolutePath.substring(
    0, keystorePropertiesFile.absolutePath.length - "keystore.properties".length
)

android {
    namespace = "com.melody.map.myapplication"
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.target.sdk.version.get().toInt()
        versionCode = libs.versions.lib.maven.version.code.get().toInt()
        versionName = libs.versions.lib.maven.version.name.get()

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(jksDir + keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
        create("release") {
            storeFile = file(jksDir + keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "19"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    applicationVariants.all {
        outputs.all {
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "gaode_${defaultConfig.versionName}_${formattedDate}-${buildType.name}.apk"
        }
    }
}

dependencies {
    implementation("io.github.TheMelody:gd_compose:1.0.7")
    debugImplementation(libs.leakcanary.android)
    implementation(platform(libs.compose.bom))
    api(libs.androidx.runtime)
    api(libs.core.ktx)
    api(libs.lifecycle.runtime.ktx)
    api(libs.lifecycle.viewmodel.compose)
    api(libs.activity.compose)
    api(libs.accompanist.permissions)
    api(libs.startup.runtime)
    implementation(platform(libs.compose.bom))
    api(libs.compose.ui)
    api(libs.compose.ui.tooling.preview)
    api(libs.foundation)
    api(libs.material)
    api(libs.accompanist.flowlayout)
    api(libs.accompanist.drawablepainter)
}
