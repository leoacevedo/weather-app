package app

import androidx.appcompat.app.AppCompatActivity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.leolei.mvp.WeatherPresenter
import com.leolei.mvp.WeatherView
import com.leolei.weather.model.Forecast
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), WeatherView {
    @Inject
    lateinit var presenter: WeatherPresenter

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
    override fun onStart() {
        super.onStart()
        presenter.onShowing(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onHiding()
    }

    private fun searchPreviousResults(queryString: String) {

    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun render(forecast: Forecast) {
    }

    override fun render(error: Throwable) {
    }

    }
}
