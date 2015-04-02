package com.nexters.intersection.intersectionapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private int location = 1;
    private boolean isVisited = false;

    public static StartFragment newInstance(int location) {
        StartFragment fragment = new StartFragment();
        fragment.location = location;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_start, null);
        final IntersactionSession intersactionSession = IntersactionSession.getInstance(vw.getContext());

        ImageView img = (ImageView) vw.findViewById(R.id.img_menu);
        ImageButton nextBtn = (ImageButton) vw.findViewById(R.id.btn_next);

        switch (location) {
            case 1:
                img.setImageResource(R.drawable.tutorial_1);
                break;
            case 2:
                img.setImageResource(R.drawable.tutorial_2);
                break;
            case 3:
                img.setImageResource(R.drawable.tutorial_3);
                break;
            case 4:
                img.setImageResource(R.drawable.tutorial_4);
                break;
            case 5:
                img.setImageResource(R.drawable.tutorial_5);
                break;
            case 6:
                img.setImageResource(R.drawable.tutorial_6);
                break;
            case 7:
                img.setImageResource(R.drawable.tutorial_7);
                break;
            case 8:
                img.setImageResource(R.drawable.tutorial_8);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isVisited = intersactionSession.getBoolean(IntersactionSession.IS_VISITED);
                        intersactionSession.putBoolean(IntersactionSession.IS_VISITED, true);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        if (!isVisited)
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
    }
}
