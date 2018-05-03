package course.examples.ui.colorpalettewithnavdrawer;

import android.app.Activity;
import android.os.Bundle;

public class DisplaySingleColorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_single_color_layout);

        /*
         * Set the background to the selected color
         */
        findViewById(R.id.color_block).setBackgroundColor(
                getIntent().getIntExtra(PaletteNameAdapter.COLOR_EXTRA, PaletteNameAdapter.NO_COLOR));
    }

}
