package course.examples.helloandroidwithmenu

import android.app.Activity
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

class HelloAndroidWithMenuActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Long presses on TextView invoke Context Menu
        registerForContextMenu(findViewById<TextView>(R.id.text_view))

    }

    // Create Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }

    // Process clicks on Options Menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help -> {
                showToast(R.string.helped_string)
                true
            }
            R.id.more_help -> {
                showToast(R.string.helped_more_string)
                true
            }
            R.id.even_more_help -> true
            else -> false
        }
    }

    // Create Context Menu
    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    // Process clicks on Context Menu Items
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.help_guide -> {
                showToast(R.string.context_menu_shown_string)
                true
            }
            else -> false
        }
    }

    private fun showToast(msg: Int) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}