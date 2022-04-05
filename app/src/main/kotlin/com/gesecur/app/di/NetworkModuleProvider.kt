package com.gesecur.app.di


import com.gesecur.app.data.gesecur.GesecurNetworkProvider
import com.gesecur.app.BuildConfig
import com.gesecur.app.data.gesecur.GesecurService
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

object NetworkModuleProvider : ModuleProvider {

    override fun getModule(isMocked: Boolean): Module {
        return module {

            /**
             * OkHttp client
             */
            single {
                GesecurNetworkProvider.getOkHttpClient()
            }

            /**
             * Retrofit
             */
            single { (baseUrl: String) ->
                GesecurNetworkProvider.getRetrofit(
                    baseUrl = baseUrl,
                    okHttpClient = get(),
                    json = get()
                )
            }

            /**
             * Retrofit service
             */
            single {
                val retrofit: Retrofit = get { parametersOf(BuildConfig.API_URL) }
                retrofit.create(GesecurService::class.java)
            }
        }
    }
}