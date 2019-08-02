package course.examples.ui.viewpager


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast

// Each instance holds one image to be displayed in the ViewPager
class ImageHolderFragment : Fragment() {

    companion object {

        const val KEY_RES_ID = "res_id"
        const val KEY_POS = "position"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val imageView = inflater.inflate(R.layout.page, container, false) as ImageView

        arguments?.let { args ->

            // Set the Image for the ImageView
            imageView.setImageResource(args.getInt(KEY_RES_ID))

            // Set an setOnItemClickListener on the Gallery
            imageView.setOnClickListener {
                // Display a Toast message indicate the selected item
                Toast.makeText(activity, args.getString(KEY_POS), Toast.LENGTH_SHORT).show()
            }

        }
        return imageView
    }
}