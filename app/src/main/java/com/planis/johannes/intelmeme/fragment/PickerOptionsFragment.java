package com.planis.johannes.intelmeme.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planis.johannes.intelmeme.R;
import com.planis.johannes.intelmeme.activity.PhotoPickerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class PickerOptionsFragment extends Fragment {

    PhotoPickerActivity activity;

    public PickerOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_picker_options, container, false);

        ButterKnife.bind(this,view);
        activity = (PhotoPickerActivity) getActivity();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }



    @OnClick({R.id.tvChooseFromGalerry, R.id.tvChooseLocally, R.id.tvChooseService})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvChooseFromGalerry:
                chooseFromGalerry();
                break;
            case R.id.tvChooseLocally:
                chooseLocally();
                break;
            case R.id.tvChooseService:
                chooseService();
                break;
        }
    }

    private void chooseService() {
        if (null!=activity){
            activity.chooseService();
        }
    }

    private void chooseLocally() {
        if (null!=activity){
            activity.chooseLocally();
        }
    }

    private void chooseFromGalerry() {
        if (null!=activity){
            activity.chooseFromGalerry();
        }
    }
}
