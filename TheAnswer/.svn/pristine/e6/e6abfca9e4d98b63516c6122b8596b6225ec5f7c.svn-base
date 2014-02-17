package course.examples.theanswer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TheAnswer extends Activity {

	public static final int[] answers = { 42, -10, 0, 100, 1000 };
	public static final int answer = 42;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.answer_layout);

		TextView answerView = (TextView) findViewById(R.id.answer_view);

		int val = findAnswer();
		String output = (val == answer) ? "42" : "We may never know";
		answerView
				.setText("The answer to life, the universe and everything is:\n\n"
						+ output);
	}

	private int findAnswer() {
		for (int val : answers) {
			if (val == answer)
				return val;
		}
		return -1;
	}
}
