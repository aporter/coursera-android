package course.examples.threading.nothreading;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class NoThreadingExample extends Activity {
    private ImageView mIView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mIView = findViewById(R.id.imageView);
    }

    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(NoThreadingExample.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }


    public void onClickLoadButton(@SuppressWarnings("unused") View view) {
        try {
            // Accentuates slow operation
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mIView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.painter));
    }
}