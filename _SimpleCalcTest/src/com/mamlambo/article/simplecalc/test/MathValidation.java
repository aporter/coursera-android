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
package com.mamlambo.article.simplecalc.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.mamlambo.article.simplecalc.MainActivity;
import com.mamlambo.article.simplecalc.R;

public class MathValidation extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private TextView result;

    public MathValidation() {
        super("com.mamlambo.article.simplecalc", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MainActivity mainActivity = getActivity();

        result = (TextView) mainActivity.findViewById(R.id.result);
    }

    private static final String NUMBER_24 = "2 4 ENTER ";
    private static final String NUMBER_74 = "7 4 ENTER ";
    private static final String NUMBER_5_DOT_5 = "5 PERIOD 5 ENTER ";
    private static final String NUMBER_NEG_22 = "MINUS 2 2 ENTER ";

    private static final String ADD_RESULT = "98";
    private static final String ADD_DECIMAL_RESULT = "79.5";
    private static final String ADD_NEGATIVE_RESULT = "52";
    private static final String MULTIPLY_RESULT = "1776";

    public void testAddValues() {
        // we use sendKeys instead of setText so it goes through entry
        // validation
        sendKeys(NUMBER_24);
        // now on value2 entry
        sendKeys(NUMBER_74);

        // now on Add button
        sendKeys("ENTER");

        // get result
        String mathResult = result.getText().toString();
        assertTrue("Add result should be 98 " + ADD_RESULT + " but was "
                + mathResult, mathResult.equals(ADD_RESULT));
    }

    public void testAddDecimalValues() {
        sendKeys(NUMBER_5_DOT_5 + NUMBER_74 + "ENTER");

        String mathResult = result.getText().toString();
        assertTrue("Add result should be " + ADD_DECIMAL_RESULT + " but was "
                + mathResult, mathResult.equals(ADD_DECIMAL_RESULT));
    }

    public void testSubtractValues() {
        sendKeys(NUMBER_NEG_22 + NUMBER_74 + "ENTER");

        String mathResult = result.getText().toString();
        assertTrue("Add result should be " + ADD_NEGATIVE_RESULT + " but was "
                + mathResult, mathResult.equals(ADD_NEGATIVE_RESULT));
    }
    

    public void testMultiplyValues() {
        sendKeys(NUMBER_24 + NUMBER_74 + " DPAD_RIGHT ENTER");

        String mathResult = result.getText().toString();
        assertTrue("Multiply result should be " + MULTIPLY_RESULT + " but was "
                + mathResult, mathResult.equals(MULTIPLY_RESULT));
    }
}
