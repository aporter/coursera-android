package course.examples.helloworldwithlogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import java.util.Random

class LoginScreen : Activity() {

    private lateinit var uname: EditText
    private lateinit var passwd: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginscreen)

        uname = findViewById(R.id.username_edittext)
        passwd = findViewById(R.id.password_edittext)

    }

    fun onClick(v: View?) {
        if (!TextUtils.isEmpty(uname.text) &&
            !TextUtils.isEmpty(passwd.text)
            && checkPassword(uname.text, passwd.text)
        ) {

            // Create an explicit Intent for starting the HelloAndroid
            // Activity
            val helloAndroidIntent = Intent(
                this@LoginScreen,
                HelloAndroid::class.java
            )

            // Use the Intent to start the HelloAndroid Activity
            startActivity(helloAndroidIntent)

        } else {
            uname.text.clear()
            passwd.text.clear()
        }
    }


    private fun checkPassword(uname: Editable, passwd: Editable): Boolean {
        // Just pretending to extract text and check password
        return Random().nextBoolean()
    }

}
