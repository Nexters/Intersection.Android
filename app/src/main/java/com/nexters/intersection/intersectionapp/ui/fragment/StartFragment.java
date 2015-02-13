package com.nexters.intersection.intersectionapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.ui.activity.MainActivity;

public final class StartFragment extends Fragment {
    private static final String KEY_CONTENT = "StartFragment:Location";
    private int location = 0;

    public static StartFragment newInstance(int location) {
        StartFragment fragment = new StartFragment();
        fragment.location = location;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            location = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_start, null);
        ImageView img = (ImageView)vw.findViewById(R.id.img_menu);
        Button nextBtn = (Button) vw.findViewById(R.id.btn_next);
        switch(location){
            case 0:
                   img.setImageResource(R.drawable.howtouse_1);
                break;
            case 1:
                   img.setImageResource(R.drawable.howtouse_2);
                break;
            case 2:
                   img.setImageResource(R.drawable.howtouse_3);
                break;
            case 3:
                   img.setImageResource(R.drawable.howtouse_4);
                nextBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);

                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
                nextBtn.setVisibility(View.VISIBLE);
                break;


        }
        return vw;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, location);
    }
}
