package com.wldmedical.capnoeasy.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wldmedical.capnoeasy.ui.theme.CapnoEasyTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// TODO: 按钮无法切换点击 
data class SystemIno(
    val name: String,
    val value: String,
    val isRadio: Boolean = false,
    val radios: Array<LanguageTypes>? = null
)

enum class LanguageTypes(name: String) {
    CHINESE(name = "中文"),
    ENGLISH(name = "English")
}

val systeminfos = arrayOf(
    SystemIno(
        name = "语言",
        value = LanguageTypes.CHINESE.name,
        isRadio = true,
        radios = arrayOf(
            LanguageTypes.CHINESE,
            LanguageTypes.ENGLISH
        )
    ),
    SystemIno(name = "固件版本", value = "V1.0.0"),
    SystemIno(name = "硬件版本", value = "V1.0.1"),
    SystemIno(name = "软件版本", value = "V1.0.2"),
    SystemIno(name = "生产日期", value = "2024年05月13日17:56:47"),
    SystemIno(name = "序列号", value = "FKUXP72K0P094"),
    SystemIno(name = "模块名称", value = "CapnoGraph"),
)

/**
 * App 配置页 - 系统配置页，配置列表
 */
@Composable
fun SystemInoList(
    systeminfos: Array<SystemIno>,
) {
    val infoHeight = 60.dp
    val selectedLanguage = remember { mutableStateOf(LanguageTypes.CHINESE) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(systeminfos) { systeminfo ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(infoHeight)
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 27.dp)
                ) {
                    Text(
                        text = systeminfo.name,
                        fontSize = 17.sp,
                    )
                    if(systeminfo.isRadio) {
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier =  Modifier.selectableGroup()
                        ) {
                            for (radio in systeminfo.radios!!) {
                                Row(
                                    Modifier
                                        .height(56.dp)
                                        .selectable(
                                            selected = selectedLanguage.value == radio,
                                            onClick = { },
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedLanguage.value == radio,
                                        onClick = {  }
                                    )
                                    Text(text = radio.name)
                                }
                            }
                        }
                    } else {
                        Text(
                            text = systeminfo.value,
                            fontSize = 17.sp,
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color(0xffDFE6E9))
                        .alpha(0.4f)
                        .padding(horizontal = 18.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SystemInoListPreview() {
    CapnoEasyTheme {
        SystemInoList(systeminfos = systeminfos)
    }
}