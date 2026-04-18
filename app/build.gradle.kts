// 在文件顶部添加 import（如果还没有）
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.mrwang.coffeeapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mrwang.coffeeapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}
kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)     // ← 这里改成这样
        // 如果你还有其他选项，比如 languageVersion，也可以一起迁移
        // languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-core:1.7.8")
    implementation("io.coil-kt:coil-compose:2.6.0") // 请使用最新版本

    // 1. Coil: 用于加载网络图片 (如果你之前还没加的话)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // 2. Retrofit: 核心网络请求库
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // 3. Kotlinx Serialization Converter: 让 Retrofit 知道如何使用你已经配置好的 Kotlin Serialization 来解析 JSON
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")

    // 4. OkHttp Logging Interceptor: 极其重要的调试工具，能在 Logcat 里帮你打印出所有的网络请求网址和收到的 JSON 数据
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    val nav_version = "2.9.7"
    implementation("androidx.navigation:navigation-compose:2.9.7")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

}