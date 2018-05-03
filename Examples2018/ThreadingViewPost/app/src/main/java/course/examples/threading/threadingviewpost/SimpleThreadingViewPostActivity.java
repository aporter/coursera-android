package course.examples.threading.threadingviewpost;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SimpleThreadingViewPostActivity extends Activity {
    private ImageView mImageView;
    private final int mDelay = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mImageView = findViewById(R.id.imageView);
    }

    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(SimpleThreadingViewPostActivity.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }

    public void onClickLoadButton(@SuppressWarnings("unused") final View view) {
        view.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mImageView.post(new Runnable() {
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