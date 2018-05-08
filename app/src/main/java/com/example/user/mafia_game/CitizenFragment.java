package com.example.user.mafia_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SCIT on 2018-03-08.
 */
                                //호환성 추가된 프래그먼트 사용
public class CitizenFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                        //이렇게하면 뷰가 하나 넘어갈거임
        return inflater.inflate(R.layout.fragment_citizen, null);//뷰를 리턴
    }
}
