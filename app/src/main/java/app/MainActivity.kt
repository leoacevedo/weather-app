package app

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import app.cities.history.ui.CityHistoryAdapter
import app.cities.history.ui.ParcelizableForecast
import app.cities.history.ui.WeatherAdapter
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
    private lateinit var cityAdapter: CityHistoryAdapter
    private lateinit var weatherAdapter: WeatherAdapter

    private var metricUnits = true
    private var forecast: ParcelizableForecast? = null

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

        weatherAdapter = WeatherAdapter(this)
        weatherVariables.adapter = weatherAdapter

        savedInstanceState?.let {
            metricUnits = it.getBoolean(METRIC_UNITS)
            forecast = it.getParcelable(FORECAST)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(METRIC_UNITS, metricUnits)
        forecast?.let {
            outState.putParcelable(FORECAST, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        menu.findItem(R.id.app_bar_search).actionView.apply {
            this as SearchView

            queryHint = getString(R.string.search_hint)

            autoCompleteTextView = this.findViewById(R.id.search_src_text)
            if (::cityAdapter.isInitialized && autoCompleteTextView.adapter != cityAdapter) {
                autoCompleteTextView.setAdapter(cityAdapter)
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
                    val query = cityAdapter.data[position]
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.units) {
            metricUnits = !metricUnits
            item.title = when (metricUnits) {
                false -> getString(R.string.units_metric)
                true -> getString(R.string.units_imperial)
            }
            forecast?.let { render(it) }
        }
        return super.onOptionsItemSelected(item)
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
        val parcelizableForecast = ParcelizableForecast(forecast)
        this.forecast = parcelizableForecast
        render(parcelizableForecast)
    }

    private fun render(forecast: ParcelizableForecast) {
        runOnUiThread {
            val titleTemperature = getString(R.string.temperature)
            val titleMinTemperature = getString(R.string.min_temperature)
            val titleMaxTemperature = getString(R.string.max_temperature)
            val titleHumidity = getString(R.string.humidity)
            val titlePressure = getString(R.string.pressure)

            with(forecast) {
                val valueTemperature =
                    getString(getTemperatureResId(), temperatureCelsius.toTemperatureUnit())
                val valueMinTemperature =
                    getString(getTemperatureResId(), minTemperatureCelsius.toTemperatureUnit())
                val valueMaxTemperature =
                    getString(getTemperatureResId(), maxTemperatureCelsius.toTemperatureUnit())
                val valueHumidity = getString(R.string.percent, humidity)
                val valuePressure = getString(getPressureResId(), pressureHpa.toPressureUnit())

                weatherAdapter.data = listOf(
                    titleTemperature to valueTemperature,
                    titleMinTemperature to valueMinTemperature,
                    titleMaxTemperature to valueMaxTemperature,
                    titleHumidity to valueHumidity,
                    titlePressure to valuePressure
                )

                screenTitle.text = getString(R.string.forecast_for_city_of, cityName)
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

            val imeService = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imeService.hideSoftInputFromWindow(autoCompleteTextView.windowToken, 0)
        }
    }

    override fun render(error: Throwable) {
        this.forecast = null

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
            cityAdapter = CityHistoryAdapter(this, cities)
            cityAdapter.removeCityListener = ::removeCity
            if (::autoCompleteTextView.isInitialized) {
                autoCompleteTextView.setAdapter(cityAdapter)
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

    private fun getTemperatureResId() = if (metricUnits) R.string.celsius else R.string.fahrenheit

    private fun getPressureResId() = if (metricUnits) R.string.hectopascals else R.string.inHg

    private fun Double.toTemperatureUnit(): Double {
        return if (metricUnits) this else this * 9 / 5 + 32
    }

    private fun Double.toPressureUnit(): Double {
        return if (metricUnits) this else this * 0.029529988
    }

    companion object {
        const val METRIC_UNITS = "metric_units"
        const val FORECAST = "forecast"
    }
}
