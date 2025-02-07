plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")  // Hilt 插件
    id("kotlin-kapt")
}

val vicoVersion = "2.0.1"

android {
    namespace = "com.wldmedical.capnoeasy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wldmedical.capnoeasy"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    // https://github.com/bcgit/bc-java/issues/1685
    packaging {
        resources.excludes.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.core)
    implementation(libs.androidx.constraintlayout)
    implementation("com.github.zj565061763:compose-wheel-picker:1.0.0-rc02")
    //https://www.patrykandpatrick.com/vico/guide/stable/getting-started
    implementation("com.patrykandpatrick.vico:compose-m3:$vicoVersion")
    implementation("androidx.core:core-splashscreen:1.0.1")
    //https://developer.android.com/training/dependency-injection/hilt-android?hl=zh-cn
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.appcompat)
    implementation(libs.identity.jvm)
    implementation(libs.androidx.bluetooth)
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // 安装 Coil 和对应的Compose 扩展
    implementation("io.coil-kt:coil:2.4.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.37.0")
    implementation(libs.androidx.baselibrary)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}