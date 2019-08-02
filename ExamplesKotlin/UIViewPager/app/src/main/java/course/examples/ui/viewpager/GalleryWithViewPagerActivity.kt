// This project uses the v13 support library.
// See http://developer.android.com/tools/support-library/setup.html for more information


package course.examples.ui.viewpager

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager

class GalleryWithViewPagerActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val viewPager = findViewById<ViewPager>(R.id.pager)

        // Create a new ImageAdapter (subclass of FragmentStatePagerAdapter)
        val imageAdapter = ImageAdapter(getImageIds(), supportFragmentManager)

        // Set the Adapter on the ViewPager
        viewPager.adapter = imageAdapter

    }

    private fun getImageIds(): IntArray {

       val resIds = resources.obtainTypedArray(R.array.image_ids)

        val ids = IntArray(resIds.length())
        for (i in ids.indices) {
            ids[i] = resIds.getResourceId(i, 0)
        }

        resIds.recycle()

        return ids
    }
}
