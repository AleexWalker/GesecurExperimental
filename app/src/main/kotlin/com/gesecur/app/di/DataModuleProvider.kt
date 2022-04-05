package com.gesecur.app.di


import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.gesecur.app.domain.repositories.gesecur.GesecurRepository
import com.gesecur.app.domain.repositories.gesecur.GesecurRepositoryImpl
import com.gesecur.app.domain.repositories.personal.PersonalRepositoryImpl
import com.gesecur.app.domain.repositories.gesecur.VigilantRepositoryImpl
import com.gesecur.app.domain.repositories.incidence.IncidenceRepository
import com.gesecur.app.domain.repositories.incidence.IncidenceRepositoryImpl
import com.gesecur.app.domain.repositories.personal.PersonalRepository
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.domain.repositories.user.UserRepositoryImpl
import com.gesecur.app.domain.repositories.vigilant.VigilantRepository
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DataModuleProvider : ModuleProvider {

    override fun getModule(isMocked: Boolean): Module {
        return module {

            /**
             * Encrypted shared preferences
             */
            single {
                val builder = MasterKey.Builder(androidContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)

                EncryptedSharedPreferences.create(
                    androidContext(),
                    "Gesecur",
                    builder.build(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            }

            single {
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    isLenient = true
                }
            }

            single<UserRepository> {
                UserRepositoryImpl(
                    service = get(),
                    preferences = get()
                )
            }

            single<GesecurRepository> {
                GesecurRepositoryImpl(
                        service = get()
                )
            }

            single<IncidenceRepository> {
                IncidenceRepositoryImpl(
                    service = get()
                )
            }

            single<VigilantRepository> {
                VigilantRepositoryImpl(
                    service = get()
                )
            }

            single<PersonalRepository> {
                PersonalRepositoryImpl(
                    service = get()
                )
            }
        }
    }
}

