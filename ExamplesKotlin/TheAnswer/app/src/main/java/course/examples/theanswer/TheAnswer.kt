package course.examples.theanswer

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class TheAnswer : Activity() {

    companion object {

        private val answers = intArrayOf(42, -10, 0, 100, 1000)
        private const val answer = 42
        private const val TAG = "TheAnswer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Required call through to Activity.onCreate()
        // Restore any saved instance state
        super.onCreate(savedInstanceState)

        // Set up the application's user interface (content view)
        setContentView(R.layout.answer_layout)

        val value = findAnswer()
        val output = if (value != null) answer.toString() else getString(R.string.never_know_string)

        // Get a reference to a TextView in the content view
        val answerView = findViewById<TextView>(R.id.answer_view)

        // Set desired text in answerView TextView
        answerView.text = output
    }

    private fun findAnswer(): Int? {
        Log.d(TAG, "Entering findAnswer()")

        // Incorrect behavior
         return answers.firstOrNull { it == -answer }

        // Correct behavior
//         return answers.firstOrNull { it == answer }
    }
}
