package app

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.leolei.mvp.WeatherPresenter
import com.leolei.mvp.WeatherView
import com.leolei.weather.model.Forecast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), WeatherView {
    @Inject
    lateinit var presenter: WeatherPresenter

    private var snackbar: Snackbar? = null
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        val progressDrawable = loadingIndicator.indeterminateDrawable.mutate()
        progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        loadingIndicator.indeterminateDrawable = progressDrawable
        mapFragment = map as SupportMapFragment

        mapFragment.getMapAsync { with(it.uiSettings) {
            this.setAllGesturesEnabled(false)
        }}
    }

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
        presenter.getForecast("Montevideo")
    }

    override fun onStop() {
        super.onStop()
        presenter.onHiding()
        snackbar?.dismiss()
    }

    private fun searchPreviousResults(queryString: String) {

    }

    override fun showLoading() {
        runOnUiThread { progressOverlay.show() }
    }

    override fun hideLoading() {
        runOnUiThread { progressOverlay.hide() }
    }

    override fun render(forecast: Forecast) {
        runOnUiThread {
            with(forecast) {
                degrees.text = getString(R.string.celsius, temperatureCelsius.toString())
                city.text = cityName
                mapFragment.getMapAsync { map ->
                    map.clear()
                    val latLng = LatLng(latitude, longitude)
                    val marker = MarkerOptions()
                        .anchor(0.5f, 1f)
                        .title(cityName)
                        .position(latLng)
                    map.addMarker(marker)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 4f))
                }
            }
        }
    }

    override fun render(error: Throwable) {
        runOnUiThread {
            val errorMessage = error.message ?: getString(R.string.unknown_error)
            Snackbar.make(root, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dismiss) {}
                .let {
                    snackbar = it
                    it.show()
                }
        }
    }

    private fun View.show() {
        runOnUiThread { visibility = VISIBLE }
    }

    private fun View.hide() {
        runOnUiThread { visibility = GONE }
    }
}
