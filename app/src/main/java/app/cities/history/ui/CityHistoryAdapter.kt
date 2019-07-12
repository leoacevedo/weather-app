package app.cities.history.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import app.R

class CityHistoryAdapter(
    context: Context,
    val data: List<String>
) : ArrayAdapter<String>(context, R.layout.item_city_history, data), Filterable {

    var removeCityListener: ((String) -> Unit)? = null

    private val inflater by lazy { LayoutInflater.from(context) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = if (convertView == null) {
            inflater.inflate(R.layout.item_city_history, parent, false).apply {
                val cityTextView = findViewById<TextView>(android.R.id.text1)
                val removeBtn = findViewById<View>(R.id.remove)
                val viewHolder = ViewHolder(cityTextView, removeBtn)

                removeBtn.setOnClickListener {
                    removeCityListener?.invoke(cityTextView.text.toString())
                }
                tag = viewHolder
            }
        } else {
            convertView
        }

        val viewHolder = view.tag as ViewHolder
        val item = getItem(position)

        viewHolder.cityName.text = item
        return view
    }

    override fun getFilter(): Filter {
        return DummyFilter(data)
    }
}

class DummyFilter(val data: List<String>) : Filter() {
    override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        return FilterResults().apply {
            values = data
            count = data.size
        }
    }
}

data class ViewHolder(
    val cityName: TextView,
    val removeBtn: View
)