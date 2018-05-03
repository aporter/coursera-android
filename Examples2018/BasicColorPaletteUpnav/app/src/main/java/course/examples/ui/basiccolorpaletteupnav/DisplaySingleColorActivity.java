package course.examples.ui.basiccolorpaletteupnav;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

public class DisplaySingleColorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_color_layout);

        findViewById(R.id.color_block)
                .setBackgroundColor(getIntent().getIntExtra(
                        PaletteAdapter.COLOR_EXTRA, PaletteAdapter.NO_COLOR));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Toast toast = Toast.makeText(
                        this, getString(R.string.shortcut_string), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // Navigate up to parent Activity and finish this
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
