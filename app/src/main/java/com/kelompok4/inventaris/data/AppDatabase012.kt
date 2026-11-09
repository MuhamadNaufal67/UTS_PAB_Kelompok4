// Bagian NIM 102042330012 – Muhammad Syuja Al Abid (Room Database & Dashboard)
package com.kelompok4.inventaris.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User020::class, Item012::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase012 : RoomDatabase() {

    abstract fun userDao020(): UserDao020
    abstract fun itemDao012(): ItemDao012

    companion object {
        private const val DB_NAME_012 = "db_inventaris_012"

        @Volatile
        private var instance012: AppDatabase012? = null

        // Singleton: hanya satu instance database untuk seluruh aplikasi
        fun getInstance012(context012: Context): AppDatabase012 {
            return instance012 ?: synchronized(this) {
                instance012 ?: Room.databaseBuilder(
                    context012.applicationContext,
                    AppDatabase012::class.java,
                    DB_NAME_012
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance012 = it }
            }
        }
    }
}
