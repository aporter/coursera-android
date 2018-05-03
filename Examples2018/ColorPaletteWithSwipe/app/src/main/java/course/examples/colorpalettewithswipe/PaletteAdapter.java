package course.examples.colorpalettewithswipe;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

class PaletteAdapter extends BaseAdapter {

    public static final String COLOR_EXTRA = "color";
    public static final int NO_COLOR = 0;
    private final Context mContext;
    private final int mRowLayout;
    private final TypedArray mColors;

    public PaletteAdapter(Context context, int rowLayout, int arrayResourceId) {
        mContext = context;
        mRowLayout = rowLayout;
        mColors = context.getResources().obtainTypedArray(arrayResourceId);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View newView = convertView;
        if (null == newView) {
            newView = LayoutInflater.from(mContext).inflate(mRowLayout, viewGroup, false);
        }

        final int backgroundColor = mColors.getColor(i, NO_COLOR);
        newView.setBackgroundColor(backgroundColor);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DisplaySingleColorActivity.class);
                intent.putExtra(COLOR_EXTRA, backgroundColor);
                mContext.startActivity(intent);
            }
        });

        return newView;
    }

    @Override
    public Object getItem(int i) {
        return mColors.getResourceId(i, -1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mColors.length();
    }
}
