/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.google.android.wikinotes;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.wikinotes.db.WikiNote;

/**
 * The WikiNotes activity is the default handler for displaying individual
 * wiki notes. It takes the wiki note name to view from the intent data URL
 * and defaults to a page name defined in R.strings.start_page if no page name
 * is given. The lifecycle events store the current content URL being viewed.
 * It uses Linkify to turn wiki words in the body of the wiki note into
 * clickable links which in turn call back into the WikiNotes activity from
 * the Android operating system. If a URL is passed to the WikiNotes activity
 * for a page that does not yet exist, the WikiNoteEditor activity is
 * automatically started to place the user into edit mode for a new wiki note
 * with the given name.
 */
public class WikiNotes extends Activity {
    private static final String TAG = "WikiNotes";

	/**
     * The view URL which is prepended to a matching wikiword in order to fire
     * the WikiNotes activity again through Linkify
     */

    private static final String KEY_URL = "wikiNotesURL";

    public static final int EDIT_ID = Menu.FIRST;
    public static final int HOME_ID = Menu.FIRST + 1;
    public static final int LIST_ID = Menu.FIRST + 3;
    public static final int DELETE_ID = Menu.FIRST + 4;
    public static final int ABOUT_ID = Menu.FIRST + 5;
    public static final int EXPORT_ID = Menu.FIRST + 6;
    public static final int IMPORT_ID = Menu.FIRST + 7;
    public static final int EMAIL_ID = Menu.FIRST + 8;

    private TextView mNoteView;
    Cursor mCursor;
    private Uri mURI;
    String mNoteName;
    private static final Pattern WIKI_WORD_MATCHER;
    private WikiActivityHelper mHelper;

    static {
	// Compile the regular expression pattern that will be used to
	// match WikiWords in the body of the note
	WIKI_WORD_MATCHER = Pattern
	    .compile("\\b[A-Z]+[a-z0-9]+[A-Z][A-Za-z0-9]+\\b");
    }

    @Override
    public void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setContentView(R.layout.main);

	mNoteView = (TextView) findViewById(R.id.noteview);

	// get the URL we are being asked to view
	Uri uri = getIntent().getData();

	if ((uri == null) && (icicle != null)) {
	    // perhaps we have the URI in the icicle instead?
	    uri = Uri.parse(icicle.getString(KEY_URL));
	}

	// do we have a correct URI including the note name?
	if ((uri == null) || (uri.getPathSegments().size() < 2)) {
	    // if not, build one using the default StartPage name
	    uri = Uri.withAppendedPath(WikiNote.Notes.ALL_NOTES_URI,
				       getResources()
					   .getString(R.string.start_page));
	}

	// can we find a matching note?
	Cursor cursor = managedQuery(uri, WikiNote.WIKI_NOTES_PROJECTION,
				     null, null, null);

	boolean newNote = false;
	if ((cursor == null) || (cursor.getCount() == 0)) {
	    // no matching wikinote, so create it
	    uri = getContentResolver().insert(uri, null);
	    if (uri == null) {
		Log.e(TAG, "Failed to insert new wikinote into " +
				   getIntent().getData());
		finish();
		return;
	    }
	    // make sure that the new note was created successfully, and
	    // select
	    // it
	    cursor = managedQuery(uri, WikiNote.WIKI_NOTES_PROJECTION, null,
				  null, null);
	    if ((cursor == null) || (cursor.getCount() == 0)) {
		Log.e(TAG, "Failed to open new wikinote: " +
				   getIntent().getData());
		finish();
		return;
	    }
	    newNote = true;
	}

	mURI = uri;
	mCursor = cursor;
	cursor.moveToFirst();
	mHelper = new WikiActivityHelper(this);

	// get the note name
	String noteName = cursor.getString(cursor
	    .getColumnIndexOrThrow(WikiNote.Notes.TITLE));
	mNoteName = noteName;

	// set the title to the name of the page
	setTitle(getResources().getString(R.string.wiki_title, noteName));

	// If a new note was created, jump straight into editing it
	if (newNote) {
	    mHelper.editNote(noteName, null);
	} else if (!getSharedPreferences(Eula.PREFERENCES_EULA,
					 Activity.MODE_PRIVATE)
	    .getBoolean(Eula.PREFERENCE_EULA_ACCEPTED, false)) {
	    Eula.showEula(this);
	}

	// Set the menu shortcut keys to be default keys for the activity as
	// well
	setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
	
	Log.i(TAG, "Exiting onCreate()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	// Put the URL currently being viewed into the icicle
	outState.putString(KEY_URL, mURI.toString());
    }

    @Override
    protected void onResume() {
	super.onResume();
	Cursor c = mCursor;
	if (c.getCount() < 1) {
	    // if the note can't be found, don't try to load it -- bail out
	    // (probably means it got deleted while we were frozen;
	    // thx to joe.bowbeer for the find)
	    finish();
	    return;
	}
	c.requery();
	c.moveToFirst();
	showWikiNote(c
	    .getString(c.getColumnIndexOrThrow(WikiNote.Notes.BODY)));
    }

    /**
     * Show the wiki note in the text edit view with both the default Linkify
     * options and the regular expression for WikiWords matched and turned
     * into live links.
     * 
     * @param body
     *            The plain text to linkify and put into the edit view.
     */
    private void showWikiNote(CharSequence body) {
	TextView noteView = mNoteView;
	noteView.setText(body);

	// Add default links first - phone numbers, URLs, etc.
	Linkify.addLinks(noteView, Linkify.ALL);

	// Now add in the custom linkify match for WikiWords
	Linkify.addLinks(noteView, WIKI_WORD_MATCHER,
			 WikiNote.Notes.ALL_NOTES_URI.toString() + "/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	menu.add(0, EDIT_ID, 0, R.string.menu_edit).setShortcut('1', 'e')
	    .setIcon(R.drawable.icon_delete);
	menu.add(0, HOME_ID, 0, R.string.menu_start).setShortcut('4', 'h')
	    .setIcon(R.drawable.icon_start);
	menu.add(0, LIST_ID, 0, R.string.menu_recent).setShortcut('3', 'r')
	    .setIcon(R.drawable.icon_recent);
	menu.add(0, DELETE_ID, 0, R.string.menu_delete).setShortcut('2', 'd')
	    .setIcon(R.drawable.icon_delete);
	menu.add(0, ABOUT_ID, 0, R.string.menu_about).setShortcut('5', 'a')
	    .setIcon(android.R.drawable.ic_dialog_info);
	menu.add(0, EXPORT_ID, 0, R.string.menu_export).setShortcut('7', 'x')
	    .setIcon(android.R.drawable.ic_dialog_info);
	menu.add(0, IMPORT_ID, 0, R.string.menu_import).setShortcut('8', 'm')
	    .setIcon(android.R.drawable.ic_dialog_info);	
	menu.add(0, EMAIL_ID, 0, R.string.menu_email).setShortcut('6', 'm')
	    .setIcon(android.R.drawable.ic_dialog_info);
	return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	switch (item.getItemId()) {
	case EDIT_ID:
	    mHelper.editNote(mNoteName, mCursor);
	    return true;
	case HOME_ID:
	    mHelper.goHome();
	    return true;
	case DELETE_ID:
	    mHelper.deleteNote(mCursor);
	    return true;
	case LIST_ID:
	    mHelper.listNotes();
	    return true;
	case WikiNotes.ABOUT_ID:
	    Eula.showEula(this);
	    return true;
	case WikiNotes.EXPORT_ID:
	    mHelper.exportNotes();
	    return true;
	case WikiNotes.IMPORT_ID:
	    mHelper.importNotes();
	    return true;
	case WikiNotes.EMAIL_ID:
	    mHelper.mailNote(mCursor);
	    return true;
	default:
	    return false;
	}
    }

    /**
     * If the note was edited and not canceled, commit the update to the
     * database and then refresh the current view of the linkified note.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
				    Intent result) {
	super.onActivityResult(requestCode, resultCode, result);
	if ((requestCode == WikiActivityHelper.ACTIVITY_EDIT) &&
	    (resultCode == RESULT_OK)) {
	    // edit was confirmed - store the update
	    Cursor c = mCursor;
	    c.requery();
	    c.moveToFirst();
	    Uri noteUri = ContentUris
		.withAppendedId(WikiNote.Notes.ALL_NOTES_URI, c.getInt(0));
	    ContentValues values = new ContentValues();
	    values.put(WikiNote.Notes.BODY, result
		.getStringExtra(WikiNoteEditor.ACTIVITY_RESULT));
	    values.put(WikiNote.Notes.MODIFIED_DATE, System
		.currentTimeMillis());
	    getContentResolver().update(noteUri, values,
					"_id = " + c.getInt(0), null);
	    showWikiNote(c.getString(c
		.getColumnIndexOrThrow(WikiNote.Notes.BODY)));
	}
    }
}
