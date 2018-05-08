package com.example.user.mafia_game;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by SCIT on 2018-03-09.
 */

class BackThread extends Activity implements Runnable {
    @Override
    public void run() {
        try{
            Toast.makeText(getParent().getApplicationContext(),"밤이 되었습니다. \n 마피아가 죽일 사람을 결정하고 있습니다.",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
