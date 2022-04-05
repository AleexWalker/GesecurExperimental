package com.gesecur.app.ui.common.toolbar

import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ToolbarOptions(
    val showToolbar: Boolean = true,
    val homeButton: GesecurToolbar.HomeButton = GesecurToolbar.HomeButton.NONE,
    @StringRes val titleRes: Int = ResourcesCompat.ID_NULL,
    val title: String = ""
)