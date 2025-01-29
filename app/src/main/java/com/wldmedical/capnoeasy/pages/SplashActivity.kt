package com.wldmedical.capnoeasy.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Image(
            painter = painterResource(id = R.drawable.wld_logo),
            contentDescription = "万联达信科",
            modifier = Modifier.width(logoWidth)
        )
        Text(
            text = "万联达信科",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 24.sp,
            color = Color(0xff3D3D3D),
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "WLD Instruments Co., Ltd",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            color = Color(0xff3D3D3D)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SplashScreen(onNavigateToMain = {})
}