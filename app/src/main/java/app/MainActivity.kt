package app

import androidx.appcompat.app.AppCompatActivity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val componentName = ComponentName(this, MainActivity::class.java)
        val searchService = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchableInfo = searchService.getSearchableInfo(componentName)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo(searchableInfo)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val queryString = intent.extras?.getString(SearchManager.QUERY)
        if (queryString != null) {
            searchPreviousResults(queryString)
        }

    }
    private fun searchPreviousResults(queryString: String) {

    }

    }
}
