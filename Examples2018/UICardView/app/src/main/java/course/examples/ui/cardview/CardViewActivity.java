package course.examples.ui.cardview;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class CardViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        ImageView imageView = findViewById(R.id.flag_image);
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), (R.drawable.us)));

        TextView place = findViewById(R.id.place_name);
        place.setText(R.string.location_string);

        TextView country = findViewById(R.id.country_name);
        country.setText(R.string.country_string);

        TextView date = findViewById(R.id.date_string);
        date.setText(R.string.visited_on_string);

        TextView location = findViewById(R.id.location);
        location.setText(R.string.location_coords_string);

    }
}
