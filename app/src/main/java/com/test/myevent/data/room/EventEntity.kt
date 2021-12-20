package com.test.myevent.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "location") var location: String? = "",
    @ColumnInfo(name = "start_date") var start_date: String? = "",
    @ColumnInfo(name = "end_date") var end_date: String? = "",
    @ColumnInfo(name = "start_time") var start_time: String? = "",
    @ColumnInfo(name = "end_time") var end_time: String? = "",
    @ColumnInfo(name = "image") var image: String? = "",
    @ColumnInfo(name = "description") var description: String? = "",
    @ColumnInfo(name = "organizer") var organizer: String? = ""

) : Serializable