package app.cities.history.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.R

class WeatherAdapter(private val context: Context) : RecyclerView.Adapter<WeatherEntryVH>() {

    val inflater by lazy { LayoutInflater.from(context) }

    var data: List<Pair<String, String>> = ArrayList()
        set(value) {
            data.apply {
                this as ArrayList<Pair<String, String>>
                clear()
                addAll(value)
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherEntryVH {
        val view = inflater.inflate(R.layout.layout_weather_entry, parent, false)
        return WeatherEntryVH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: WeatherEntryVH, position: Int) {
        val item = data[position]
        with(holder) {
            title.text = item.first
            value.text = item.second

            val backgroundColorResId = when (position % 2) {
                0 -> R.color.gray_list
                else -> R.color.white
            }

            itemView.setBackgroundColor(
                ContextCompat.getColor(context, backgroundColorResId)
            )
        }
    }
}

class WeatherEntryVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.findViewById<TextView>(R.id.title)
    val value = itemView.findViewById<TextView>(R.id.value)
}