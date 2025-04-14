package com.ogeedeveloper.local_event_finder_frontend

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.layout.*
import com.skydoves.landscapist.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.geometry.Offset
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFFFFFFFF),
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF3C5AF6), Color(0xFF5972F7), ),
                        start = Offset.Zero,
                        end = Offset(0F,Float.POSITIVE_INFINITY),
                    )
                )
                .verticalScroll(rememberScrollState())
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 227.dp,)
                    .height(11.dp)
                    .fillMaxWidth()
                    .padding(vertical = 13.dp,horizontal = 21.dp,)
            ){
                Text("9:41",
                    color = Color(0xFFFFFFFF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 3.dp,)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                ){
                }
                GlideImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/xVQcCmsGFO/9qhaek2q_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .padding(end = 5.dp,)
                        .width(17.dp)
                        .height(10.dp)
                )
                GlideImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/xVQcCmsGFO/mvmj3o7n_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .padding(end = 5.dp,)
                        .width(15.dp)
                        .height(10.dp)
                )
                GlideImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/xVQcCmsGFO/oo8tup2p_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .width(24.dp)
                        .height(11.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 273.dp,start = 48.dp,end = 48.dp,)
                    .fillMaxWidth()
            ){
                GlideImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/xVQcCmsGFO/a0fecfde_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .padding(bottom = 24.dp,start = 40.dp,end = 40.dp,)
                        .height(200.dp)
                        .fillMaxWidth()
                )
                Text("App Name",
                    color = Color(0xFFFFFFFF),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}