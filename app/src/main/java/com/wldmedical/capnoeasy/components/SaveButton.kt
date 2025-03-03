package com.wldmedical.capnoeasy.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wldmedical.capnoeasy.R
import com.wldmedical.capnoeasy.getString

/***
 * 保存按钮
 */
@Composable
fun SaveButton(
    onClick: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card (
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.clickable {
                onClick?.invoke()
            }
        ) {
            Text(
                text = getString(R.string.savebutton_save),
                letterSpacing = 5.sp,
                color = Color(0xff165DFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .background(Color(0xffE0EAFF))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
    )
}