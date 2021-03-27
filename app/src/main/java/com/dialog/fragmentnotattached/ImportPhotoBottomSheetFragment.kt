package com.dialog.fragmentnotattached

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*


class ImportPhotoBottomSheetFragment: BottomSheetDialogFragment() {
    private val job = Job()
    private val coroutineScope: CoroutineScope
        get() = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container,
            false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_btn_camera).setOnClickListener {
            view
                .findViewById<TextView>(R.id.tv_bottom_sheet_heading)
                .text = getString(R.string.importing_photos)
            simulatePhotoImport(FROM_CAMERA)
        }

        view.findViewById<TextView>(R.id.tv_btn_gallery).setOnClickListener {
            view
                .findViewById<TextView>(R.id.tv_bottom_sheet_heading)
                .text = getString(R.string.importing_photos)
            simulatePhotoImport(FROM_GALLERY)
        }
    }

    private fun simulatePhotoImport(source: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            delay(3000)
            coroutineScope.launch(Dispatchers.Main) {
                setResults(source)
            }
        }
    }

    private fun setResults(source: Int) {
        targetFragment?.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(EXTRA_SOURCE, source)
            }
        )
        dismiss()
    }

    override fun onDestroy() {
        // To easily simulate the issue, the coroutine scope is not cancel
        //coroutineScope.cancel()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_SOURCE = "photo_source"
        const val FROM_CAMERA = 1
        const val FROM_GALLERY = 2

        fun newInstance() = ImportPhotoBottomSheetFragment()
    }
}
