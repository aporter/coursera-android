package course.examples.colorpalettewithswipe;

import android.app.Activity;
import android.os.Bundle;

public class DisplaySingleColorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_color_layout);

        /**
         * Set the background to the selected color
         */
        findViewById(R.id.color_block).setBackgroundColor(
                getIntent().getIntExtra(PaletteAdapter.COLOR_EXTRA, PaletteAdapter.NO_COLOR));
    }
}
