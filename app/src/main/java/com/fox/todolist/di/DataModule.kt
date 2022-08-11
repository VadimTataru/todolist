package com.fox.todolist.di

import com.fox.todolist.utils.Constants.CHECK_TAG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DataModule {

    @Provides
    fun provideTestTag(): String {
        return CHECK_TAG
    }
}