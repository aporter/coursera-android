package course.examples.ui.recyclerview

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import java.util.ArrayList
import java.util.Collections

class RecyclerViewActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.recycler_view)

        //Set up RecyclerView
        val mRecyclerView = findViewById<RecyclerView>(R.id.list)

        //Set the layout manager
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the adapter
        val names = ArrayList<String>()

        Collections.addAll(names, *resources.getStringArray(R.array.colors))

        val mAdapter = MyRecyclerViewAdapter(
            names,
            R.layout.list_item
        )
        mRecyclerView.adapter = mAdapter
    }
}