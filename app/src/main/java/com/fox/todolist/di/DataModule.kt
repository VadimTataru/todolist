package com.fox.todolist.di

import android.content.Context
import androidx.room.Room
import com.fox.todolist.data.source.local.NoteDao
import com.fox.todolist.data.source.local.NoteDatabase
import com.fox.todolist.utils.Constants.CHECK_TAG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideTestTag(): String {
        return CHECK_TAG
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao {
        return db.noteDao()
    }

}