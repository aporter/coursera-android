package course.examples.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final List<String> mNames;
    private final int mRowLayout;

    public MyRecyclerViewAdapter(List<String> names, int rowLayout) {
        mNames = names;
        mRowLayout = rowLayout;
    }

    // Create ViewHolder which holds a View to be displayed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new MyRecyclerViewAdapter.ViewHolder(v);
    }

    // Binding: The process of preparing a child view to display data corresponding to a position within the adapter.
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mName.setText(mNames.get(i));
    }

    @Override
    public int getItemCount() {
        return (null == mNames) ? 0 : mNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Display a Toast message indicting the selected item
            Toast.makeText(view.getContext(),
                    mName.getText(), Toast.LENGTH_SHORT).show();
        }
    }

}

