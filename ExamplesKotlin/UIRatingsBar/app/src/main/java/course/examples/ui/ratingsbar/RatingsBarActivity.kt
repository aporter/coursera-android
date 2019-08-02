package course.examples.ui.ratingsbar

import android.app.Activity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView


class RatingsBarActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val tv = findViewById<TextView>(R.id.textView)

       findViewById<RatingBar>(R.id.ratingbar)?.onRatingBarChangeListener =
            OnRatingBarChangeListener { _, rating, _ ->
                // Called when the user swipes the RatingBar
                tv.text = getString(R.string.rating_string, rating)
            }
    }
}