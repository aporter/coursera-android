package course.examples.datamanagement.sql

import android.app.ListActivity
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.SimpleCursorAdapter

class DatabaseExampleActivity : ListActivity() {

    private lateinit var mDbHelper: DatabaseOpenHelper
    private lateinit var mAdapter: SimpleCursorAdapter
    private var mCursor: Cursor? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Create a new DatabaseHelper
        mDbHelper = DatabaseOpenHelper(this)

        // start with an empty database
        clearAll()

        // Insert records
        insertArtists()

        // Create a cursor
        mCursor = readArtists()
        mAdapter = SimpleCursorAdapter(
            this, R.layout.list_layout, mCursor,
            DatabaseOpenHelper.columns, intArrayOf(R.id._id, R.id.name),
            0
        )

        listAdapter = mAdapter

    }

    fun onClick(v: View) {

        // Execute database operations
        fix()

        // Redisplay data
        mCursor = readArtists()
        mAdapter.changeCursor(mCursor)
    }

    // Insert several artist records
    private fun insertArtists() {

        val values = ContentValues()

        values.put(DatabaseOpenHelper.ARTIST_NAME, "Frank Sinatra")
        mDbHelper.writableDatabase.insert(DatabaseOpenHelper.TABLE_NAME, null, values)

        values.clear()

        values.put(DatabaseOpenHelper.ARTIST_NAME, "Lady Gaga")
        mDbHelper.writableDatabase.insert(DatabaseOpenHelper.TABLE_NAME, null, values)

        values.clear()

        values.put(DatabaseOpenHelper.ARTIST_NAME, "Jawny Cash")
        mDbHelper.writableDatabase.insert(DatabaseOpenHelper.TABLE_NAME, null, values)

        values.clear()

        values.put(DatabaseOpenHelper.ARTIST_NAME, "Ludwig van Beethoven")
        mDbHelper.writableDatabase.insert(DatabaseOpenHelper.TABLE_NAME, null, values)
    }

    // Returns all artist records in the database
    private fun readArtists(): Cursor {
        return mDbHelper.writableDatabase.query(
            DatabaseOpenHelper.TABLE_NAME,
            DatabaseOpenHelper.columns, null, arrayOf(), null, null, null
        )
    }

    // Modify the contents of the database
    private fun fix() {

        // Sorry Lady Gaga :-(
        mDbHelper.writableDatabase.delete(
            DatabaseOpenHelper.TABLE_NAME,
            DatabaseOpenHelper.ARTIST_NAME + "=?",
            arrayOf("Lady Gaga")
        )

        // fix the Man in Black
        val values = ContentValues()
        values.put(DatabaseOpenHelper.ARTIST_NAME, "Johnny Cash")

        mDbHelper.writableDatabase.update(
            DatabaseOpenHelper.TABLE_NAME, values,
            DatabaseOpenHelper.ARTIST_NAME + "=?",
            arrayOf("Jawny Cash")
        )

    }

    // Delete all records
    private fun clearAll() {
        mDbHelper.writableDatabase.delete(DatabaseOpenHelper.TABLE_NAME, null, null)
    }

    // Close database
    override fun onDestroy() {

        mDbHelper.writableDatabase.close()
        mDbHelper.deleteDatabase()

        super.onDestroy()

    }
}