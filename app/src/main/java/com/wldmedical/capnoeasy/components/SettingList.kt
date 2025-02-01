package com.wldmedical.capnoeasy.components

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.pages.AlertSettingActivity
import com.wldmedical.capnoeasy.pages.DisplaySettingActivity
import com.wldmedical.capnoeasy.pages.HistoryRecordsActivity
import com.wldmedical.capnoeasy.pages.ModuleSettingActivity
import com.wldmedical.capnoeasy.pages.PrintSettingActivity
import com.wldmedical.capnoeasy.pages.SettingActivity
import com.wldmedical.capnoeasy.pages.SystemSettingActivity

enum class SettingType(value: Int) {
    ZERO(0), // 校零
    KEEP_LIGHT(1), // 屏幕常亮
    ALERT_PARAM(2), // 报警参数
    DISPLAY_PARAM(3), // 显示参数
    MODULE_PARAM(4), // 模块参数
    SYSTEM_SETTING(5), // 系统设置
    PRINT_SETTING(6), // 打印设置
    HISTORY_RECORD(7), // 历史记录
    SHUTDOWN(8), // 关机
}

data class Setting(
    val name: String,
    val icon: Int,
    val type: SettingType = SettingType.ZERO
)

val settings = arrayOf(
    Setting(
        name = "校零",
        icon = R.drawable.m3_refresh,
        type = SettingType.ZERO
    ),
    Setting(
        name = "屏幕常亮",
        icon = R.drawable.m3_lightbulb,
        type = SettingType.KEEP_LIGHT
    ),
    Setting(
        name = "报警参数",
        icon = R.drawable.m3_arrow_forward,
        type = SettingType.ALERT_PARAM
    ),
    Setting(
        name = "显示参数",
        icon = R.drawable.m3_arrow_forward,
        type = SettingType.DISPLAY_PARAM
    ),
    Setting(
        name = "模块参数",
        icon = R.drawable.m3_arrow_forward,
        type = SettingType.MODULE_PARAM
    ),
    Setting(
        name = "系统设置",
        icon = R.drawable.m3_arrow_forward,
        type = SettingType.SYSTEM_SETTING
    ),
    Setting(
        name = "打印设置",
        icon = R.drawable.m3_arrow_forward,
        type = SettingType.PRINT_SETTING
    ),
    Setting(
        name = "历史记录",
        icon = R.drawable.m3_arrow_forward,
        type = SettingType.HISTORY_RECORD
    ),
    Setting(
        name = "关机",
        icon = R.drawable.m3_power_settings,
        type = SettingType.SHUTDOWN
    ),
)

/**
 * App 配置页 - 所有二级配置页的入口
 */
@Composable
fun SettingList(
    settings: Array<Setting>,
    onSettingClick: ((setting: Setting) -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(settings) { setting ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 27.dp)
                        .clickable {
                            onSettingClick?.invoke(setting)
                        }
                ) {
                    Text(
                        text = setting.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                        painter = painterResource(setting.icon),
                        contentDescription = setting.name
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xffDFE6E9)).alpha(0.4f).padding(horizontal = 18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingListPreview() {
    CapnoEasyTheme {
        SettingList(settings = settings)
    }
}