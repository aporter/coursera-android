package course.examples.ui.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

internal class MyRecyclerViewAdapter(
    private val mNames: List<String>?,
    private val mRowLayout: Int
) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    // Create ViewHolder which holds a View to be displayed
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(mRowLayout, viewGroup, false)
        return ViewHolder(v)
    }

    // Binding: The process of preparing a child view to display data corresponding to a position within the adapter.
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mName.text = mNames!![i]
    }

    override fun getItemCount(): Int {
        return mNames?.size ?: 0
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal val mName: TextView = itemView.findViewById(R.id.text)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            // Display a Toast message indicting the selected item
            Toast.makeText(
                view.context,
                mName.text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

