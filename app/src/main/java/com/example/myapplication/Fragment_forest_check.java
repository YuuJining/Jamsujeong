package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_forest_check extends Fragment {
    private Context mContext;

    GridView gridView;

    String[] seatsArray = {
            "1", "2", "3", "4",
            "5", "6", "7", "8"
    };

    Integer[] seatsId = {
            201, 202, 203, 204,
            205, 206, 207, 208
    };


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_forest, container, false);

        gridView = (GridView) rootView.findViewById(R.id.seatsView);
        ButtonAdapter_check buttonAdapterCheck = new ButtonAdapter_check(mContext, seatsArray, seatsId);
        gridView.setAdapter(buttonAdapterCheck);

        return rootView;
    }


}
