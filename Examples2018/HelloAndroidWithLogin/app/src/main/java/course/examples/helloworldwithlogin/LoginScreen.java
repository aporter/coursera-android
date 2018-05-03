package course.examples.helloworldwithlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class LoginScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        final EditText uname = findViewById(R.id.username_edittext);
        final EditText passwd = findViewById(R.id.password_edittext);

        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (!TextUtils.isEmpty(uname.getText()) && !TextUtils.isEmpty(passwd.getText())
                        && checkPassword(uname.getText(), passwd.getText())) {

                    // Create an explicit Intent for starting the HelloAndroid
                    // Activity
                    Intent helloAndroidIntent = new Intent(LoginScreen.this,
                            HelloAndroid.class);

                    // Use the Intent to start the HelloAndroid Activity
                    startActivity(helloAndroidIntent);

                } else {
                    uname.getText().clear();
                    passwd.getText().clear();
                }
            }
        });
    }

    @SuppressWarnings("unused")
    private boolean checkPassword(Editable uname, Editable passwd) {
        // Just pretending to extract text and check password
        return new Random().nextBoolean();
    }
}
