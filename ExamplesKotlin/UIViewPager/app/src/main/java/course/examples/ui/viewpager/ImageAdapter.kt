package course.examples.ui.viewpager

import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager


// Manages Fragments holding ImageViews
internal class ImageAdapter(private val ids: IntArray, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        val fragment = ImageHolderFragment()
        val args = Bundle()
        args.putInt(ImageHolderFragment.KEY_RES_ID, ids[i])
        args.putString(ImageHolderFragment.KEY_POS, i.toString())
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return ids.size
    }

}