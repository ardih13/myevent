package com.test.myevent.data.room

import android.content.Context
import com.test.myevent.data.model.EventModel

object CallRoomEvent {

    private fun initRoomDB(ctx: Context): OfflineDatabase {
        return OfflineDatabase.getDatabase(ctx)
    }

    private fun callEventDao(ctx: Context): EvenDao {
        val dao = initRoomDB(ctx).getEventDao()
        return dao
    }

    private fun convertFromInsertEvent(event: EventModel): EventEntity {
        return EventEntity(
            0,
            event.name,
            event.location,
            event.start_date,
            event.end_date,
            event.start_time,
            event.end_time,
            event.image,
            event.description,
            event.organizer
        )
    }

    fun insertEvent(ctx: Context, listEvent: EventModel) {
        val event = convertFromInsertEvent(listEvent)
        callEventDao(ctx).insert(event)
    }


    private fun convertFromInsertEventResponseModel(eventEntity: EventEntity): EventModel {
        val eventItem = EventModel()
        eventItem.id = eventEntity.id
        eventItem.name = eventEntity.name
        eventItem.location = eventEntity.location
        eventItem.start_date = eventEntity.start_date
        eventItem.end_date = eventEntity.end_date
        eventItem.start_time = eventEntity.start_time
        eventItem.end_time = eventEntity.end_time
        eventItem.image = eventEntity.image
        eventItem.description = eventEntity.description
        eventItem.organizer = eventEntity.organizer

        return eventItem
    }


    fun getAllEvent(ctx: Context): MutableList<EventModel> {
        val eventList = callEventDao(ctx).getAllEvent()
        val eventItemList = mutableListOf<EventModel>()

        for (i in 0 until eventList.size) {
            eventItemList.add(
                convertFromInsertEventResponseModel(
                    eventList[i]
                )
            )
        }
        return eventItemList
    }

    fun getTodayEvent(ctx: Context, query: String): MutableList<EventModel> {
        val registrationList = callEventDao(ctx).getTodayEvent(query)
        val registrationItemList = mutableListOf<EventModel>()

        for (i in 0 until registrationList.size) {
            registrationItemList.add(
                convertFromInsertEventResponseModel(
                    registrationList[i]
                )
            )
        }
        return registrationItemList
    }

}