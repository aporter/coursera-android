package course.examples.datamanagement.fileinternal

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter

class InternalFileWriteReadActivity : Activity() {

    companion object {
        private const val TAG = "IntFileWriteRead"
        private const val FILE_NAME = "TestFile.txt"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        val textView = findViewById<TextView>(R.id.textview)


        // Check whether FILE_NAME already exists in directory used
        // by the openFileOutput() method.
        // If the text file doesn't exist, then create it now

        if (!getFileStreamPath(FILE_NAME).exists()) {
            try {
                writeFile()
            } catch (e: FileNotFoundException) {
                Log.i(TAG, "FileNotFoundException")
            }

        }


        // Read the data from the text file and display it
        try {

            readFileAndDisplay(textView)

        } catch (e: IOException) {
            Log.i(TAG, "IOException")
        }

    }

    @Throws(FileNotFoundException::class)
    private fun writeFile() {

        val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)

        val pw = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))

        pw.println("Line 1: This is a test of the File Writing API")
        pw.println("Line 2: This is a test of the File Writing API")
        pw.println("Line 3: This is a test of the File Writing API")

        pw.close()

    }

    @Throws(IOException::class)
    private fun readFileAndDisplay(tv: TextView) {

        val sep = System.getProperty("line.separator")
        val fis = openFileInput(FILE_NAME)
        val br = BufferedReader(InputStreamReader(fis))

        br.forEachLine {
            tv.append(it + sep)
        }
        br.close()
    }
}