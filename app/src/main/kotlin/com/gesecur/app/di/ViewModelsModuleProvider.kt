package com.gesecur.app.di

import com.gesecur.app.ui.auth.AuthViewModel
import com.gesecur.app.ui.incidences.IncidencesViewModel
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.profile.PersonalViewModel
import com.gesecur.app.ui.splash.SplashViewModel
import com.gesecur.app.ui.vigilant.VigilantViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelsModuleProvider : ModuleProvider {

    override fun getModule(isMocked: Boolean): Module {
        return module {

            viewModel {
                SplashViewModel(
                    userRepository = get()
                )
            }

            viewModel {
                AuthViewModel(
                    userRepository = get()
                )
            }

            viewModel {
                OperatorViewModel(
                     gesecurRepository = get(),
                     userRepository = get()
                )
            }

            viewModel {
                IncidencesViewModel(
                        incidenceRepository = get(),
                        userRepository = get()
                )
            }

            viewModel {
                VigilantViewModel(
                    userRepository = get(),
                    vigilantRepository = get()
                )
            }

            viewModel {
                PersonalViewModel(
                    userRepository = get(),
                    personalRepository = get()
                )
            }
        }
    }
}
