package com.nexters.intersection.intersectionapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nexters.intersection.intersectionapp.R;
import com.nexters.intersection.intersectionapp.ui.activity.MainActivity;
import com.nexters.intersection.intersectionapp.utils.IntersactionSession;

public final class StartFragment extends Fragment {
    private static final String KEY_CONTENT = "StartFragment:Location";
    private int location = 0;
    private boolean isVisited = false;
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
        final IntersactionSession intersactionSession = IntersactionSession.getInstance(vw.getContext());

        ImageView img = (ImageView)vw.findViewById(R.id.img_menu);
        ImageButton nextBtn = (ImageButton) vw.findViewById(R.id.btn_next);

        switch(location){
            case 0:
                vw = inflater.inflate(R.layout.activity_intro, null);
                ImageView tutoImg = (ImageView)vw.findViewById(R.id.ai_iv_title_tutorial);
                tutoImg.setVisibility(View.VISIBLE);
            case 1:
                   img.setImageResource(R.drawable.howtouse_1);
                break;
            case 2:
                   img.setImageResource(R.drawable.howtouse_2);
                break;
            case 3:
                   img.setImageResource(R.drawable.howtouse_3);
                break;
            case 4:
                   img.setImageResource(R.drawable.howtouse_4);
                nextBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        isVisited = intersactionSession.getBoolean(IntersactionSession.IS_VISITED);
                        intersactionSession.putBoolean(IntersactionSession.IS_VISITED, true);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        if(!isVisited)
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
