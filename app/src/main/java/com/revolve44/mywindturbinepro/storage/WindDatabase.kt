package com.revolve44.mywindturbinepro.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.revolve44.mywindturbinepro.models.ForecastPer3hr


@Database(
    entities = [
        ForecastPer3hr::class
    ],
    version = 1
)
abstract class WindDatabase : RoomDatabase() {
    abstract val windDao : WindDao

    companion object{
        @Volatile
        private var INSTANCE :WindDatabase? = null

        fun getInstance(context: Context): WindDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WindDatabase::class.java,
                    "mywindturbine_db"
                ).build().also { INSTANCE = it}
            }
        }
    }
}