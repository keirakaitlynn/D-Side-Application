package com.example.dsideapp.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import java.util.ArrayList

/// -----------
class CategoryAdapter(var categoriesList: MutableList<String>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), Filterable {
    var categoriesListAll: List<String> = ArrayList(categoriesList)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.row_item, parent, false)
        var tv: Button = view.findViewById(R.id.textView)
        tv.setOnClickListener{
            Log.w("","GOT IT WORKING")
            //Toast.makeText(view.context, categoriesList[adapterPosition], Toast.LENGTH_SHORT).show()
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = categoriesList[position]
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    private var filter = object : Filter() {
        //run on background thread
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<String> = ArrayList()
            if (charSequence.toString().isEmpty()) {
                filteredList.addAll(categoriesListAll)
            } else {
                for (category in categoriesListAll) {
                    if (category.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(category)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        //run on a ui thread
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            categoriesList.clear()
            categoriesList.addAll((filterResults.values as Collection<String>))
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.textView)
        override fun onClick(view: View) {
            Toast.makeText(view.context, categoriesList[adapterPosition], Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        private const val TAG = "RecyclerAdapter"
    }

}