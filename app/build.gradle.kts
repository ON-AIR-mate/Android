plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    id("androidx.navigation.safeargs.kotlin") version "2.7.7"
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "umc.onairmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "umc.onairmate"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        buildConfig = true
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.converter.scalars)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)


    // Hilt 의존성 주입
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // 네트워크 이미지 로딩
    implementation(libs.coil)

    // 유튜브 플레이어 라이브러리
    implementation(libs.core)
    
    // CardView 의존성 추가
    implementation("androidx.cardview:cardview:1.0.0")

    // 채팅용 Socket
    implementation("io.socket:socket.io-client:2.0.0") {
        exclude(group = "org.json", module = "json") // 충돌 방지
    }
}