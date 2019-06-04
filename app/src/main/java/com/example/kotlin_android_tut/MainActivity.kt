package com.example.kotlin_android_tut

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var downloadData: DownloadDataAsyncTask? = null
    private var feedLimit = 10
    private val feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/%s"
    private val moviesUrl = feedUrl.format("topMovies/limit=%d/xml")
    private val tvSeasonsUrl = feedUrl.format("topTvSeasons/limit=%d/xml")
    private val tvEpisodesUrl = feedUrl.format("topTvEpisodes/limit=%d/xml")
    private var currentUrl = moviesUrl

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        downloadFeed(moviesUrl, 10)
        Log.d(TAG, "onCreate finish")
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData?.cancel(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu.findItem(R.id.menuLimitTop10)?.isChecked = true
        } else {
            menu.findItem(R.id.menuLimitTop25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuMovies -> currentUrl = moviesUrl
            R.id.menuTvSeasons -> currentUrl = tvSeasonsUrl
            R.id.menuTvEpisodes -> currentUrl = tvEpisodesUrl
            R.id.menuLimitTop10 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 10
                }
            }
            R.id.menuLimitTop25 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 25
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        downloadFeed(currentUrl, feedLimit)
        return true
    }

    private fun downloadFeed(feedUrl: String, limit: Int) {
        currentUrl = feedUrl
        downloadData = DownloadDataAsyncTask(this, xmlListView)
        downloadData?.execute(feedUrl.format(limit))
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
