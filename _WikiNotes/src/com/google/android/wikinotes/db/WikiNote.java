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

package com.google.android.wikinotes.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines columns and URIs used in the wikinotes application
 */
public class WikiNote {
    /**
     * Notes table
     */
    public static final class Notes implements BaseColumns {
	/**
	 * The content:// style URI for this table - this to see all notes or
	 * append a note name to see just the matching note.
	 */
	public static final Uri ALL_NOTES_URI =
	        Uri
	                .parse("content://com.google.android.wikinotes.db.wikinotes/wikinotes");

	/**
	 * This URI can be used to search the bodies of notes for an appended
	 * search term.
	 */
	public static final Uri SEARCH_URI =
	        Uri
	                .parse("content://com.google.android.wikinotes.db.wikinotes/wiki/search");

	/**
	 * The default sort order for this table - most recently modified first
	 */
	public static final String DEFAULT_SORT_ORDER = "modified DESC";

	/**
	 * The title of the note
	 * <P>
	 * Type: TEXT
	 * </P>
	 */
	public static final String TITLE = "title";

	/**
	 * The note body
	 * <P>
	 * Type: TEXT
	 * </P>
	 */
	public static final String BODY = "body";

	/**
	 * The timestamp for when the note was created
	 * <P>
	 * Type: INTEGER (long)
	 * </P>
	 */
	public static final String CREATED_DATE = "created";

	/**
	 * The timestamp for when the note was last modified
	 * <P>
	 * Type: INTEGER (long)
	 * </P>
	 */
	public static final String MODIFIED_DATE = "modified";

    }

    /**
     * Convenience definition for the projection we will use to retrieve columns
     * from the wikinotes
     */
    public static final String[] WIKI_NOTES_PROJECTION =
	    {Notes._ID, Notes.TITLE, Notes.BODY, Notes.MODIFIED_DATE};

    public static final String[] WIKI_EXPORT_PROJECTION =
    	{Notes.TITLE, Notes.BODY, Notes.CREATED_DATE, Notes.MODIFIED_DATE};

    /**
     * The root authority for the WikiNotesProvider
     */
    public static final String WIKINOTES_AUTHORITY =
	    "com.google.android.wikinotes.db.wikinotes";
}
