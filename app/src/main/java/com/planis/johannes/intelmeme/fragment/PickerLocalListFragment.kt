package com.planis.johannes.intelmeme.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.planis.johannes.intelmeme.R
import com.planis.johannes.intelmeme.ui.LayoutLocalListItemAdapter
import kotlinx.android.synthetic.main.fragment_picker_local_list.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class PickerLocalListFragment : Fragment() {


    private val imagesToPick = arrayOf(R.drawable.fuck_yeah, R.drawable.success_kid, R.drawable.einstein, R.drawable.its_something)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_picker_local_list, container, false)


        return view
    }

    private fun initView() {

        val list = ArrayList(Arrays.asList(*imagesToPick))
        val adapter = LayoutLocalListItemAdapter(activity, list)
        lvChooseLocally.adapter = adapter
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }
}
