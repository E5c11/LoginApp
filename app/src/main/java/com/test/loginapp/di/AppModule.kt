package com.test.loginapp.di

import com.google.firebase.auth.FirebaseAuth
import com.test.loginapp.data.preferences.UserPreferences
import com.test.loginapp.data.repos.LoginRepo
import com.test.loginapp.data.repos.implimentations.LoginRepoImpl
import com.test.loginapp.domain.usecases.LoginUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFbAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideLoginRepo(fbAuth: FirebaseAuth): LoginRepo = LoginRepoImpl(fbAuth)

    @Provides
    fun provideLoginUsecase(userPref: UserPreferences, loginRepo: LoginRepo): LoginUsecase =
        LoginUsecase(userPref, loginRepo)

}