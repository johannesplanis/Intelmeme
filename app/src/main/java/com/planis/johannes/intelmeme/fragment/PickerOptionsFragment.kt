package com.planis.johannes.intelmeme.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.planis.johannes.intelmeme.R


/**
 * A simple [Fragment] subclass.
 */
class PickerOptionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_picker_options, container, false)
        return view
    }
}// Required empty public constructor
