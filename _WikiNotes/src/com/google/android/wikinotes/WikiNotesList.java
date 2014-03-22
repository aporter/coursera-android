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

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.wikinotes.db.WikiNote;

/**
 * Activity to list wikinotes. By default, the notes are listed in the order
 * of most recently modified to least recently modified. This activity can
 * handle requests to either show all notes, or the results of a title or body
 * search performed by the content provider.
 */
public class WikiNotesList extends ListActivity {

    /**
     * A key to store/retrieve the search criteria in a bundle
     */
    public static final String SEARCH_CRITERIA_KEY = "SearchCriteria";
    /**
     * The projection to use (columns to retrieve) for a query of wikinotes
     */
    public static final String[] PROJECTION = { WikiNote.Notes._ID,
					       WikiNote.Notes.TITLE,
					       WikiNote.Notes.MODIFIED_DATE };

    private Cursor mCursor;
    private WikiActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	Intent intent = getIntent();
	Uri uri = null;
	String query = null;

	// locate a query string; prefer a fresh search Intent over saved
	// state
	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    query = intent.getStringExtra(SearchManager.QUERY);
	} else if (savedInstanceState != null) {
	    query = savedInstanceState.getString(SearchManager.QUERY);
	}
	if (query != null && query.length() > 0) {
	    uri = Uri.withAppendedPath(WikiNote.Notes.SEARCH_URI, Uri
		.encode(query));
	}

	if (uri == null) {
	    // somehow we got called w/o a query so fall back to a reasonable
	    // default (all notes)
	    uri = WikiNote.Notes.ALL_NOTES_URI;
	}

	// Do the query
	Cursor c = managedQuery(uri, PROJECTION, null, null,
				WikiNote.Notes.DEFAULT_SORT_ORDER);
	mCursor = c;

	mHelper = new WikiActivityHelper(this);

	// Bind the results of the search into the list
	ListAdapter adapter = new SimpleCursorAdapter(
						      this,
						      android.R.layout.simple_list_item_1,
						      mCursor,
						      new String[] { WikiNote.Notes.TITLE },
						      new int[] { android.R.id.text1 });
	setListAdapter(adapter);

	// use the menu shortcut keys as default key bindings for the entire
	// activity
	setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
    }

    /**
     * Override the onListItemClick to open the wiki note to view when it is
     * selected from the list.
     */
    @Override
    protected void onListItemClick(ListView list, View view, int position,
				   long id) {
	Cursor c = mCursor;
	c.moveToPosition(position);
	String title = c.getString(c
	    .getColumnIndexOrThrow(WikiNote.Notes.TITLE));

	// Create the URI of the note we want to view based on the title
	Uri uri = Uri.withAppendedPath(WikiNote.Notes.ALL_NOTES_URI, title);
	Intent i = new Intent(Intent.ACTION_VIEW, uri);
	startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	menu.add(0, WikiNotes.HOME_ID, 0, R.string.menu_start)
	    .setShortcut('4', 'h').setIcon(R.drawable.icon_start);
	menu.add(0, WikiNotes.LIST_ID, 0, R.string.menu_recent)
	    .setShortcut('3', 'r').setIcon(R.drawable.icon_recent);
	menu.add(0, WikiNotes.ABOUT_ID, 0, R.string.menu_about)
	    .setShortcut('5', 'a').setIcon(android.R.drawable.ic_dialog_info);
	menu.add(0, WikiNotes.EXPORT_ID, 0, R.string.menu_export)
	    .setShortcut('6', 'x').setIcon(android.R.drawable.ic_dialog_info);
	menu.add(0, WikiNotes.IMPORT_ID, 0, R.string.menu_import)
	    .setShortcut('7', 'm').setIcon(android.R.drawable.ic_dialog_info);
	return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	switch (item.getItemId()) {
	case WikiNotes.HOME_ID:
	    mHelper.goHome();
	    return true;
	case WikiNotes.LIST_ID:
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
	default:
	    return false;
	}
    }
}
