package course.examples.fragments.staticlayout;

// Callback interface that defines how a TitlesFragment notifies the QuoteViewerActivity when
// user clicks on a List Item in the TitlesFragment
interface ListSelectionListener {
    void onListSelection(int index);
}