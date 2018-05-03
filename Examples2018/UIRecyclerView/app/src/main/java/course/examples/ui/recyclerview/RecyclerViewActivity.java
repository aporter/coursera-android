package course.examples.ui.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view);

        //Set up RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.list);

        //Set the layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter
        ArrayList<String> names = new ArrayList<>();
        Collections.addAll(names, getResources().getStringArray(R.array.colors));

        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter(names,
                R.layout.list_item);
        mRecyclerView.setAdapter(mAdapter);
    }
}