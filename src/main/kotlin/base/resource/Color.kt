package base.resource

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val BottomAppBarBgColor = Color(0xfff2f2f2)
val BottomAppBarTextColor = Color(0xff2e003d)
val BottomAppBarConnected= Color(0xff0061b2)
val BottomAppBarDisConnect= Color(0xffd81e06)
val BottomAppBarNoDoConnect= Color(0xffd4237a)
val BottomAppBarRightArrow= Color(0xff3f81c1)
val BottomAppBarTaskLog = Color(0xff333333)

val TaskLogBarBg = Color(0xffe2e6ec)

fun simpleAdbColors(
    primary: Color = Color.White,
    primaryVariant: Color = BottomAppBarBgColor,
    secondary: Color = BottomAppBarBgColor,
    secondaryVariant: Color = secondary,
    background: Color = BottomAppBarBgColor,
    surface: Color = Color.White,
    error: Color = BottomAppBarBgColor,
    onPrimary: Color = Color.White,
    onSecondary: Color = BottomAppBarBgColor,
    onBackground: Color = Color.White,
    onSurface: Color = Color.White,
    onError: Color = BottomAppBarNoDoConnect
): Colors = Colors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    false
)

