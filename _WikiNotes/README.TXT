INTRODUCTION

WikiNotes for Android is a sample application designed to be as simple
as possible while still demonstrating several major concepts of the
Android platform, including:

- Activities with IntentFilters for viewing, editing and searching
  specific MIME data types, in this case a MIME type defined for a
  wikinote or a list of wikinotes
- The activities themselves which demonstrate lifecycle, state management,
  forward navigation through intent resolution
- Use of the Linkify class to turn ordinary textual information into
  live links that fire default intents
- Use of default intents on different data types to achieve different
  results, for example, navigating to a new wiki note, or calling a
  phone number, or browsing to a URL, etc.
- A custom ContentProvider that looks up wiki notes using a note name,
  this demonstrates the pattern matching of ReST-like URIs to find
  the data and pass parameters like the note name to find as part of
  the URI.
- Within the ContentProvider implementation, SQLite usage for storage,
  retrieval, update, deletion and searching of data
- UI layout and creation for multiple activities, including menus

There are many more subtleties demonstrated by the application, but it
is also deliberately kept limited in functionality so as not to
obscure the important concepts behind a cloud of features and edge
cases.


BUILDING

The project directory is ready for use from either Eclipse or with the
activityCreator.py script (if you want to use something other than
eclipse).

To use Eclipse, install Eclipse, the Android SDK and the Eclipse ADT
plugin (if you haven't already, see
http://code.google.com/android/intro/installing.html for more details)
then start Eclipse, and create a new Android project. Use the
"from existing source" option, and browse to this directory. The
Android project should then be created, and you can browse the source,
build the application and run it in the emulator.

For instructions on how to build the application with activityCreator
Start a command line and change to the WikiNotes directory, then type
activityCreator.py com.google.android.wikinotes.WikiNotes

(It is assumed you have set up activityCreator.py, ant and emulator
to run from the command line. Please see
http://code.google.com/android/intro/installing.html#otherides
for more details)

It should give some warnings about files already existing, this is
expected. Then type:

ant

and the project should build.

Start the emulator and then install the WikiNotes.apk file from the
bin directory:

adb install WikiNotes.apk

You should then be able to run the app.

