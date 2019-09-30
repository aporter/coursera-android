package course.examples.datamanagement.fileexternal

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import java.io.*

class ExternalFileWriteReadActivity : Activity() {

    companion object {
        private const val TAG = "ExtFileWriteRead"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        if (Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()
        ) {

            val fileName = "painter.png"
            val outFile = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

            if (!outFile.exists())
                copyImageToMemory(outFile)

            val imageview = findViewById<ImageView>(R.id.image)
            imageview.setImageURI(Uri.parse("file://" + outFile.absolutePath))

        }
    }

    private fun copyImageToMemory(outFile: File) {

        var outputStream: BufferedOutputStream? = null
        var inputStream: BufferedInputStream? = null

        try {

            outputStream = BufferedOutputStream(
                FileOutputStream(outFile)
            )

            inputStream = BufferedInputStream(
                resources.openRawResource(R.raw.painter)
            )

            //copy(inputStream, outputStream)
            inputStream.copyTo(outputStream)

        } catch (e: FileNotFoundException) {
            Log.e(TAG, "FileNotFoundException")
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, "IOException")
            }

        }
    }
}