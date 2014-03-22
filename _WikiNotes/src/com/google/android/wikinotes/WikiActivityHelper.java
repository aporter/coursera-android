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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.google.android.wikinotes.db.WikiNote;

/**
 * A helper class that implements operations required by Activities in the
 * system. Notably, this includes launching the Activities for edit and
 * delete.
 */
public class WikiActivityHelper {
    public static final int ACTIVITY_EDIT = 1;
    public static final int ACTIVITY_DELETE = 2;
    public static final int ACTIVITY_LIST = 3;
    public static final int ACTIVITY_SEARCH = 4;

    private Activity mContext;

    /**
     * Preferred constructor.
     * 
     * @param context
     *            the Activity to be used for things like calling
     *            startActivity
     */
    public WikiActivityHelper(Activity context) {
	mContext = context;
    }

    /**
     * @see #WikiActivityHelper(Activity)
     */
    // intentionally private; tell IDEs not to warn us about it
    @SuppressWarnings("unused")
    private WikiActivityHelper() {
    }

    /**
     * If a list of notes is requested, fire the WikiNotes list Content URI
     * and let the WikiNotesList activity handle it.
     */
    public void listNotes() {
	Intent i = new Intent(Intent.ACTION_VIEW,
			      WikiNote.Notes.ALL_NOTES_URI);
	mContext.startActivity(i);
    }

    /**
     * If requested, go back to the start page wikinote by requesting an
     * intent with the default start page URI
     */
    public void goHome() {
	Uri startPageURL = Uri
	    .withAppendedPath(WikiNote.Notes.ALL_NOTES_URI, mContext
		.getResources().getString(R.string.start_page));
	Intent startPage = new Intent(Intent.ACTION_VIEW, startPageURL);
	mContext.startActivity(startPage);
    }

    /**
     * Create an intent to start the WikiNoteEditor using the current title
     * and body information (if any).
     */
    public void editNote(String mNoteName, Cursor cursor) {
	// This intent could use the android.intent.action.EDIT for a wiki
	// note
	// to invoke, but instead I wanted to demonstrate the mechanism for
	// invoking
	// an intent on a known class within the same application directly.
	// Note
	// also that the title and body of the note to edit are passed in
	// using
	// the extras bundle.
	Intent i = new Intent(mContext, WikiNoteEditor.class);
	i.putExtra(WikiNote.Notes.TITLE, mNoteName);
	String body;
	if (cursor != null) {
	    body = cursor.getString(cursor
		.getColumnIndexOrThrow(WikiNote.Notes.BODY));
	} else {
	    body = "";
	}
	i.putExtra(WikiNote.Notes.BODY, body);
	mContext.startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * If requested, delete the current note. The user is prompted to confirm
     * this operation with a dialog, and if they choose to go ahead with the
     * deletion, the current activity is finish()ed once the data has been
     * removed, so that android naturally backtracks to the previous activity.
     */
    public void deleteNote(final Cursor cursor) {
	new AlertDialog.Builder(mContext)
	    .setTitle(
		      mContext.getResources()
			  .getString(R.string.delete_title))
	    .setMessage(R.string.delete_message)
	    .setPositiveButton(R.string.yes_button, new OnClickListener() {
		public void onClick(DialogInterface dialog, int arg1) {
		    Uri noteUri = ContentUris
			.withAppendedId(WikiNote.Notes.ALL_NOTES_URI, cursor
			    .getInt(0));
		    mContext.getContentResolver().delete(noteUri, null, null);
		    mContext.setResult(Activity.RESULT_OK);
		    mContext.finish();
		}
	    }).setNegativeButton(R.string.no_button, null).show();
    }

    /**
     * Equivalent to showOutcomeDialog(titleId, msg, false).
     * 
     * @see #showOutcomeDialog(int, String, boolean)
     * @param titleId
     *            the resource ID of the title of the dialog window
     * @param msg
     *            the message to display
     */
    private void showOutcomeDialog(int titleId, String msg) {
	showOutcomeDialog(titleId, msg, false);
    }

    /**
     * Creates and displays a Dialog with the indicated title and body. If
     * kill is true, the Dialog calls finish() on the hosting Activity and
     * restarts it with an identical Intent. This effectively restarts or
     * refreshes the Activity, so that it can reload whatever data it was
     * displaying.
     * 
     * @param titleId
     *            the resource ID of the title of the dialog window
     * @param msg
     *            the message to display
     */
    private void showOutcomeDialog(int titleId, String msg, final boolean kill) {
	new AlertDialog.Builder(mContext).setCancelable(false)
	    .setTitle(mContext.getResources().getString(titleId))
	    .setMessage(msg)
	    .setPositiveButton(R.string.export_dismiss_button,
			       new DialogInterface.OnClickListener() {
				   public void onClick(
						       DialogInterface dialog,
						       int which) {
				       if (kill) {
					   // restart the Activity w/ same
					   // Intent
					   Intent intent = mContext
					       .getIntent();
					   mContext.finish();
					   mContext.startActivity(intent);
				       }
				   }
			       }).create().show();
    }

    /**
     * Exports notes to the SD card. Displays Dialogs as appropriate (using
     * the Activity provided in the constructor as a Context) to inform the
     * user as to what is going on.
     */
    public void exportNotes() {
	// first see if an SD card is even present; abort if not
	if (!Environment.MEDIA_MOUNTED.equals(Environment
	    .getExternalStorageState())) {
	    showOutcomeDialog(R.string.export_failure_title, mContext
		.getResources().getString(R.string.export_failure_missing_sd));
	    return;
	}

	// do a query for all records
	Cursor c = mContext.managedQuery(WikiNote.Notes.ALL_NOTES_URI,
					 WikiNote.WIKI_EXPORT_PROJECTION,
					 null, null,
					 WikiNote.Notes.DEFAULT_SORT_ORDER);

	// iterate over all Notes, building up an XML StringBuffer along the
	// way
	boolean dataAvailable = c.moveToFirst();
	StringBuffer sb = new StringBuffer();
	String title, body, created, modified;
	int cnt = 0;
	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<wiki-notes>");
	while (dataAvailable) {
	    title = c.getString(c.getColumnIndexOrThrow("title"));
	    body = c.getString(c.getColumnIndexOrThrow("body"));
	    modified = c.getString(c.getColumnIndexOrThrow("modified"));
	    created = c.getString(c.getColumnIndexOrThrow("created"));
	    // do a quick sanity check to make sure the note is worth saving
	    if (!"".equals(title) && !"".equals(body) && title != null &&
		body != null) {
		sb.append("\n").append("<note>\n\t<title><![CDATA[")
		    .append(title).append("]]></title>\n\t<body><![CDATA[");
		sb.append(body).append("]]></body>\n\t<created>")
		    .append(created).append("</created>\n\t");
		sb.append("<modified>").append(modified)
		    .append("</modified>\n</note>");
		cnt++;
	    }
	    dataAvailable = c.moveToNext();
	}
	sb.append("\n</wiki-notes>\n");

	// open a file on the SD card under some useful name, and write out
	// XML
	FileWriter fw = null;
	File f = null;
	try {
	    f = new File(Environment.getExternalStorageDirectory() + "/" +
			 EXPORT_DIR);
	    f.mkdirs();
	    // EXPORT_FILENAME includes current date+time, for user benefit
	    String fileName = String.format(EXPORT_FILENAME, Calendar
		.getInstance());
	    f = new File(Environment.getExternalStorageDirectory() + "/" +
			 EXPORT_DIR + "/" + fileName);
	    if (!f.createNewFile()) {
		showOutcomeDialog(R.string.export_failure_title, mContext
		    .getResources()
		    .getString(R.string.export_failure_io_error));
		return;
	    }

	    fw = new FileWriter(f);
	    fw.write(sb.toString());
	} catch (IOException e) {
	    showOutcomeDialog(R.string.export_failure_title, mContext
		.getResources().getString(R.string.export_failure_io_error));
	    return;
	} finally {
	    if (fw != null) {
		try {
		    fw.close();
		} catch (IOException ex) {
		}
	    }
	}

	if (f == null) {
	    showOutcomeDialog(R.string.export_failure_title, mContext
		.getResources().getString(R.string.export_failure_io_error));
	    return;
	}
	// Victory! Tell the user and display the filename just written.
	showOutcomeDialog(R.string.export_success_title, String
	    .format(mContext.getResources()
		.getString(R.string.export_success), cnt, f.getName()));
    }

    /**
     * Imports records from SD card. Note that this is a destructive
     * operation: all existing records are destroyed. Displays confirmation
     * and outcome Dialogs before erasing data and after loading data.
     * 
     * Note that this is the method that actually does the work;
     * {@link #importNotes()} is the method that should be called to kick
     * things off.
     * 
     * @see #importNotes()
     */
    public void doImport() {
	// first see if an SD card is even present; abort if not
	if (!Environment.MEDIA_MOUNTED.equals(Environment
	    .getExternalStorageState())) {
	    showOutcomeDialog(R.string.import_failure_title, mContext
		.getResources().getString(R.string.export_failure_missing_sd));
	    return;
	}

	int cnt = 0;
	FileReader fr = null;
	ArrayList<StringBuffer[]> records = new ArrayList<StringBuffer[]>();
	try {
	    // first, find the file to load
	    fr = new FileReader(new File(Environment
		.getExternalStorageDirectory() +
					 "/" + IMPORT_FILENAME));

	    // don't destroy data until AFTER we find a file to import!
	    mContext.getContentResolver()
		.delete(WikiNote.Notes.ALL_NOTES_URI, null, null);

	    // next, parse that sucker
	    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    factory.setNamespaceAware(false);
	    XmlPullParser xpp = factory.newPullParser();
	    xpp.setInput(fr);
	    int eventType = xpp.getEventType();
	    StringBuffer[] cur = new StringBuffer[4];
	    int curIdx = -1;
	    while (eventType != XmlPullParser.END_DOCUMENT) {
		// save the previous note data-holder when we end a <note> tag
		if (eventType == XmlPullParser.END_TAG &&
		    "note".equals(xpp.getName())) {
		    if (cur[0] != null && cur[1] != null &&
			!"".equals(cur[0]) && !"".equals(cur[1])) {
			records.add(cur);
		    }
		    cur = new StringBuffer[4];
		    curIdx = -1;
		} else if (eventType == XmlPullParser.START_TAG &&
			   "title".equals(xpp.getName())) {
		    curIdx = 0;
		} else if (eventType == XmlPullParser.START_TAG &&
			   "body".equals(xpp.getName())) {
		    curIdx = 1;
		} else if (eventType == XmlPullParser.START_TAG &&
			   "created".equals(xpp.getName())) {
		    curIdx = 2;
		} else if (eventType == XmlPullParser.START_TAG &&
			   "modified".equals(xpp.getName())) {
		    curIdx = 3;
		} else if (eventType == XmlPullParser.TEXT && curIdx > -1) {
		    if (cur[curIdx] == null) {
			cur[curIdx] = new StringBuffer();
		    }
		    cur[curIdx].append(xpp.getText());
		}
		eventType = xpp.next();
	    }

	    // we stored data in a stringbuffer so we can skip empty records;
	    // now process the successful records & save to Provider
	    for (StringBuffer[] record : records) {
		Uri uri = Uri.withAppendedPath(WikiNote.Notes.ALL_NOTES_URI,
					       record[0].toString());
		ContentValues values = new ContentValues();
		values.put("title", record[0].toString().trim());
		values.put("body", record[1].toString().trim());
		values.put("created", Long.parseLong(record[2].toString()
		    .trim()));
		values.put("modified", Long.parseLong(record[3].toString()
		    .trim()));
		if (mContext.getContentResolver().insert(uri, values) != null) {
		    cnt++;
		}
	    }
	} catch (FileNotFoundException e) {
	    showOutcomeDialog(R.string.import_failure_title, mContext
		.getResources().getString(R.string.import_failure_nofile));
	    return;
	} catch (XmlPullParserException e) {
	    showOutcomeDialog(R.string.import_failure_title, mContext
		.getResources().getString(R.string.import_failure_corrupt));
	    return;
	} catch (IOException e) {
	    showOutcomeDialog(R.string.import_failure_title, mContext
		.getResources().getString(R.string.import_failure_io));
	    return;
	} finally {
	    if (fr != null) {
		try {
		    fr.close();
		} catch (IOException e) {
		}
	    }
	}

	// Victory! Inform the user.
	showOutcomeDialog(R.string.import_success_title, String
	    .format(mContext.getResources()
		.getString(R.string.import_success), cnt), true);
    }

    /**
     * Starts a note import. Essentially this method just displays a Dialog
     * requesting confirmation from the user for the destructive operation. If
     * the user confirms, {@link #doImport()} is called.
     * 
     * @see #doImport()
     */
    public void importNotes() {
	new AlertDialog.Builder(mContext).setCancelable(true)
	    .setTitle(
		      mContext.getResources()
			  .getString(R.string.import_confirm_title))
	    .setMessage(R.string.import_confirm_body)
	    .setPositiveButton(R.string.import_confirm_button,
			       new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface arg0,
						       int arg1) {
				       doImport();
				   }
			       })
	    .setNegativeButton(R.string.import_cancel_button, null).create()
	    .show();
    }

    /**
     * Launches the email system client to send a mail with the current
     * message.
     */
    public void mailNote(final Cursor cursor) {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("message/rfc822");
	intent.putExtra(Intent.EXTRA_SUBJECT, cursor.getString(cursor
	    .getColumnIndexOrThrow("title")));
	String body = String.format(mContext.getResources()
	    .getString(R.string.email_leader), cursor.getString(cursor
	    .getColumnIndexOrThrow("body")));
	intent.putExtra(Intent.EXTRA_TEXT, body);
	mContext.startActivity(Intent.createChooser(intent, "Title:"));
    }

    /* Some constants for filename structures. Could conceivably go into
     * strings.xml, but these don't need to be internationalized, so...
     */
    private static final String EXPORT_DIR = "WikiNotes";
    private static final String EXPORT_FILENAME = "WikiNotes_%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS.xml";
    private static final String IMPORT_FILENAME = "WikiNotes-import.xml";
}
