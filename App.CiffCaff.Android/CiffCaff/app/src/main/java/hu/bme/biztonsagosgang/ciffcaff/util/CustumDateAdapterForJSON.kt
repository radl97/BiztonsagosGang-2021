package hu.bme.biztonsagosgang.ciffcaff.util

import com.squareup.moshi.*
import java.text.SimpleDateFormat
import java.util.*

class CustomDateAdapter : JsonAdapter<Date>() {
    private val dateFormatter = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsString = reader.nextString()
            synchronized(dateFormatter) {
                dateFormatter.parse(dateAsString)
            }
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            synchronized(dateFormatter) {
                writer.value(dateFormatter.format(value))
            }
        }
    }

    companion object {
        const val SERVER_FORMAT = ("yyyy-MM-dd'T'HH:mm") // define your server format here
    }
}
