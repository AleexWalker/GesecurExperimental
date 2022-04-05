package com.gesecur.app.ui.common.bottombar

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class BottomBarOptions(
    val showBottomBar: Boolean = true
)