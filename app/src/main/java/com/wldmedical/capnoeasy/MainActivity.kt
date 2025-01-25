package com.wldmedical.capnoeasy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.components.ActionBar
import com.wldmedical.capnoeasy.components.DeviceTypeSwitch
import com.wldmedical.capnoeasy.components.Loading
import com.wldmedical.capnoeasy.components.NavBar
import com.wldmedical.capnoeasy.components.NavBarComponentState
import com.wldmedical.capnoeasy.components.PageScene
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mid = remember { mutableStateOf(1) }
            val historypage = remember { mutableStateOf(NavBarComponentState(currentPage = PageScene.HISTORY_LIST_PAGE)) }

            Scaffold { innerPadding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                ) {
                    Loading(text = "搜索设备中")
                    CapnoEasyTheme {
//                        NavBar(
//                            state = historypage,
//                            onRightClick = {
//                                // 处理点击事件的逻辑
//                                Log.d("TAG", "Button clicked")
//                            }
//                        )

                        DeviceTypeSwitch()
//                        ActionBar(
//                            selectedIndex = mid,
//                            onTabClick = {}
//                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CapnoEasyTheme {
        Greeting("Android")
    }
}