package com.smarttechprogramming.veloxkit

import android.app.Application
import androidx.room.Room
import com.smarttechprogramming.veloxkit.data.database.AppDatabase
import com.smarttechprogramming.veloxkit.data.repository.AppRepository
import com.smarttechprogramming.veloxkit.data.repository.PreferenceRepository

class VeloxkitApplication : Application() {
    lateinit var database: AppDatabase
        private set

    lateinit var appRepository: AppRepository
        private set

    lateinit var preferenceRepository: PreferenceRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "veloxkit_database"
        ).fallbackToDestructiveMigration().build()

        appRepository = AppRepository(
            snippetDao = database.snippetDao(),
            chatDao = database.chatDao()
        )

        preferenceRepository = PreferenceRepository(applicationContext)
    }
}
