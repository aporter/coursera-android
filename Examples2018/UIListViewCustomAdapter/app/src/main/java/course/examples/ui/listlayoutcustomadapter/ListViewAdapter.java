package course.examples.ui.listlayoutcustomadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class ListViewAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mLayoutInflater;

    public ListViewAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View dataView = convertView;

        // Check for recycled View
        if (null == dataView) {

            // Not recycled. Create the View
            dataView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

            // Cache View information in ViewHolder Object
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = dataView.findViewById(R.id.text);
            dataView.setTag(viewHolder);

        }

        // Set the View's data

        // Retrieve the viewHolder Object
        ViewHolder storedViewHolder = (ViewHolder) dataView.getTag();

        //Set the data in the data View
        storedViewHolder.text.setText(getItem(position));

        return dataView;
    }

    // The ViewHolder class. See:
    // http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        public TextView text;
    }
}
