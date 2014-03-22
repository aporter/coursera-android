/*
 * Copyright (c) 2010, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package com.mamlambo.article.simplecalc;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final String LOG_TAG = "MainScreen";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText value1 = (EditText) findViewById(R.id.value1);
        final EditText value2 = (EditText) findViewById(R.id.value2);

        final TextView result = (TextView) findViewById(R.id.result);

        Button addButton = (Button) findViewById(R.id.addValues);
        addButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    int val1 = Integer.parseInt(value1.getText().toString());
                    int val2 = Integer.parseInt(value2.getText().toString());

                    Integer answer = val1 + val2;
                    result.setText(answer.toString());
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to add numbers", e);
                }
            }
        });

        Button multiplyButton = (Button) findViewById(R.id.multiplyValues);
        multiplyButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    int val1 = Integer.parseInt(value1.getText().toString());
                    int val2 = Integer.parseInt(value2.getText().toString());

                    Integer answer = val1 * val2;
                    result.setText(answer.toString());
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to multiply numbers", e);
                }
            }
        });
    }
}