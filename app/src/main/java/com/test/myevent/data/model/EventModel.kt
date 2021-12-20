package com.test.myevent.data.model

import java.io.Serializable

class EventModel : Serializable {
    var id: Int = 0
    var name: String? = null
    var location: String? = null
    var start_date: String? = null
    var end_date: String? = null
    var start_time: String? = null
    var end_time: String? = null
    var image: String? = null
    var description: String? = null
    var organizer: String? = null
}