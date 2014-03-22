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

import android.graphics.Rect;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import com.mamlambo.article.simplecalc.MainActivity;
import com.mamlambo.article.simplecalc.R;

public class LayoutTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private Button addValues;
    private Button multiplyValues;
    private View mainLayout;

    public LayoutTests() {
        super("com.mamlambo.article.simplecalc", MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        MainActivity mainActivity = getActivity();
        addValues = (Button) mainActivity.findViewById(R.id.addValues);
        multiplyValues = (Button) mainActivity
                .findViewById(R.id.multiplyValues);
        mainLayout = (View) mainActivity.findViewById(R.id.mainLayout);

    }

    public void testAddButtonOnScreen() {
        int fullWidth = mainLayout.getWidth();
        int fullHeight = mainLayout.getHeight();
        int[] mainLayoutLocation = new int[2];
        mainLayout.getLocationOnScreen(mainLayoutLocation);

        int[] viewLocation = new int[2];
        addValues.getLocationOnScreen(viewLocation);

        Rect outRect = new Rect();
        addValues.getDrawingRect(outRect);

        assertTrue("Add button off the right of the screen", fullWidth
                + mainLayoutLocation[0] > outRect.width() + viewLocation[0]);
        assertTrue("Add button off the bottom of the screen", fullHeight
                + mainLayoutLocation[1] > outRect.height() + viewLocation[1]);
    }
    
    public void testMultiplyButtonOnScreen() {
        int fullWidth = mainLayout.getWidth();
        int fullHeight = mainLayout.getHeight();
        int[] mainLayoutLocation = new int[2];
        mainLayout.getLocationOnScreen(mainLayoutLocation);

        int[] viewLocation = new int[2];
        multiplyValues.getLocationOnScreen(viewLocation);

        Rect outRect = new Rect();
        multiplyValues.getDrawingRect(outRect);

        assertTrue("Multiply button off the right of the screen", fullWidth
                + mainLayoutLocation[0] > outRect.width() + viewLocation[0]);
        assertTrue("Multiply button off the bottom of the screen", fullHeight
                + mainLayoutLocation[1] > outRect.height() + viewLocation[1]);
    }
}
