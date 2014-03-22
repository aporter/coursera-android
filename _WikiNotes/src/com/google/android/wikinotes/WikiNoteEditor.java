/* 
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.wikinotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.wikinotes.db.WikiNote;

/**
 * Wikinote editor activity. This is a very simple activity that allows the user
 * to edit a wikinote using a text editor and confirm or cancel the changes. The
 * title and body for the wikinote to edit are passed in using the extras bundle
 * and the onFreeze() method provisions for those values to be stored in the
 * icicle on a lifecycle event, so that the user retains control over whether
 * the changes are committed to the database.
 */
public class WikiNoteEditor extends Activity {

    protected static final String ACTIVITY_RESULT =
	    "com.google.android.wikinotes.EDIT";
    private EditText mNoteEdit;
    private String mWikiNoteTitle;

    @Override
    protected void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setContentView(R.layout.wiki_note_edit);

	mNoteEdit = (EditText) findViewById(R.id.noteEdit);

	// Check to see if the icicle has body and title values to restore
	String wikiNoteText =
	        icicle == null ? null : icicle.getString(WikiNote.Notes.BODY);
	String wikiNoteTitle =
	        icicle == null ? null : icicle.getString(WikiNote.Notes.TITLE);

	// If not, check to see if the extras bundle has these values passed in
	if (wikiNoteTitle == null) {
	    Bundle extras = getIntent().getExtras();
	    wikiNoteText =
		    extras == null ? null : extras
		            .getString(WikiNote.Notes.BODY);
	    wikiNoteTitle =
		    extras == null ? null : extras
		            .getString(WikiNote.Notes.TITLE);
	}

	// If we have no title information, this is an invalid intent request
	if (TextUtils.isEmpty(wikiNoteTitle)) {
	    // no note title - bail
	    setResult(RESULT_CANCELED);
	    finish();
	    return;
	}

	mWikiNoteTitle = wikiNoteTitle;

	// but if the body is null, just set it to empty - first edit of this
	// note
	wikiNoteText = wikiNoteText == null ? "" : wikiNoteText;

	// set the title so we know which note we are editing
	setTitle(getString(R.string.wiki_editing, wikiNoteTitle));

	// set the note body to edit
	mNoteEdit.setText(wikiNoteText);

	// set listeners for the confirm and cancel buttons
	((Button) findViewById(R.id.confirmButton))
	        .setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {
		        Intent i = new Intent();
		        i.putExtra(ACTIVITY_RESULT, mNoteEdit.getText()
		                .toString());
		        setResult(RESULT_OK, i);
		        finish();
		    }
	        });

	((Button) findViewById(R.id.cancelButton))
	        .setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {
		        setResult(RESULT_CANCELED);
		        finish();
		    }
	        });
	if (!getSharedPreferences(Eula.PREFERENCES_EULA,
				  Activity.MODE_PRIVATE)
	    .getBoolean(Eula.PREFERENCE_EULA_ACCEPTED, false)) {
	    Eula.showEula(this);
	}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	outState.putString(WikiNote.Notes.TITLE, mWikiNoteTitle);
	outState.putString(WikiNote.Notes.BODY, mNoteEdit.getText().toString());
    }
}
