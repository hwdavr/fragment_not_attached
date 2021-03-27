package com.dialog.fragmentnotattached

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*


class TestFragment : BaseFragment() {
    private var tvStatus: TextView? = null
    private val job = Job()
    private val coroutineScope: CoroutineScope
        get() = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Test Fragment")
        enableBackButton(true)

        tvStatus = view.findViewById(R.id.textview_status)

        view
            .findViewById<Button>(R.id.button_dialog)
            .setOnClickListener {
                simulateApiCall()
            }
        view
            .findViewById<Button>(R.id.button_bottom_sheet)
            .setOnClickListener {
                showBottomSheet()
            }
        view
            .findViewById<Button>(R.id.button_recreate)
            .setOnClickListener {
                baseActivity?.recreate()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Uncomment this to fix the issue
//            checkIfFragmentAttached {
                data?.extras?.let {
                    val source = it.getInt(ImportPhotoBottomSheetFragment.EXTRA_SOURCE, 0)
                    if (source == ImportPhotoBottomSheetFragment.FROM_CAMERA) {
                        tvStatus?.text = getString(R.string.imported_photos_from_camera)
                    } else if (source == ImportPhotoBottomSheetFragment.FROM_GALLERY) {
                        tvStatus?.text = getString(R.string.imported_photos_from_gallery)
                    }
                }
//            }
        }
    }

    private fun showDialog() {
        Log.d(TAG, "Is Fragment attached: $isAdded")
        // Uncomment this to fix the issue
//        checkIfFragmentAttached {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning")
                .setMessage("API Error")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    tvStatus?.text = getString(R.string.error_not_found)
                }.show()
//        }
    }

    private fun simulateApiCall() {
        tvStatus?.text = getString(R.string.waiting_api)
        coroutineScope.launch(Dispatchers.IO) {
            delay(3000)
            coroutineScope.launch(Dispatchers.Main) {
                showDialog()
            }
        }
    }

    private fun showBottomSheet() {
        activity?.supportFragmentManager?.let {
            val bottomSheet = ImportPhotoBottomSheetFragment.newInstance()
            bottomSheet.setTargetFragment(this, REQUEST_CODE)
            bottomSheet.show(it, "")
        }
    }

    override fun onDestroy() {
        // To easily simulate the issue, the coroutine scope is not cancel
        //coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "TestFragment"
        const val REQUEST_CODE = 100

        fun newInstance() = TestFragment()
    }
}
