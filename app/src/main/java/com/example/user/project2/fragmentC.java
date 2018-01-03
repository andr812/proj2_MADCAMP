package com.example.user.project2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by user on 2018-01-03.
 */

public class fragmentC extends Fragment {

    Button orderbtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3cafe, container, false);

        orderbtn = rootView.findViewById(R.id.orderbtn);

        orderbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderActivity.class));
            }
        });


        return rootView;
    }
}
