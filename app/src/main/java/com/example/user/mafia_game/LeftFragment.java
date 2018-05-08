package com.example.user.mafia_game;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftFragment extends Fragment implements View.OnClickListener  {

    private  OnCharacterButtonListener onCharacterButtonListener;


    public LeftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onCharacterButtonListener  = (OnCharacterButtonListener)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_left, container, false);
        view.findViewById(R.id.rule_mafia).setOnClickListener(this); //마피아
        view.findViewById(R.id.rule_police).setOnClickListener(this); //경찰
        view.findViewById(R.id.rule_citizen).setOnClickListener(this);//시민
        view.findViewById(R.id.rule_couple).setOnClickListener(this);//연인
        view.findViewById(R.id.rule_doctor).setOnClickListener(this);//의사
        view.findViewById(R.id.rule_reporter).setOnClickListener(this);//기자
        view.findViewById(R.id.rule_soldier).setOnClickListener(this);//군인
        view.findViewById(R.id.rule_terror).setOnClickListener(this);//테러
        return view;

    }
    //character  0 : 시민  , 1 : 마피아 , 2 : 경찰 , 3 : 군인 , 4 : 기자 , 5:테러 , 6:연인 , 7:의사
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rule_citizen:
                onCharacterButtonListener.onCharacterClick(0);
                break;
            case R.id.rule_mafia:
                onCharacterButtonListener.onCharacterClick(1);
                break;
            case R.id.rule_police:
                onCharacterButtonListener.onCharacterClick(2);
                break;
            case R.id.rule_soldier:
                onCharacterButtonListener.onCharacterClick(3);
                break;
            case R.id.rule_reporter:
                onCharacterButtonListener.onCharacterClick(4);
                break;
            case R.id.rule_terror:
                onCharacterButtonListener.onCharacterClick(5);
                break;
            case R.id.rule_couple:
                onCharacterButtonListener.onCharacterClick(6);
                break;
            case R.id.rule_doctor:
                onCharacterButtonListener.onCharacterClick(7);
                break;
        }


    }
}
