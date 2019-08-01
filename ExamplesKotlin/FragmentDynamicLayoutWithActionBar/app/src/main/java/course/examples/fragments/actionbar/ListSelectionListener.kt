package course.examples.fragments.actionbar

// Callback interface that allows this Fragment to notify the
// QuoteViewerActivity when user clicks on a List Item

internal interface ListSelectionListener {
    fun onListSelection(index: Int)
}