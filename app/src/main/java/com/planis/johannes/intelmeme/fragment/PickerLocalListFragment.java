package com.planis.johannes.intelmeme.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.planis.johannes.intelmeme.ui.LayoutLocalListItemAdapter;
import com.planis.johannes.intelmeme.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class PickerLocalListFragment extends Fragment {

    @Bind(R.id.lvChooseLocally)
    ListView lvChooseLocally;

    Integer[] imagesToPick = {R.drawable.fuck_yeah,R.drawable.success_kid,R.drawable.einstein,R.drawable.its_something};

    public PickerLocalListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picker_local_list, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {

        List<Integer> list = new ArrayList<>(Arrays.asList(imagesToPick));
        LayoutLocalListItemAdapter adapter = new LayoutLocalListItemAdapter(getActivity(),list);

        lvChooseLocally.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
