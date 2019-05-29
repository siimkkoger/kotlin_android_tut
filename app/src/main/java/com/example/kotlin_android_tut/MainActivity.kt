package com.example.kotlin_android_tut

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val downloadData by lazy { DownloadDataAsyncTask(this, xmlListView) }
    private var rssUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/limit=200/xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        downloadData.execute(rssUrl)
        Log.d(TAG, "onCreate finish")
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private class DownloadDataAsyncTask(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadDataAsyncTask"

            var propContext: Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                propListView.adapter =
                    FeedAdapter(propContext, R.layout.list_record, ParseApplication().parse(result))
            }

            override fun doInBackground(vararg params: String?): String? {
                Log.d(TAG, "doInBackground: starts with ${params[0]}")
                val rssFeed = URL(params[0]).readText()
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: rssFeed is empty.")
                }
                return rssFeed
            }
        }
    }
}
