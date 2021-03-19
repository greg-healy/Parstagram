package com.codepath.greghealy.parstagram

import android.app.Application
import com.parse.Parse
import com.parse.ParseObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ParseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)

        val builder = OkHttpClient.Builder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.networkInterceptors().add(httpLoggingInterceptor)

        // Register parse models
        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId("h2mGQMZqzySO6wnaWi9qOgcJIyI1OJ6ZQlYK19YQ")
            .clientKey("MnjS7NRqsI2xyKWWiyz8PbKuCt49V1OW8fLDC0RB")
            .server("https://parseapi.back4app.com")
            .build()
        )

//        val testObject: ParseObject = ParseObject("TestObject")
//        testObject.put("foo", "bar")
//        testObject.saveInBackground()
    }
}