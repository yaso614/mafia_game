package com.example.user.mafia_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

public class Police16Activity extends AppCompatActivity {

    MediaPlayer mediaPlayer,night_time;
    int call_game_type = 0;
    int playerAccount = 16;
    boolean user_game_result;
    SoundPool soundf;
    Button start; // 시작버튼,reset()
    TextView play; //하단 텍스트뷰
    int day = 1; // 날짜개념 클릭한번할때마다 1일 지남
    int bodyGuard_life =2; //恋人의 몸빵(2면 활성화,아니면 비활성화)
    int solider_life = 2; //軍人의 1회용 쉴드(2면활성화,2이면 비활성화)
    int camera = 2; //記者의 취재 (2면 활성화,1이면 비활성화)
    ImageView[] man_array = new ImageView[playerAccount]; //버튼
    boolean[] man_death = new boolean[playerAccount]; // false면 생존자 , true면 사망자 - マフィア는 시작부터 true
    boolean[] man_turn_over = new boolean[playerAccount]; //정체 파악 여부
    String[] man_type = {"マフィア", "医者", "軍人", "記者", "テロリスト", "恋人",
            "市民", "市民", "市民", "市民", "市民", "マフィア",
            "市民","市民","市民","市民","市民","市民","市民","市民" };
    String[] man_num = new String[playerAccount];  //유저 플레이어 번호 配列

    HashMap<String,String> mafia_location = new HashMap<>(); // マフィア 位置
    ImageView iv; // 알럿다이얼로그에 셋뷰해줄 이미지뷰
    TableLayout mPage1, mPage2, mPage3;  //보여줄 화면 전역변수
    int mafia_shot_sound,camera_sound,terror_sound,soldier_sound,mafia_die_sound,couple_die_sound;
    int death_man = 0; //죽은자
    int get_call_game_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police16);
        //사운드 풀 방식
        soundf = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mafia_shot_sound = soundf.load(this, R.raw.mafia_shot,1); //マフィア 효과음
       /* doctor_sound = soundf.load(this, R.raw.doctor,1); 医者효과음*/
        camera_sound = soundf.load(this, R.raw.camera_sound,1); //記者 효과음
        terror_sound = soundf.load(this, R.raw.timer_bomb,1); //테러 효과음
        soldier_sound = soundf.load(this, R.raw.bangtan_bgm,1); //軍人 효과음
        mafia_die_sound = soundf.load(this, R.raw.pocket_mafia,1); //マフィア 잡음 효과음
        couple_die_sound = soundf.load(this, R.raw.lose,1); //커플 효과음

        //미디어플레이어 사용식
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.daytime); //미플 문법
        night_time = MediaPlayer.create(getApplicationContext(),R.raw.night); //미플 문법
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //줄그인애임 위의 사운드풀 동일
        night_time.setAudioStreamType(AudioManager.STREAM_MUSIC); //줄그인애임 위의 사운드풀 동일

        //배경음 반복
        night_time.setLooping(true);
        night_time.start();



        for(int x = 0 ; x < playerAccount ; x++){
            man_num[x] = "";
            man_num[x] += x;
            Log.i("ｘは?" , man_num[x]);
        }

        iv = new ImageView(getApplicationContext());
        play = findViewById(R.id.play);
        start = findViewById(R.id.start);
        start.setOnClickListener(new reset());

        for (int a = 0; a < playerAccount; a++) {
            int id = getResources().getIdentifier("man" + a, "id", getPackageName());
            man_array[a] = findViewById(id);
            man_array[a].setOnClickListener(new choose_mafia());
        }

        setting(); // 게임 시작(マフィア결정)
        Log.i("온크리트 マフィア 배치 ", ""+mafia_location);

        get_call_game_type = getIntent().getIntExtra("call_game_type",0);

       /* if(get_call_game_type == 20){
            doBTN(4); //
        }*/



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(0,1,0,"マフィアゲーム");

        SubMenu playgame = menu.addSubMenu("START");
        SubMenu play_police = playgame.addSubMenu("警察でPLAY");
        play_police.add(0,2,0,"12名");
        play_police.add(0,3,0,"16名");
        play_police.add(0,4,0,"20名");
        SubMenu play_mafia = playgame.addSubMenu("マフィアでPLAY");
        play_mafia.add(0,5,0,"12名");
        play_mafia.add(0,6,0,"16名");
        play_mafia.add(0,7,0,"20名");
        MenuItem rule_description = menu.add(0,8,0,"ルール説明");
        MenuItem reset_menu = menu.add(0,9,0,"Reset");
        MenuItem game_end = menu.add(0,10,0,"");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return doBTN(item.getItemId());
    }

    public boolean doBTN(int itemId) {

        switch(itemId){
            case 1 :
                Toast.makeText(this,"マフィアゲームにようこそ",Toast.LENGTH_SHORT).show();
                return true;

            case 2 : //경찰 플레이 12명 Main
                call_game_type = 12;
                Intent intent = new Intent(getApplicationContext(), Police12Activity.class);
                intent.putExtra("call_game_type",call_game_type);
                startActivityForResult(intent,123);
                return true;


            case 3 : //경찰 플레이 16명
                call_game_type = 16;
                Intent intent_16people = new Intent(getApplicationContext() , Police16Activity.class);
                intent_16people.putExtra("call_game_type",call_game_type);
                startActivityForResult(intent_16people,123);
                return true;


            case 4 : //경찰 플레이 20명
                call_game_type = 20;
                Intent intent_abc = new Intent(getApplicationContext() , Police20Activity.class);
                intent_abc.putExtra("call_game_type",call_game_type);
                startActivityForResult(intent_abc,123);
                return true;

            case 5 :
                return true;
            case 6 :
                return true;
            case 7 :
                return true;
            case 8 : //플레이 룰
                Intent intent_play_rule = new Intent(Police16Activity.this, RuleExplain.class);
                startActivity(intent_play_rule);
                return true;
            case 9 : //재시작
                restart_game(); //게임 변수들 리셋후 재시작
                return true;
            case 10 : //게임종료
                finishAffinity();
                return true;

        }
        return false;
    }

    public void setting() {
        int suffle_count = 0;

        while (suffle_count <= 30) {
            int random1 = (int) (Math.random() * playerAccount);  //랜덤 0~11 바꿔질 A
            int random2 = (int) (Math.random() * playerAccount);  //랜덤 0-11 바꿔질 B
            String temp = "";  // 잠깐 바꿔치기하전에 넣어둘곳
            temp = man_type[random1];
            man_type[random1] = man_type[random2];
            man_type[random2] = temp;
            suffle_count++;

            Arrays.fill(man_death, false); //죽음 초기화
        }//while_end

        //マフィア 찾아다가 이미 죽은 상태로 만들기
        for(int x = 0 ; x < playerAccount ; x++){
            if(man_type[x].equals("マフィア")){
                man_death[x] = true; // 이미 죽은 상태setting
                mafia_location.put("mafia"+x,man_num[x]);//マフィア1에 マフィア 位置를
            }//if_end
        }//for_end

    } //setting()end


   /* public void replace(int i){
        for(int k = 0 ; k <= 11 ; k++){
            man_type[k] = "市民";
        }
        man_type[i] = "マフィア";

        man_death[i] = true;
    }*/

    //클릭하면 대상 정체를 밝히고  랜덤하게 한명 죽이자
    class choose_mafia implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            for (int a = 0; a < playerAccount; a++) {
                int cacao =  Police16Activity.this.getResources().getIdentifier("man"+a,"id",getPackageName());
                if(cacao == view.getId()){
                    invest(a);
                    Log.i("choose_mafia", a+"클릭");
                }
            }

        }
    }




    public void invest(int i) {
        night_time.pause();
        iv = new ImageView(getApplicationContext());
        switch (man_type[i]) {
            case "市民":
                Toast.makeText(
                        Police16Activity.this
                        ,"対象は 市民です。"
                        ,Toast.LENGTH_SHORT
                ).show();

                int citizen_view = (int)(Math.random()*5);
                if(citizen_view == 0){
                    iv.setImageResource(R.drawable.citizen_view1);
                }
                if(citizen_view == 1){
                    iv.setImageResource(R.drawable.citizen_view2);
                }
                if(citizen_view == 2){
                    iv.setImageResource(R.drawable.citizen_view3);
                }
                if(citizen_view == 3){
                    iv.setImageResource(R.drawable.citizen_view4);
                }
                if(citizen_view == 4){
                    iv.setImageResource(R.drawable.citizen_view5);
                }
                AlertDialog.Builder citizen_AD = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                citizen_AD.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();

                play.setText("対象は市民です。");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.goodman);
                man_array[i].setClickable(false);
                break;
            case "医者":
                Toast.makeText(
                        Police16Activity.this
                        ,"対象は 医者です。"
                        ,Toast.LENGTH_SHORT
                ).show();

                int doctor_view = (int)(Math.random()*3);
                if(doctor_view == 0){
                    iv.setImageResource(R.drawable.doctor_view1);
                }
                if(doctor_view == 1){
                    iv.setImageResource(R.drawable.doctor_view2);
                }
                if(doctor_view == 2){
                    iv.setImageResource(R.drawable.doctor_view3);
                }

                AlertDialog.Builder doctor_AD = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                doctor_AD.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();

                play.setText("対象は医者です。");
                man_array[i].setImageResource(R.drawable.icon_doctor);
                man_turn_over[i] = true;
                man_array[i].setClickable(false);
                break;
            case "マフィア":
                Toast.makeText(
                        Police16Activity.this
                        ,"マフィアを見つけました。"
                        ,Toast.LENGTH_SHORT
                ).show();
                soundf.play(mafia_die_sound,1,1,3,0,1);
                int mafia_view = (int)(Math.random()*5);
                if(mafia_view == 0){
                    iv.setImageResource(R.drawable.mafia_view1);
                }
                if(mafia_view == 1){
                    iv.setImageResource(R.drawable.mafia_view2);
                }
                if(mafia_view == 2){
                    iv.setImageResource(R.drawable.mafia_view3);
                }
                if(mafia_view == 3){
                    iv.setImageResource(R.drawable.mafia_view4);
                }
                if(mafia_view == 4){
                    iv.setImageResource(R.drawable.mafia_view5);
                }
                AlertDialog.Builder mafia_invest_AD = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                mafia_invest_AD.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();

                mafia_location.remove("mafia"+i);
                Log.i("マフィア 状態", mafia_location+"");
                play.setText("対象は マフィア です。");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_mafia_die);
                man_array[i].setClickable(false);
                break;
            case "軍人":
                Toast.makeText(
                        Police16Activity.this
                        ,"対象は 軍人 です。"
                        ,Toast.LENGTH_SHORT
                ).show();

                int soldier_view = (int)(Math.random()*3);
                if(soldier_view == 0){
                    iv.setImageResource(R.drawable.soldier_view1);
                }
                if(soldier_view == 1){
                    iv.setImageResource(R.drawable.soldier_view2);
                }
                if(soldier_view == 2){
                    iv.setImageResource(R.drawable.soldier_view3);
                }
                AlertDialog.Builder terrorist = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                terrorist.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("対象は軍人です。");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_soldier);
                man_array[i].setClickable(false);
                break;
            case "記者":
                Toast.makeText(
                        Police16Activity.this
                        ,"対象は記者です。"
                        ,Toast.LENGTH_SHORT
                ).show();

                int report_view = (int)(Math.random()*3);
                if(report_view == 0){
                    iv.setImageResource(R.drawable.reporter_view1);
                }
                if(report_view == 1){
                    iv.setImageResource(R.drawable.reporter_view2);
                }
                if(report_view == 2){
                    iv.setImageResource(R.drawable.reporter_view3);
                }

                AlertDialog.Builder reporter_invest = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                reporter_invest.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("対象は記者です。");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_reporter);
                man_array[i].setClickable(false);
                break;
            case "恋人":
                Toast.makeText(
                        Police16Activity.this
                        ,"対象は恋人です。"
                        ,Toast.LENGTH_SHORT
                ).show();

                int couple_view = (int)(Math.random()*2);
                if(couple_view == 0){
                    iv.setImageResource(R.drawable.couple_view1);
                }
                if(couple_view == 1){
                    iv.setImageResource(R.drawable.couple_view2);
                }
                AlertDialog.Builder couple_invest_DA = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                couple_invest_DA.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("対象は恋人です。");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_couple);
                man_array[i].setClickable(false);
                break;
            case "テロリスト":
                Toast.makeText(
                        Police16Activity.this
                        ,"対象はテロリストです。"
                        ,Toast.LENGTH_SHORT
                ).show();
                int terror_view = (int)(Math.random()*3);
                if(terror_view == 0){
                    iv.setImageResource(R.drawable.terrorist_view1);
                }
                if(terror_view == 1){
                    iv.setImageResource(R.drawable.terrorist_view2);
                }
                if(terror_view == 2){
                    iv.setImageResource(R.drawable.terrorist_view3);
                }
                AlertDialog.Builder t_invest_DA = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                t_invest_DA.setTitle("[捜査結果]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"番目の人は"+man_type[i]+"でした。\nマフィアはテロリストを 排除しまし。。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("対象はテロリストです。");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_terror);
                man_array[i].setClickable(false);
                break;
        }

        int ply = 0 ;
        mafia_kill();
        for(int reporter = 0 ; reporter > playerAccount ; reporter++){
            if(man_type[reporter].equals("記者")){
                ply = Integer.parseInt(man_num[reporter]);
                if(camera == 2 && man_death[ply] == false && day == 3){
                    //카메라 사용한적 없으면 2 && 記者 생존시 && 셋째날일때
                    reporter_shot(); //記者 메소드 실행후 camera = 1; 되서 한번만 실행한다.
                }
            }
        }
        Log.i("配列", Arrays.toString(man_type));
        day++;
        Log.i("今日は何日か?",""+day);
        gameover_check();
    }





    class reset implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.start) { //start button 클릭시
                restart_game();
            } //start button _ end
        } //onClick_Override _ end
    }


    //恋人は 할일
    public void bodyguard(int kill_target, int bodyGuarder) {//マフィア의 총맞는 사람 , 恋人 位置
        iv = new ImageView(getApplicationContext());
        int bodyguardTarget = (int) (Math.random() * playerAccount); // 恋人は 지킬 사람(랜덤1~11)
        Log.i("守る対象",bodyguardTarget+"");
        if (kill_target == bodyguardTarget && man_death[bodyguardTarget] == true &&
                !(man_type[bodyguardTarget].equals("マフィア")) && !(man_type[bodyguardTarget].equals("恋人"))) {
            soundf.play(couple_die_sound,1,1,1,0,1);
            // マフィア 죽일대상 = 恋人 지킬대상 같으면서 지킬대상의 상태가 이미 총맞아 죽은상태인경우
            // 맞기 전에 행동하는게 아니라 マフィア는 쏴서 죽였지만 되살린뒤에 恋人가 대신 죽게끔 해놓음..
            switch (man_type[kill_target]){ // 그래서
                case "市民" :
                    iv.setImageResource(R.drawable.couple_break);
                    AlertDialog.Builder couple_citizen = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_citizen.setTitle("[恋人の犠牲]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"番目の人は"+man_type[bodyGuarder]+"でした。\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"番に 位置した愛する恋人の代わりに死亡しました。")
                            .setPositiveButton("閉じる", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //市民 되살리고
                    man_array[kill_target].setImageResource(R.drawable.goodman); //市民 살아있는 그림적용
                    break;
                case "医者" :
                    iv.setImageResource(R.drawable.couple_break);
                    AlertDialog.Builder couple_doctor = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_doctor.setTitle("[恋人の犠牲]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"番目の人は"+man_type[bodyGuarder]+"でした。\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"番に位置した愛する恋人の 代わりに死亡しました。")
                            .setPositiveButton("閉じる", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //市民 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_doctor);
                    break;
                case "テロリスト" :
                    AlertDialog.Builder couple_terror = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_terror.setTitle("[恋人の犠牲]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"番目の人は"+man_type[bodyGuarder]+"でした。\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"番に位置した愛する恋人の代わりに 死亡しました。")
                            .setPositiveButton("閉じる", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //市民 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_terror);
                    break;
                case "記者" :
                    AlertDialog.Builder couple_reporter = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_reporter.setTitle("[恋人の犠牲]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"番目の人は"+man_type[bodyGuarder]+"でした。\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"番に 位置した愛する恋人の代わりに死亡しました。")
                            .setPositiveButton("閉じる", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //市民 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_reporter);
                    break;
                case "軍人" :
                    AlertDialog.Builder couple_soldier = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_soldier.setTitle("[恋人の犠牲]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"番目の人は"+man_type[bodyGuarder]+"でした。\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"番に 位置した愛する恋人の代わりに死亡しました。")
                            .setPositiveButton("閉じる", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //市民 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_soldier);
                    break;
            }
            //살인대상 == 恋人대상 && 살인대상이 생존시 && 보호대상 != 보호자
            man_death[bodyGuarder] = true; // 보호자가 죽는다.
            man_array[bodyGuarder].setClickable(false); //恋人 클릭불가
            man_turn_over[bodyGuarder] = true; //恋人의 카드는 뒤집혔다.
            String abc = man_type[kill_target].toString();
            play.setText(abc+"のために 恋人が犠牲しました。");
            man_array[bodyGuarder].setImageResource(R.drawable.icon_couple_die);
            Log.i("보호대상 : ", "보호대상 : "+man_type[kill_target].toString() + "");
            bodyGuard_life--;
        }

    }//恋人가 하는 일 끝

    public void citizen_death(){
        iv.setImageResource(R.drawable.someone_s_death);
        AlertDialog.Builder sitizen_death = new AlertDialog.Builder(this).setView(iv);
        sitizen_death.setTitle("昨晩市民一名死にました。.")
                .setIcon(R.drawable.icon_mafia)
                .setMessage("市民たちは恐怖に怯えています\nマフィアを捕まえてください")
                .setPositiveButton("閉じる",null)
                .setCancelable(true)
                .show();
        soundf.play(mafia_shot_sound,1,1,1,0,1);
    }


    public void mafia_kill() {
        iv = new ImageView(getApplicationContext());
        int kill_man = (int) (Math.random() * playerAccount); // 죽일 대상을 지정한다.
        Log.i("初期状態", Arrays.toString(man_death));
        if (man_death[kill_man] == false) {
            switch (man_type[kill_man]) { // 죽일 대상
                case "市民":
                    iv.setImageResource(R.drawable.someone_s_death);
                    AlertDialog.Builder sitizen_death = new AlertDialog.Builder(this).setView(iv);
                    sitizen_death.setTitle("昨夜 市民 一名が しぼうしました。")
                            .setIcon(R.drawable.icon_mafia)
                            .setMessage("市民たちは恐怖に怯えています\nマフィアを捕まえてください")
                            .setPositiveButton("閉じる",null)
                            .setCancelable(true)
                            .show();
                    soundf.play(mafia_shot_sound,1,1,0,0,1);
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.goodman_die);
                    play.setText("昨夜 市民が 殺されました。");
                    Log.i("マフィアの 除去対象 : ", String.valueOf(kill_man) + "番目の人");
                    Log.i("配列", Arrays.toString(man_death));
                    break;
                case "恋人":
                    citizen_death();
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.icon_couple_die);
                    Log.i("マフィアの 除去対象 : ", String.valueOf(kill_man) + "番目の人");
                    Log.i("配列", Arrays.toString(man_death));
                    play.setText("夜に 恋人が殺されました。");
                    break;
                case "医者":
                    citizen_death();
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.icon_doctor_die);
                    play.setText("夜に 医者が殺されました。");
                    Log.i("マフィアの　排除対象 : ", String.valueOf(kill_man) + "番目の人");
                    Log.i("配列", Arrays.toString(man_death));
                    break;
                case "軍人":
                    if(solider_life == 2){
                        soundf.play(soldier_sound,1,1,3,0,1);
                        iv = new ImageView(getApplicationContext());
                        iv.setImageResource(R.drawable.soldier_tanking);
                        AlertDialog.Builder soldier_AD = new AlertDialog.Builder(this).setView(iv);
                        soldier_AD.setTitle("軍人が攻撃を耐えました!!!")
                                .setIcon(R.drawable.icon_soldier)
                                .setMessage("軍人は 正体が　現れました。再び攻撃をもらうと死にます。")
                                .setPositiveButton("閉じる",null)
                                .setCancelable(true)
                                .show();
                        man_array[kill_man].setClickable(false); //軍人 클릭불가
                        man_array[kill_man].setImageResource(R.drawable.icon_soldier_shoted); //軍人정체공개
                        man_turn_over[kill_man] = true; //軍人의 카드는 뒤집혔다.
                        Log.i("マフィアが 軍人 攻撃 : ", String.valueOf(kill_man) + 1 +"番の 間に 位置");
                        Log.i("配列", Arrays.toString(man_death));
                        play.setText("マフィアの 銃に 打たれても 軍人は 死にません");
                        solider_life--;
                        break;
                    }
                    else if(solider_life == 1){
                        iv.setImageResource(R.drawable.someone_s_death);
                        AlertDialog.Builder soldier_AD = new AlertDialog.Builder(this).setView(iv);
                        soldier_AD.setTitle(man_type[kill_man]+"が死亡しました。")
                                .setIcon(R.drawable.icon_mafia)
                                .setMessage("マフィアは怪我した軍人に\n最後の一発を打ちました。")
                                .setPositiveButton("閉じる",null)
                                .setCancelable(true)
                                .show();
                        man_death[kill_man] = true; //죽인다
                        man_array[kill_man].setClickable(false);
                        man_array[kill_man].setImageResource(R.drawable.icon_soldier_die);
                        play.setText("夜に 軍人に 殺されました。");
                        Log.i("マフィアの　排除対象 : ", String.valueOf(kill_man) + "番目の人");
                        Log.i("配列", Arrays.toString(man_death));
                        break;
                    }
                case "記者":
                    citizen_death();
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.icon_reporter_die);
                    play.setText("夜に 記者が殺されました。");
                    Log.i("マフィアの　排除対象 : ", String.valueOf(kill_man) + "番目の人");
                    Log.i("配列", Arrays.toString(man_death));
                    break;
                case "テロリスト":
                    int mafia_loca = 0;

                    for(int zzz = 0 ; zzz < playerAccount ; zzz++){
                        if(mafia_location.containsKey("mafia"+zzz)){ // mafia


                            String a = mafia_location.get("mafia"+zzz); //mafia+zzz의 value를 받아다
                            mafia_loca = Integer.parseInt(a); // 인티저해서 숫자로

                            if(explosion(kill_man)){ //テロリスト가 マフィア를 경계할때
                                Log.i("explosion(kill_man)",""+explosion(kill_man));
                                soundf.play(terror_sound,1,1,3,0,1);
                                iv.setImageResource(R.drawable.terrorist_explosion); // 이미지뷰 iv에 이미지 넣기
                                AlertDialog.Builder explosion = new AlertDialog.Builder(this).setView(iv);
                                explosion.setTitle("テロリスト !!!") // 다이어로그에 타이틀 정하기
                                        .setIcon(R.drawable.icon_terror) //아이콘 정하기
                                        .setPositiveButton("閉じる",null)
                                        .setMessage("マフィアを警戒していたテロリストは"+(Integer.parseInt(man_num[mafia_loca])+1)+"に位置している"+man_type[mafia_loca]+"と一緒に自爆しました。")
                                        .setCancelable(true)
                                        .show();

                                Log.i("マフィアの位置",mafia_loca+"");
                                man_array[mafia_loca].setImageResource(R.drawable.icon_mafia_die); //マフィア 죽은 상태로 전환(이미지)
                                mafia_location.remove("マフィア"+zzz);


                                man_array[kill_man].setImageResource(R.drawable.icon_terror_die); //テロリスト 죽은 상태로 전환(이미지)
                                man_death[kill_man] = true; //테러 죽은 상태로 전환(목숨)
                                man_turn_over[kill_man] = true; //테러 카드 뒤집힌 상태
                                man_array[kill_man].setClickable(false); //죽은 테러 클릭불가
                                iv = new ImageView(getApplicationContext());
                                Log.i("マフィア 상태", ""+mafia_location);
                                break;
                            }else{ //그게 아니면 マフィア가 쐇을때 그냥 조용히 죽는것..
                                citizen_death();
                                man_death[kill_man] = true; //죽인다
                                man_turn_over[kill_man] = true; //뒤집기
                                man_array[kill_man].setClickable(false);
                                man_array[kill_man].setImageResource(R.drawable.icon_terror_die);
                                play.setText("夜に テロリストが殺されました。");
                                Log.i("マフィアの　排除対象 : ", String.valueOf(kill_man) + "番目の人");
                                Log.i("配列", Arrays.toString(man_death));
                                break;
                            }//else_end
                        }//if_end
                    }//for_end
            }//switch end
        }else{
            //이미 죽은 시체를 쏘면
            Log.i("mafia_kill_else문", "死体を打つ");
            mafia_kill();  //재귀함수 쓰면 안될꺼같긴한데 모르겠다 에라.. 다시 쏴라
            return;
        }
        iv = new ImageView(getApplicationContext());

        int bodynum = 0; // 恋人 位置를 찾아서

        for(int a = 0 ; a < playerAccount ; a++ ){
            if(man_type[a].equals("恋人")){
                bodynum = (Integer.parseInt(man_num[a]));
                if(man_death[a] == false) { //恋人가 살아있으면
                    bodyguard(kill_man, bodynum); // 恋人 메소드 실행
                }
            }
        }

        int ply = 0 ;
        for(int reporter = 0 ; reporter < playerAccount ; reporter++){
            if(man_type[reporter].equals("記者")){
                ply = Integer.parseInt(man_num[reporter]);
                Log.i("記者は何番に位置していますか？",""+man_num[ply]);
                if(man_death[ply] == false && camera == 2 && day > 2){
                    //記者 생존 && 취재 1번만 가능 && 첫째날 취재불가
                    reporter_shot(); //記者 메소드 실행후 camera = 1; 되서 한번만 실행한다.
                }
            }
        }



        //恋人 생명상태가 2면
        Log.i("恋人 位置",bodynum+"");
        Log.i("今回死ぬ人",kill_man+"");

        Log.i("最後の状況", Arrays.toString(man_death));
        Log.i("ひっくり返たカード", Arrays.toString(man_turn_over));
    } // マフィア가 사람 죽이는 메소드


    //記者가 할일
    public void reporter_shot(){
        soundf.play(camera_sound,1,1,1,0,1);
        iv = new ImageView(getApplicationContext());
        int reporting_target = (int)(Math.random() * playerAccount);
        switch(man_type[reporting_target]){
            case "市民" :
                    /*레이아웃 노가다 방식
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); //inflater 생성
                    View viewInDialog = inflater.inflate(R.layout.dialog_citizen, null); //inflater로 View 객체에 레이아웃 넣기
                    AlertDialog.Builder citizen = new AlertDialog.Builder(this).setView(viewInDialog);  // 문법 한줄*/
                int report_citizen_view = (int)(Math.random()*5);
                if(report_citizen_view == 0){
                    iv.setImageResource(R.drawable.citizen_view1);
                }
                if(report_citizen_view == 1){
                    iv.setImageResource(R.drawable.citizen_view2);
                }
                if(report_citizen_view == 2){
                    iv.setImageResource(R.drawable.citizen_view3);
                }
                if(report_citizen_view == 3){
                    iv.setImageResource(R.drawable.citizen_view4);
                }
                if(report_citizen_view == 4){
                    iv.setImageResource(R.drawable.citizen_view5);
                }
                AlertDialog.Builder citizen = new AlertDialog.Builder(this).setView(iv); //잘 이해안감
                citizen.setTitle("[誤報注意!]") // 다이어로그에 타이틀 정하기
                        .setIcon(R.drawable.goodman) //아이콘 정하기
                            .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人は"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setIcon(R.drawable.goodman)
                        .setPositiveButton("閉じる",null)
                        .setCancelable(false)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.goodman);
                man_turn_over[reporting_target] = true;
                   /* ((ViewGroup)iv.getParent()).removeView(iv);*/
                break;
            case "マフィア" :
                int report_mafia_view = (int)(Math.random()*5);
                if(report_mafia_view == 0){
                    iv.setImageResource(R.drawable.mafia_view1);
                }
                if(report_mafia_view == 1){
                    iv.setImageResource(R.drawable.mafia_view2);
                }
                if(report_mafia_view == 2){
                    iv.setImageResource(R.drawable.mafia_view3);
                }
                if(report_mafia_view == 3){
                    iv.setImageResource(R.drawable.mafia_view4);
                }
                if(report_mafia_view == 4){
                    iv.setImageResource(R.drawable.mafia_view5);
                }
                AlertDialog.Builder mafia = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                mafia.setTitle("[記者のスクープ]")
                        .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人が"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setIcon(R.drawable.icon_mafia)
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView play = findViewById(R.id.play);
                                play.setText("記者가 マフィアを探しました。 !!!");
                            }
                        })
                        .setCancelable(true)
                        .show();
                mafia_location.remove("mafia"+reporting_target);
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_mafia_die);
                man_turn_over[reporting_target] = true;
                    /*((ViewGroup)iv.getParent()).removeView(iv);*/
                break;
            case "医者" :
                int report_doctor_view = (int)(Math.random()*3);
                if(report_doctor_view == 0){
                    iv.setImageResource(R.drawable.doctor_view1);
                }
                if(report_doctor_view == 1){
                    iv.setImageResource(R.drawable.doctor_view2);
                }
                if(report_doctor_view == 2){
                    iv.setImageResource(R.drawable.doctor_view3);
                }
                AlertDialog.Builder doctor = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                doctor.setTitle("[記者のスクープ]")
                        .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人は"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setIcon(R.drawable.icon_doctor)
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*play.setText("");*/
                            }
                        })
                        .setCancelable(true)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_doctor);
                man_turn_over[reporting_target] = true;

                break;
            case "軍人" :
                int soldier_view = (int)(Math.random()*3);
                if(soldier_view == 0){
                    iv.setImageResource(R.drawable.soldier_view1);
                }
                if(soldier_view == 1){
                    iv.setImageResource(R.drawable.soldier_view2);
                }
                if(soldier_view == 2){
                    iv.setImageResource(R.drawable.soldier_view3);
                }
                AlertDialog.Builder soldier = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                soldier.setTitle("[記者のスクープ]")
                        .setIcon(R.drawable.icon_soldier)
                        .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人は"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView text = findViewById(R.id.play);
                            }
                        })
                        .setCancelable(true)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_soldier);
                man_turn_over[reporting_target] = true;
                break;
            case "テロリスト" :
                int report_teror_view = (int)(Math.random()*2);
                if(report_teror_view == 0){
                    iv.setImageResource(R.drawable.terrorist_view1);
                }
                if(report_teror_view == 1){
                    iv.setImageResource(R.drawable.terrorist_view2);
                }
                if(report_teror_view == 2){
                    iv.setImageResource(R.drawable.terrorist_view3);
                }
                AlertDialog.Builder terrorist = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                terrorist.setTitle("[記者のスクープ]")
                        .setIcon(R.drawable.icon_terror)
                        .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人は"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                play.setText("テロリストの正体がばれました。");
                            }
                        })
                        .setCancelable(true)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_terror);
                man_turn_over[reporting_target] = true;
                break;
            case "記者" :
                int report_view = (int)(Math.random()*3);
                if(report_view == 0){
                    iv.setImageResource(R.drawable.reporter_view1);
                }
                if(report_view == 1){
                    iv.setImageResource(R.drawable.reporter_view2);
                }
                if(report_view == 2){
                    iv.setImageResource(R.drawable.reporter_view3);
                }
                AlertDialog.Builder reporter = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                reporter.setTitle("[記者のスクープ]")
                        .setIcon(R.drawable.icon_reporter)
                        .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人は"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView text = findViewById(R.id.play);
                            }
                        })
                        .setCancelable(true)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_reporter);
                man_turn_over[reporting_target] = true;

                break;
            case "恋人" :
                int report_couple_view = (int)(Math.random()*3);
                if(report_couple_view == 0){
                    iv.setImageResource(R.drawable.couple_view1);
                }
                if(report_couple_view == 1){
                    iv.setImageResource(R.drawable.couple_view2);
                }
                if(report_couple_view == 2){
                    iv.setImageResource(R.drawable.couple_view3);
                }
                AlertDialog.Builder couple = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄

                couple.setTitle("[記者のスクープ]")
                        .setIcon(R.drawable.icon_couple)
                        .setMessage("記者は世界中に知らせます。\n"+(Integer.parseInt(man_num[reporting_target])+1)+"番目の人は"+man_type[reporting_target]+"だと言う記事を出しました。")
                        .setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView text = findViewById(R.id.play);
                            }
                        })
                        .setCancelable(true)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_couple);
                man_turn_over[reporting_target] = true;
                break;
        }
        iv = new ImageView(getApplicationContext());
        // 그림 초기화 ,
        camera=1;//카메라 사용후 카메라 고장남
    }//記者 끝







    public boolean explosion(int km){
        int warning = (int)(Math.random() * playerAccount); //テロリスト 랜덤 지목대상
        int terror_locaton = 0;
        for(int f = 0 ; f < playerAccount ; f++){
            if(man_type[f].equals("テロリスト")){
                terror_locaton = Integer.parseInt(man_num[f]);
                //テロリスト 位置
            }
        }

        //warning == terror_location 맞추면 발동하는게 맞긴한데.. 확률이 너무 낮아서 그냥
        // 정체만 안들어난 상태면 マフィア가 쏘면 터지게..
        if(man_turn_over[terror_locaton] == false){
            //그냥 테러 정체 숨겨져 있으면 발동
            return true;
        }
        return false;
    }

    private void restart_game(){
        iv = new ImageView(getApplicationContext());
        for (int x = 0; x < playerAccount; x++) {
            setting(); // 셔플로 패섞기 ,  모든 생명 부활 , マフィア 찾아서 죽여놓기 ,
            man_array[x].setImageResource(R.drawable.icon_citizen); // 모든 버튼이미지 초기화
            man_array[x].setClickable(true); // 클릭막은걸 풀어준다.
            man_turn_over[x] = false; // 뒤집힘을 리셋한다.
        }
        solider_life = 2; // 軍人 목숨을 2개로 만든다.
        camera = 2; // 리셋시 記者 취재 가능
        day = 1; //재시작시 첫날부터 시작
        mafia_location.clear(); //マフィア 位置 초기화
        death_man = 0;
        for(int x = 0 ; x < playerAccount ; x++){
            if(man_type[x].equals("マフィア")){
                man_death[x] = true; // 이미 죽은 상태setting
                mafia_location.put("mafia"+x,man_num[x]);//マフィア1에 マフィア 位置를
            }//if_end
        }//for_end
        Log.i("リセット マフィア 配置 ", ""+mafia_location);
        play.setText("ゲームを開始します");
    }


    private int gameover_check(){

        death_man = 0;
        for(int z = 0 ; z < playerAccount ; z++){
            if(man_death[z] == true){
                death_man +=1;
            }
        }

        //승리기준
        if(mafia_location.isEmpty()){
            //マフィア 다 죽어버렸으면
            Intent game_over_message = new Intent(getApplicationContext(),GameOverActivity.class);
            user_game_result = true;
            game_over_message.putExtra("user_game_result",user_game_result);
            game_over_message.putExtra("man_type",man_type);
            game_over_message.putExtra("playerAccount",playerAccount);
            game_over_message.putExtra("death_man",death_man-2);// 죽은 사람 숫자
            /*startActivity(game_over_message); 반환값이 없을때 사용하는 방법*/
            startActivityForResult(game_over_message,123); // (보낼 인텐트,택배받을때 인증서명);
            Log.i("マフィア 敗北" , "市民チーム勝利");
        }
        //패배기준
        //패 다 깠는데 マフィア가 다 한명이라도 살아있는 경우
        /*if(man_turn_over.toString().contains("false")){

        }*/



        Log.i("死んだ人は何人ですか" , death_man-2+"");
        if(!(mafia_location.isEmpty()) && death_man == 7){ //マフィア가 1명이라도 살아있는데 사망자가 12명(市民10명+マフィア2명)명일경우
            Intent game_over_message = new Intent(getApplicationContext(),GameOverActivity.class);
            game_over_message.putExtra("user_game_result",user_game_result); //게임 결과 true or false
            game_over_message.putExtra("man_type",man_type);// 게임 인원 구성
            game_over_message.putExtra("playerAccount",playerAccount);// 게임플레이어 숫자
            game_over_message.putExtra("death_man",death_man-2);// 죽은 사람 숫자
            /*startActivity(game_over_message); 반환값이 없을때 사용하는 방법*/
            startActivityForResult(game_over_message,123); // (보낼 인텐트,택배받을때 인증서명);
            Log.i("マフィア 勝利" , "市民チーム敗北");
        }
        return 0;
    }

    @Override                           //보낸이          우편물타입        내용물
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 123 && resultCode == RESULT_OK){
            restart_game();
        }
        int playing = data.getIntExtra("game_type",0);

        if(requestCode == 123 && resultCode == 777 && playing == 12){
            Toast.makeText(this,"12人play",Toast.LENGTH_SHORT);
            Intent intent_12 = new Intent(getApplicationContext(), Police12Activity.class);
            intent_12.putExtra("call_game_type",call_game_type);
            finish();
            startActivityForResult(intent_12,123);
        }
        if(requestCode == 123 && resultCode == 777 && playing == 16){
            Toast.makeText(this,"16人play",Toast.LENGTH_SHORT);
            Intent intent_16 = new Intent(getApplicationContext(), Police12Activity.class);
            intent_16.putExtra("call_game_type",call_game_type);
            finish();
            startActivityForResult(intent_16,123);
        }
        if(requestCode == 123 && resultCode == 777 && playing == 20){
            Toast.makeText(this,"20人play",Toast.LENGTH_SHORT);
            Intent intent_20 = new Intent(getApplicationContext() , Police20Activity.class);
            intent_20.putExtra("call_game_type",call_game_type);
            finish();
            startActivityForResult(intent_20,123);
        }
    }


    @Override
    protected void onPause() {
        mediaPlayer.pause();
        night_time.stop();
        soundf.autoPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        night_time.start();
        super.onResume();
    }

}// 끝







