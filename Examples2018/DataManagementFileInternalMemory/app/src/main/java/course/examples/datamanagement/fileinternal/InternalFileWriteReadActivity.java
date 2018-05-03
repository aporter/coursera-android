package course.examples.datamanagement.fileinternal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class InternalFileWriteReadActivity extends Activity {

	private final static String fileName = "TestFile.txt";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		TextView textView = findViewById(R.id.textview);

		String TAG = "IntFileWriteRead";

		// Check whether fileName already exists in directory used
		// by the openFileOutput() method.
		// If the text file doesn't exist, then create it now


		if (!getFileStreamPath(fileName).exists()) {
			try {
				writeFile();
			} catch (FileNotFoundException e) {
				Log.i(TAG, "FileNotFoundException");
			}
		}

		
		// Read the data from the text file and display it
		try {
			
			readFileAndDisplay(textView);

		} catch (IOException e) {
			Log.i(TAG, "IOException");
		}
	}

	private void writeFile() throws FileNotFoundException {

		FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);

		PrintWriter pw = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(fos)));

		pw.println("Line 1: This is a test of the File Writing API");
		pw.println("Line 2: This is a test of the File Writing API");
		pw.println("Line 3: This is a test of the File Writing API");

		pw.close();

	}

	private void readFileAndDisplay(TextView tv) throws IOException {

		FileInputStream fis = openFileInput(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line;
		String sep = System.getProperty("line.separator");

		while (null != (line = br.readLine())) {
			tv.append(line + sep);
		}

		br.close();

	}

}