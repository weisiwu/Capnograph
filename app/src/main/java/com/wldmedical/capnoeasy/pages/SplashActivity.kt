package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wldmedical.capnoeasy.R
import kotlinx.coroutines.delay

/***
 * 应用启动闪屏
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreen(onNavigateToMain = {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            })
        }
    }
}

@Composable
fun SplashScreen(onNavigateToMain: () -> Unit) {
    val configuration = LocalConfiguration.current
    val logoWidth = configuration.screenWidthDp.dp - 100.dp
    val alpha = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500) // 动画时长 500ms，即 0.5 秒
    )

    LaunchedEffect(key1 = true) {
        delay(1500)
        onNavigateToMain()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .alpha(alpha.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: 临时注释掉
//        Image(
//            painter = painterResource(id = R.drawable.wld_logo),
//            contentDescription = "WLD",
//            modifier = Modifier.width(logoWidth)
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplashScreen(onNavigateToMain = {})
}