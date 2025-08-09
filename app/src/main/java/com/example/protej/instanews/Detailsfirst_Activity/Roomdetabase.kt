package com.example.protej.instanews.Detailsfirst_Activity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.java





@Database(entities = [Article::class], version = 2)

abstract class Roomdetabase:RoomDatabase() {
    abstract fun StudentDao(): DAO_CLASS

    companion object{
        @Volatile

        private var INSTANCE:Roomdetabase?=null

        fun getDatabase(context: Context):Roomdetabase {

            return INSTANCE?: synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,Roomdetabase::class.java,
                    "Student").fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                instance

            }

        }

    }
}

