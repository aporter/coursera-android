package course.examples.datamanagement.fileexternal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class ExternalFileWriteReadActivity extends Activity {
	private final String fileName = "painter.png";
	private String TAG = "ExternalFileWriteReadActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			File outFile = new File(
					getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					fileName);
			
			if (!outFile.exists())
				copyImageToMemory(outFile);
			
			ImageView imageview = (ImageView) findViewById(R.id.image);
			imageview.setImageURI(Uri.parse("file://" + outFile.getAbsolutePath()));
		
		}
	}

	private void copyImageToMemory(File outFile) {
		try {

			BufferedOutputStream os = new BufferedOutputStream(
					new FileOutputStream(outFile));

			BufferedInputStream is = new BufferedInputStream(getResources()
					.openRawResource(R.raw.painter));

			copy(is, os);

		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException");
		}
	}

	private void copy(InputStream is, OutputStream os) {
		final byte[] buf = new byte[1024];
		int numBytes;
		try {
			while (-1 != (numBytes = is.read(buf))) {
				os.write(buf, 0, numBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				Log.e(TAG, "IOException");

			}
		}
	}
}