package com.gesecur.app.ui.common.arch

import com.gesecur.app.domain.models.GesecurError

open class BaseAction {
    class ShowError(val error: GesecurError) : BaseAction()
    class ShowErrorRes(val error: Int) : BaseAction()
}