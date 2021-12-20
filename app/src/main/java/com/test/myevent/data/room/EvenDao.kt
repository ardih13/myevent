package com.test.myevent.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EvenDao {

    @Insert
    fun insert(eventEntity: EventEntity)

    @Query("SELECT * FROM `event` ORDER BY id DESC")
    fun getAllEvent(): MutableList<EventEntity>

    @Query("SELECT * FROM `event` WHERE start_date like '%' || :qry || '%' ORDER BY id DESC")
    fun getTodayEvent(qry: String): MutableList<EventEntity>

}