package com.example.kotlin_android_tut

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplication {
    private val TAG = "ParseApplication"
    val feedEntries = ArrayList<FeedEntry>()

    fun parse(xmlData: String): ArrayList<FeedEntry> {
        Log.d(TAG, "parse called with $xmlData")
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentEntry = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for $tagName")
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }

                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for $tagName")
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    feedEntries.add(currentEntry)
                                    inEntry = false
                                    currentEntry = FeedEntry()
                                }
                                "name" -> currentEntry.name = textValue
                                "artist" -> currentEntry.artist = textValue
                                "releasedate" -> currentEntry.releaseDate = textValue
                                "summary" -> currentEntry.summary = textValue
                                "image" -> currentEntry.imageURL = textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return feedEntries
    }
}