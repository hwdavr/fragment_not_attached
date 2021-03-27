package com.dialog.fragmentnotattached

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Home")
        enableBackButton(false)

        val button = view.findViewById<Button>(R.id.button_open)
        button.setOnClickListener {
            val ft = activity?.supportFragmentManager?.beginTransaction()
            ft?.addToBackStack("")
            ft?.replace(R.id.content, TestFragment.newInstance(), null)
            ft?.commit()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
