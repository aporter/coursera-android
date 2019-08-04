package course.examples.ui.relativelayout

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

class RelativeLayoutActivity : Activity() {

    private lateinit var mTextEntry: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        mTextEntry = findViewById(R.id.entry)

    }

    fun cancelButtonClicked(view: View) {
        mTextEntry.setText("")
    }

    fun okButtonClicked(view: View) {
        hideKeyboard()
        if (mTextEntry.text.isNotEmpty()) {
            Toast.makeText(
                this@RelativeLayoutActivity,
                R.string.thanks_string,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun hideKeyboard() {

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}