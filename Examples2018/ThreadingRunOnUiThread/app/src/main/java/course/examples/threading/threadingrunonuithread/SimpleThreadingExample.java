package course.examples.threading.threadingrunonuithread;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SimpleThreadingExample extends Activity {
    private ImageView mImageView;
    private final int mDelay = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mImageView = findViewById(R.id.imageView);

    }

    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(SimpleThreadingExample.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }

    public void onClickLoadButton(@SuppressWarnings("unused") View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SimpleThreadingExample.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                                R.drawable.painter));
                    }
                });
            }
        }).start();
    }
}