package com.joshgm3z.downloader.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joshgm3z.downloader.model.room.dao.DownloadTaskDao
import com.joshgm3z.downloader.model.room.data.DownloadTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [DownloadTask::class], version = 1, exportSchema = false)
abstract class RoomDb : RoomDatabase() {
    abstract fun downloadTaskDao(): DownloadTaskDao
}

@Module
@InstallIn(SingletonComponent::class)
class RoomProvider {
    @Provides
    fun provideDb(@ApplicationContext context: Context): RoomDb = Room.databaseBuilder(
        context,
        RoomDb::class.java, "download-db"
    ).build()

    @Provides
    fun provideDownloadTaskDao(roomDb: RoomDb) = roomDb.downloadTaskDao()
}

