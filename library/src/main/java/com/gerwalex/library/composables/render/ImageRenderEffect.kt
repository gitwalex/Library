package com.gerwalex.library.composables.render

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.gerwalex.library.R
import com.gerwalex.library.composables.AppTheme
import com.gerwalex.library.composables.animation.ImageFadeInOutAnimation
import com.gerwalex.library.composables.container.BlurContainer
import com.gerwalex.library.composables.container.ShaderContainer

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ImageRenderEffect(painter: Painter, modifier: Modifier = Modifier) {
    val blur = remember { Animatable(0f) }

    LaunchedEffect(painter) {
        blur.animateTo(100f, tween(easing = LinearEasing))
        blur.animateTo(0f, tween(easing = LinearEasing))
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ShaderContainer(
                modifier = Modifier
                    .animateContentSize()
                    .clipToBounds()
            ) {
                Blur(painter, blur)
            }
        } else {
            Blur(painter = painter, blur = blur)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Blur(
    painter: Painter,
    blur: Animatable<Float, AnimationVector1D>,
    modifier: Modifier = Modifier
) {
    BlurContainer(
        modifier = modifier,
        blur = blur.value,
        component = {
            ImageFadeInOutAnimation(painter, Modifier.fillMaxSize())
        }) {}
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ImageRenderPreview() {
    val resources = LocalContext.current.resources
    val bitmap = BitmapFactory.decodeResource(
        resources, R.drawable.demo_image
    )

    AppTheme {
        Surface {
            ImageRenderEffect(painter = BitmapPainter(bitmap.asImageBitmap()))
        }
    }

}
