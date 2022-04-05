package com.gesecur.app.di

import org.koin.core.module.Module

interface ModuleProvider {
    fun getModule(isMocked: Boolean = false): Module
}
