package app

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import app.cities.history.ui.CityHistoryAdapter
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
    private lateinit var searchView: SearchView
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapter: CityHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)

        val progressDrawable = loadingIndicator.indeterminateDrawable.mutate()
        progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        loadingIndicator.indeterminateDrawable = progressDrawable
        mapFragment = map as SupportMapFragment

        mapFragment.getMapAsync {
            with(it.uiSettings) {
                this.setAllGesturesEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        menu.findItem(R.id.app_bar_search).actionView.apply {
            this as SearchView

            queryHint = getString(R.string.search_hint)

            autoCompleteTextView = this.findViewById(R.id.search_src_text)
            if (::adapter.isInitialized && autoCompleteTextView.adapter != adapter) {
                autoCompleteTextView.setAdapter(adapter)
            }
            autoCompleteTextView.threshold = 1

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?) = true

                override fun onQueryTextSubmit(query: String): Boolean {
                    autoCompleteTextView.dismissDropDown()
                    searchPreviousResults(query)
                    return true
                }
            })

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int) = false

                override fun onSuggestionClick(position: Int): Boolean {
                    val query = adapter.data[position]
                    autoCompleteTextView.setText(query)
                    autoCompleteTextView.dismissDropDown()
                    searchPreviousResults(query)
                    return true
                }
            })

            autoCompleteTextView.setOnClickListener {
                autoCompleteTextView.showDropDown()
            }

            searchView = this
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        presenter.onShowing(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onHiding()
        snackbar?.dismiss()
    }

    private fun searchPreviousResults(queryString: String) {
        presenter.getForecast(queryString)
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
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 5.5f))
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

    override fun updateCitySuggestions(cities: List<String>) {
        runOnUiThread {
            adapter = CityHistoryAdapter(this, cities)
            adapter.removeCityListener = ::removeCity
            if (::autoCompleteTextView.isInitialized) {
                autoCompleteTextView.setAdapter(adapter)
            }
        }
    }

    private fun removeCity(city: String) {
        runOnUiThread { autoCompleteTextView.dismissDropDown() }

        presenter.removeCityFromHistory(city)
    }

    private fun View.show() {
        runOnUiThread { visibility = VISIBLE }
    }

    private fun View.hide() {
        runOnUiThread { visibility = GONE }
    }
}
