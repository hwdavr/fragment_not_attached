package com.dialog.fragmentnotattached

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected val baseActivity: AppCompatActivity?
        get() = (activity as? AppCompatActivity)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun enableBackButton(enable: Boolean) {
        baseActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(enable)
        baseActivity?.supportActionBar?.setDisplayShowHomeEnabled(enable)
        setHasOptionsMenu(enable)
    }

    /**
     * Check if the fragment is still attached and give the context to lambda
     */
    fun checkIfFragmentAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }

    fun setTitle(title: String) {
        baseActivity?.title = title
    }
}
