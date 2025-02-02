package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.LanguageTypes
import com.wldmedical.capnoeasy.PageScene
import com.wldmedical.capnoeasy.SystemIno

/***
 * 设置二级页 - 系统
 */
class SystemSettingActivity : BaseActivity() {
    override val pageScene = PageScene.SYSTEM_CONFIG_PAGE

    val infoHeight = 60.dp

    fun updateLanguage(newLanguage: LanguageTypes) {
        viewModel.updateLanguage(newLanguage)
    }

    @Composable
    override fun Content() {
        val systeminfos = arrayOf(
            SystemIno(
                name = "语言",
                value = viewModel.language.value.cname,
                isRadio = true,
                radios = arrayOf(
                    LanguageTypes.CHINESE,
                    LanguageTypes.ENGLISH
                )
            ),
            SystemIno(name = "固件版本", value = viewModel.firmVersion.value),
            SystemIno(name = "硬件版本", value = viewModel.hardwareVersion.value),
            SystemIno(name = "软件版本", value = viewModel.softwareVersion.value),
            SystemIno(name = "生产日期", value = viewModel.productDate.value),
            SystemIno(name = "序列号", value = viewModel.serialNumber.value),
            SystemIno(name = "模块名称", value = viewModel.moduleName.value),
        )

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
                                    val isSelected = viewModel.language.value == radio
                                    Row(
                                        Modifier
                                            .height(56.dp)
                                            .selectable(
                                                selected =  isSelected,
                                                onClick = {
                                                    updateLanguage(radio)
                                                },
                                                role = Role.RadioButton
                                            )
                                            .padding(start = 16.dp),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                updateLanguage(radio)
                                            }
                                        )
                                        Text(text = radio.cname)
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
}