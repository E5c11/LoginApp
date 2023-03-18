package com.test.loginapp.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.test.loginapp.data.preferences.UserPreferences
import com.test.loginapp.data.repos.LoginRepo
import com.test.loginapp.data.repos.implimentations.LoginRepoImpl
import com.test.loginapp.domain.usecases.LoginUsecase
import com.test.loginapp.util.NetworkTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun providesNetworkState(@ApplicationContext context: Context): NetworkTracker = NetworkTracker(context)

}