package com.example.user.mafia_game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RuleExplain extends AppCompatActivity implements OnCharacterButtonListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_explain);

        MafiaFragment  mf = new MafiaFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_bottom, mf).commit();

    }


    //character  0 : 시민  , 1 : 마피아 , 2 : 경찰 , 3 : 군인 , 4 : 기자 , 5:테러 , 6:연인 , 7:의사
    @Override
    public void onCharacterClick(int character) {

        Fragment fragment = null;
        switch (character){
            case 0:
                fragment = new CitizenFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 1:
                fragment = new MafiaFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 2:
                fragment = new PoliceFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 3:
                fragment = new SoldierFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 4:
                fragment = new ReporterFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 5:
                fragment = new TerrorFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 6:
                fragment = new CoupleFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
            case 7:
                fragment = new DoctorFragment();
                Log.i("Who is he? :",String.valueOf(character));
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_bottom,fragment).commit();


    }
}