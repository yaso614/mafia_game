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

public class Police20Activity extends AppCompatActivity {

    MediaPlayer mediaPlayer,night_time;
    int call_game_type = 0;
    int playerAccount = 20;
    boolean user_game_result;
    SoundPool soundf;
    Button start; // 시작버튼,reset()
    TextView play; //하단 텍스트뷰
    int day = 1; // 날짜개념 클릭한번할때마다 1일 지남
    int bodyGuard_life =2; //연인의 몸빵(2면 활성화,아니면 비활성화)
    int solider_life = 2; //군인의 1회용 쉴드(2면활성화,2이면 비활성화)
    int camera = 2; //기자의 취재 (2면 활성화,1이면 비활성화)
    ImageView[] man_array = new ImageView[playerAccount]; //버튼
    boolean[] man_death = new boolean[playerAccount]; // false면 생존자 , true면 사망자 - 마피아는 시작부터 true
    boolean[] man_turn_over = new boolean[playerAccount]; //정체 파악 여부
    String[] man_type = {"마피아", "의사", "군인", "기자", "테러리스트", "연인",
            "시민", "시민", "시민", "시민", "시민", "마피아",
            "시민","시민","시민","시민","시민","시민","시민","시민" };
    String[] man_num = new String[playerAccount];  //유저 플레이어 번호 배열

    HashMap<String,String> mafia_location = new HashMap<>(); // 마피아 위치
    ImageView iv; // 알럿다이얼로그에 셋뷰해줄 이미지뷰
    TableLayout mPage1, mPage2, mPage3;  //보여줄 화면 전역변수
    int mafia_shot_sound,camera_sound,terror_sound,soldier_sound,mafia_die_sound,couple_die_sound;
    int death_man = 0; //죽은자
    int get_call_game_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police20);
        soundf = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        //사운드풀 방식
        mafia_shot_sound = soundf.load(this, R.raw.mafia_shot,1); //마피아 효과음
       /* doctor_sound = soundf.load(this, R.raw.doctor,1); 의사효과음*/
        camera_sound = soundf.load(this, R.raw.camera_sound,1); //기자 효과음
        terror_sound = soundf.load(this, R.raw.timer_bomb,1); //테러 효과음
        soldier_sound = soundf.load(this, R.raw.bangtan_bgm,1); //군인 효과음
        mafia_die_sound = soundf.load(this, R.raw.pocket_mafia,1); //마피아잡음 효과음
        couple_die_sound = soundf.load(this, R.raw.lose,1); //커플 효과음
        //미디어플레이어 사용식
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.daytime); //미플 문법
        night_time = MediaPlayer.create(getApplicationContext(),R.raw.night); //미플 문법
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //줄그인애임 위의 사운드풀 동일
        night_time.setAudioStreamType(AudioManager.STREAM_MUSIC); //줄그인애임 위의 사운드풀 동일

        night_time.setLooping(true);
        night_time.start();



        for(int x = 0 ; x < playerAccount ; x++){
            man_num[x] = "";
            man_num[x] += x;
            Log.i("x는?" , man_num[x]);
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

        setting(); // 게임 시작(마피아결정)
        Log.i("온크리트 마피아 배치 ", ""+mafia_location);

        get_call_game_type = getIntent().getIntExtra("call_game_type",0);

       /* if(get_call_game_type == 20){
            doBTN(4); //
        }*/



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(0,1,0,"마피아게임");

        SubMenu playgame = menu.addSubMenu("게임하기");
        SubMenu play_police = playgame.addSubMenu("경찰로 플레이");
        play_police.add(0,2,0,"12명");
        play_police.add(0,3,0,"16명");
        play_police.add(0,4,0,"20명");
        SubMenu play_mafia = playgame.addSubMenu("마피아로 플레이");
        play_mafia.add(0,5,0,"12명");
        play_mafia.add(0,6,0,"16명");
        play_mafia.add(0,7,0,"20명");
        MenuItem rule_description = menu.add(0,8,0,"게임설명");
        MenuItem reset_menu = menu.add(0,9,0,"재시작");
        MenuItem game_end = menu.add(0,10,0,"게임종료");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return doBTN(item.getItemId());
    }

    public boolean doBTN(int itemId) {

        switch(itemId){
            case 1 :
                Toast.makeText(this,"마피아 게임에 찾아주셔서 감사합니다",Toast.LENGTH_SHORT).show();
                return true;

            case 2 : //경찰 플레이 12명 Main
                call_game_type = 12;
                Intent intent = new Intent(getApplicationContext(), Police12Activity.class);
                intent.putExtra("call_game_type",call_game_type);
                finish();
                startActivityForResult(intent,123);
                return true;


            case 3 : //경찰 플레이 16명
                call_game_type = 16;
                Intent intent_16people = new Intent(getApplicationContext() , Police16Activity.class);
                intent_16people.putExtra("call_game_type",call_game_type);
                finish();
                startActivityForResult(intent_16people,123);
                return true;


            case 4 : //경찰 플레이 20명
                call_game_type = 20;
                Intent intent_abc = new Intent(getApplicationContext() , Police20Activity.class);
                intent_abc.putExtra("call_game_type",call_game_type);
                finish();
                startActivityForResult(intent_abc,123);
                return true;

            case 5 :
                return true;
            case 6 :
                return true;
            case 7 :
                return true;
            case 8 : //플레이 룰
                Intent intent_play_rule = new Intent(Police20Activity.this, RuleExplain.class);
                startActivity(intent_play_rule);
                return true;
            case 9 : //재시작
                restart_game(); //게임 변수들 리셋후 재시작
                return true;
            case 10 :
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

        //마피아 찾아다가 이미 죽은 상태로 만들기
        for(int x = 0 ; x < playerAccount ; x++){
            if(man_type[x].equals("마피아")){
                man_death[x] = true; // 이미 죽은 상태setting
                mafia_location.put("mafia"+x,man_num[x]);//마피아1에 마피아 위치를
            }//if_end
        }//for_end

    } //setting()end


   /* public void replace(int i){
        for(int k = 0 ; k <= 11 ; k++){
            man_type[k] = "시민";
        }
        man_type[i] = "마피아";

        man_death[i] = true;
    }*/

    //클릭하면 대상 정체를 밝히고  랜덤하게 한명 죽이자
    class choose_mafia implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            for (int a = 0; a < playerAccount; a++) {
                int cacao =  Police20Activity.this.getResources().getIdentifier("man"+a,"id",getPackageName());
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
            case "시민":
                Toast.makeText(
                        Police20Activity.this
                        ,"대상은 시민입니다."
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

                citizen_AD.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();

                play.setText("대상은 시민 입니다.");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.goodman);
                man_array[i].setClickable(false);
                break;
            case "의사":
                Toast.makeText(
                        Police20Activity.this
                        ,"대상은 의사입니다."
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

                doctor_AD.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();

                play.setText("대상은 의사 입니다.");
                man_array[i].setImageResource(R.drawable.icon_doctor);
                man_turn_over[i] = true;
                man_array[i].setClickable(false);
                break;
            case "마피아":
                Toast.makeText(
                        Police20Activity.this
                        ,"마피아를 찾아냈습니다."
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

                mafia_invest_AD.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })

                        .setCancelable(true)
                        .show();

                mafia_location.remove("mafia"+i);
                Log.i("마피아 상태", mafia_location+"");
                play.setText("대상은 마피아 입니다.");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_mafia_die);
                man_array[i].setClickable(false);
                break;
            case "군인":
                Toast.makeText(
                        Police20Activity.this
                        ,"대상은 군인 입니다."
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

                terrorist.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("대상은 군인 입니다.");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_soldier);
                man_array[i].setClickable(false);
                break;
            case "기자":
                Toast.makeText(
                        Police20Activity.this
                        ,"대상은 기자 입니다."
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
                reporter_invest.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("대상은 기자 입니다.");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_reporter);
                man_array[i].setClickable(false);
                break;
            case "연인":
                Toast.makeText(
                        Police20Activity.this
                        ,"대상은 연인 입니다."
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
                couple_invest_DA.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("대상은 연인 입니다.");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_couple);
                man_array[i].setClickable(false);
                break;
            case "테러리스트":
                Toast.makeText(
                        Police20Activity.this
                        ,"대상은 테러리스트 입니다."
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
                t_invest_DA.setTitle("[수사결과]")
                        .setIcon(R.drawable.icon_police)
                        .setMessage((Integer.parseInt(man_num[i])+1)+"번째 사람은"+man_type[i]+"이였습니다.\n 참혹한 실수 입니다. 마피아는 이제 정체가 드러난 테러리스트를 조용히 제거할것입니다.")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                night_time.start();
                            }
                        })
                        .setCancelable(true)
                        .show();
                play.setText("대상은 테러리스트 입니다.");
                man_turn_over[i] = true;
                man_array[i].setImageResource(R.drawable.icon_terror);
                man_array[i].setClickable(false);
                break;
        }

        int ply = 0 ;
        mafia_kill();
        for(int reporter = 0 ; reporter > playerAccount ; reporter++){
            if(man_type[reporter].equals("기자")){
                ply = Integer.parseInt(man_num[reporter]);
                if(camera == 2 && man_death[ply] == false && day == 3){
                    //카메라 사용한적 없으면 2 && 기자 생존시 && 셋째날일때
                    reporter_shot(); //기자 메소드 실행후 camera = 1; 되서 한번만 실행한다.
                }
            }
        }
        Log.i("배열", Arrays.toString(man_type));
        day++;
        Log.i("오늘 몇일?",""+day);
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


    //연인이 할일
    public void bodyguard(int kill_target, int bodyGuarder) {//마피아의 총맞는 사람 , 연인 위치
        iv = new ImageView(getApplicationContext());
        int bodyguardTarget = (int) (Math.random() * playerAccount); // 연인이 지킬 사람(랜덤1~11)
        Log.i("지킬대상",bodyguardTarget+"");
        if (kill_target == bodyguardTarget && man_death[bodyguardTarget] == true &&
                !(man_type[bodyguardTarget].equals("마피아")) && !(man_type[bodyguardTarget].equals("연인"))) {
            soundf.play(couple_die_sound,1,1,1,0,1);
            // 마피아 죽일대상 = 연인 지킬대상 같으면서 지킬대상의 상태가 이미 총맞아 죽은상태인경우
            // 맞기 전에 행동하는게 아니라 마피아는 쏴서 죽였지만 되살린뒤에 연인가 대신 죽게끔 해놓음..
            switch (man_type[kill_target]){ // 그래서
                case "시민" :
                    iv.setImageResource(R.drawable.couple_break);
                    AlertDialog.Builder couple_citizen = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_citizen.setTitle("[연인의 희생]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"번째 사람은"+man_type[bodyGuarder]+"이였습니다.\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"번에 위치한 사랑하는 연인을 대신하여 사망하였습니다.")
                            .setPositiveButton("닫기", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //시민 되살리고
                    man_array[kill_target].setImageResource(R.drawable.goodman); //시민 살아있는 그림적용
                    break;
                case "의사" :
                    iv.setImageResource(R.drawable.couple_break);
                    AlertDialog.Builder couple_doctor = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_doctor.setTitle("[연인의 희생]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"번째 사람은"+man_type[bodyGuarder]+"이였습니다.\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"번에 위치한 사랑하는 연인을 대신하여 사망하였습니다.")
                            .setPositiveButton("닫기", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //시민 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_doctor);
                    break;
                case "테러리스트" :
                    AlertDialog.Builder couple_terror = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_terror.setTitle("[연인의 희생]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"번째 사람은"+man_type[bodyGuarder]+"이였습니다.\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"번에 위치한 사랑하는 연인을 대신하여 사망하였습니다.")
                            .setPositiveButton("닫기", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //시민 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_terror);
                    break;
                case "기자" :
                    AlertDialog.Builder couple_reporter = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_reporter.setTitle("[연인의 희생]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"번째 사람은"+man_type[bodyGuarder]+"이였습니다.\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"번에 위치한 사랑하는 연인을 대신하여 사망하였습니다.")
                            .setPositiveButton("닫기", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //시민 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_reporter);
                    break;
                case "군인" :
                    AlertDialog.Builder couple_soldier = new AlertDialog.Builder(this).setView(iv);  // 문법 한줄
                    couple_soldier.setTitle("[연인의 희생]")
                            .setIcon(R.drawable.icon_couple)
                            .setMessage((Integer.parseInt(man_num[bodyGuarder])+1)+"번째 사람은"+man_type[bodyGuarder]+"이였습니다.\n" +
                                    (Integer.parseInt(man_num[bodyguardTarget])+1)+"번에 위치한 사랑하는 연인을 대신하여 사망하였습니다.")
                            .setPositiveButton("닫기", null)
                            .setCancelable(true)
                            .show();
                    man_death[kill_target] = false; //시민 되살리고
                    man_array[kill_target].setImageResource(R.drawable.icon_soldier);
                    break;
            }
            //살인대상 == 연인대상 && 살인대상이 생존시 && 보호대상 != 보호자
            man_death[bodyGuarder] = true; // 보호자가 죽는다.
            man_array[bodyGuarder].setClickable(false); //연인 클릭불가
            man_turn_over[bodyGuarder] = true; //연인의 카드는 뒤집혔다.
            String abc = man_type[kill_target].toString();
            play.setText(abc+"를 위해 연인이 희생하였습니다..");
            man_array[bodyGuarder].setImageResource(R.drawable.icon_couple_die);
            Log.i("보호대상 : ", "보호대상 : "+man_type[kill_target].toString() + "");
            bodyGuard_life--;
        }

    }//연인가 하는 일 끝

    public void citizen_death(){
        iv.setImageResource(R.drawable.someone_s_death);
        AlertDialog.Builder sitizen_death = new AlertDialog.Builder(this).setView(iv);
        sitizen_death.setTitle("어젯밤 시민 1명이 사망했습니다.")
                .setIcon(R.drawable.icon_mafia)
                .setMessage("모든 시민들이 공포에 떨고 있습니다\n마피아를 잡으세요")
                .setPositiveButton("닫기",null)
                .setCancelable(true)
                .show();
        soundf.play(mafia_shot_sound,1,1,1,0,1);
    }


    public void mafia_kill() {
        iv = new ImageView(getApplicationContext());
        int kill_man = (int) (Math.random() * playerAccount); // 죽일 대상을 지정한다.
        Log.i("초기상태", Arrays.toString(man_death));
        if (man_death[kill_man] == false) {
            switch (man_type[kill_man]) { // 죽일 대상
                case "시민":
                    iv.setImageResource(R.drawable.someone_s_death);
                    AlertDialog.Builder sitizen_death = new AlertDialog.Builder(this).setView(iv);
                    sitizen_death.setTitle("어젯밤 시민 1명이 사망했습니다.")
                            .setIcon(R.drawable.icon_mafia)
                            .setMessage("모든 시민들이 공포에 떨고 있습니다\n마피아를 잡으세요")
                            .setPositiveButton("닫기",null)
                            .setCancelable(true)
                            .show();
                    soundf.play(mafia_shot_sound,1,1,0,0,1);
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.goodman_die);
                    play.setText("밤에 시민이 살해당했습니다.");
                    Log.i("마피아의 제거대상 : ", String.valueOf(kill_man) + "번째 사람");
                    Log.i("배열", Arrays.toString(man_death));
                    break;
                case "연인":
                    citizen_death();
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.icon_couple_die);
                    Log.i("마피아의 제거대상 : ", String.valueOf(kill_man) + "번째 사람");
                    Log.i("배열", Arrays.toString(man_death));
                    play.setText("밤에 연인가 살해당했습니다.");
                    break;
                case "의사":
                    citizen_death();
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.icon_doctor_die);
                    play.setText("밤에 의사가 살해당했습니다.");
                    Log.i("마피아의 제거대상 : ", String.valueOf(kill_man) + "번째 사람");
                    Log.i("배열", Arrays.toString(man_death));
                    break;
                case "군인":
                    if(solider_life == 2){
                        soundf.play(soldier_sound,1,1,3,0,1);
                        iv = new ImageView(getApplicationContext());
                        iv.setImageResource(R.drawable.soldier_tanking);
                        AlertDialog.Builder soldier_AD = new AlertDialog.Builder(this).setView(iv);
                        soldier_AD.setTitle("군인이 공격을 버텨냈습니다 !!!")
                                .setIcon(R.drawable.icon_soldier)
                                .setMessage("군인은 정체가 드러났고 다시 한번 공격받으면 죽게될 것입니다.")
                                .setPositiveButton("닫기",null)
                                .setCancelable(true)
                                .show();
                        man_array[kill_man].setClickable(false); //군인 클릭불가
                        man_array[kill_man].setImageResource(R.drawable.icon_soldier_shoted); //군인정체공개
                        man_turn_over[kill_man] = true; //군인의 카드는 뒤집혔다.
                        Log.i("마피아가 군인 공격 : ", String.valueOf(kill_man) + 1 +"번 칸에 위치");
                        Log.i("배열", Arrays.toString(man_death));
                        play.setText("마피아의 총을 맞고도 군인은 죽지 않았습니다.");
                        solider_life--;
                        break;
                    }
                    else if(solider_life == 1){
                        iv.setImageResource(R.drawable.someone_s_death);
                        AlertDialog.Builder soldier_AD = new AlertDialog.Builder(this).setView(iv);
                        soldier_AD.setTitle(man_type[kill_man]+"이 사망 하였습니다.")
                                .setIcon(R.drawable.icon_mafia)
                                .setMessage("마피아는 부상을 입은 군인에게 \n최후의 한발을 쏘았습니다.")
                                .setPositiveButton("닫기",null)
                                .setCancelable(true)
                                .show();
                        man_death[kill_man] = true; //죽인다
                        man_array[kill_man].setClickable(false);
                        man_array[kill_man].setImageResource(R.drawable.icon_soldier_die);
                        play.setText("밤에 군인이 살해당했습니다.");
                        Log.i("마피아의 제거대상 : ", String.valueOf(kill_man) + "번째 사람");
                        Log.i("배열", Arrays.toString(man_death));
                        break;
                    }
                case "기자":
                    citizen_death();
                    man_death[kill_man] = true; //죽인다
                    man_turn_over[kill_man] = true; //뒤집기
                    man_array[kill_man].setClickable(false);
                    man_array[kill_man].setImageResource(R.drawable.icon_reporter_die);
                    play.setText("밤에 기자가 살해당했습니다.");
                    Log.i("마피아의 제거대상 : ", String.valueOf(kill_man) + "번째 사람");
                    Log.i("배열", Arrays.toString(man_death));
                    break;
                case "테러리스트":
                    int mafia_loca = 0;

                    for(int zzz = 0 ; zzz < playerAccount ; zzz++){
                        if(mafia_location.containsKey("mafia"+zzz)){ // mafia


                            String a = mafia_location.get("mafia"+zzz); //mafia+zzz의 value를 받아다
                            mafia_loca = Integer.parseInt(a); // 인티저해서 숫자로

                            if(explosion(kill_man)){ //테러리스트가 마피아를 경계할때
                                Log.i("explosion(kill_man)",""+explosion(kill_man));
                                soundf.play(terror_sound,1,1,3,0,1);
                                iv.setImageResource(R.drawable.terrorist_explosion); // 이미지뷰 iv에 이미지 넣기
                                AlertDialog.Builder explosion = new AlertDialog.Builder(this).setView(iv);
                                explosion.setTitle("테러리스트 !!!") // 다이어로그에 타이틀 정하기
                                        .setIcon(R.drawable.icon_terror) //아이콘 정하기
                                        .setPositiveButton("닫기",null)
                                        .setMessage("마피아를 경계하던 테러리스트가"+(Integer.parseInt(man_num[mafia_loca])+1)+"에 위치한"+man_type[mafia_loca]+"와 함께 폭발하였습니다.]")
                                        .setCancelable(true)
                                        .show();

                                Log.i("mafia위치",mafia_loca+"");
                                man_array[mafia_loca].setImageResource(R.drawable.icon_mafia_die); //마피아 죽은 상태로 전환(이미지)
                                mafia_location.remove("mafia"+zzz);


                                man_array[kill_man].setImageResource(R.drawable.icon_terror_die); //테러리스트 죽은 상태로 전환(이미지)
                                man_death[kill_man] = true; //테러 죽은 상태로 전환(목숨)
                                man_turn_over[kill_man] = true; //테러 카드 뒤집힌 상태
                                man_array[kill_man].setClickable(false); //죽은 테러 클릭불가
                                iv = new ImageView(getApplicationContext());
                                Log.i("마피아 상태", ""+mafia_location);
                                break;
                            }else{ //그게 아니면 마피아가 쐇을때 그냥 조용히 죽는것..
                                citizen_death();
                                man_death[kill_man] = true; //죽인다
                                man_turn_over[kill_man] = true; //뒤집기
                                man_array[kill_man].setClickable(false);
                                man_array[kill_man].setImageResource(R.drawable.icon_terror_die);
                                play.setText("밤에 테러리스트가 살해당했습니다.");
                                Log.i("마피아의 제거대상 : ", String.valueOf(kill_man) + "번째 사람");
                                Log.i("배열", Arrays.toString(man_death));
                                break;
                            }//else_end
                        }//if_end
                    }//for_end
            }//switch end
        }else{
            //이미 죽은 시체를 쏘면
            Log.i("mafia_kill_else문", "시체쏘기");
            mafia_kill();  //재귀함수 쓰면 안될꺼같긴한데 모르겠다 에라.. 다시 쏴라
            return;
        }
        iv = new ImageView(getApplicationContext());

        int bodynum = 0; // 연인 위치를 찾아서

        for(int a = 0 ; a < playerAccount ; a++ ){
            if(man_type[a].equals("연인")){
                bodynum = (Integer.parseInt(man_num[a]));
                if(man_death[a] == false) { //연인가 살아있으면
                    bodyguard(kill_man, bodynum); // 연인 메소드 실행
                }
            }
        }

        int ply = 0 ;
        for(int reporter = 0 ; reporter < playerAccount ; reporter++){
            if(man_type[reporter].equals("기자")){
                ply = Integer.parseInt(man_num[reporter]);
                Log.i("기자는 몇번에 위치해 있나?",""+man_num[ply]);
                if(man_death[ply] == false && camera == 2 && day > 2){
                    //기자 생존 && 취재 1번만 가능 && 첫째날 취재불가
                    reporter_shot(); //기자 메소드 실행후 camera = 1; 되서 한번만 실행한다.
                }
            }
        }



        //연인 생명상태가 2면
        Log.i("연인 위치",bodynum+"");
        Log.i("이번에 죽는 사람",kill_man+"");

        Log.i("말기 상태", Arrays.toString(man_death));
        Log.i("카드 뒤집힌상태", Arrays.toString(man_turn_over));
    } // 마피아가 사람 죽이는 메소드


    //기자가 할일
    public void reporter_shot(){
        soundf.play(camera_sound,1,1,1,0,1);
        iv = new ImageView(getApplicationContext());
        int reporting_target = (int)(Math.random() * playerAccount);
        switch(man_type[reporting_target]){
            case "시민" :
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
                citizen.setTitle("[기레기 주의!]") // 다이어로그에 타이틀 정하기
                        .setIcon(R.drawable.goodman) //아이콘 정하기
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setIcon(R.drawable.goodman)
                        .setPositiveButton("닫기",null)
                        .setCancelable(false)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.goodman);
                man_turn_over[reporting_target] = true;
                   /* ((ViewGroup)iv.getParent()).removeView(iv);*/
                break;
            case "마피아" :
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
                mafia.setTitle("[기자의 특종]")
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setIcon(R.drawable.icon_mafia)
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView play = findViewById(R.id.play);
                                play.setText("기자가 마피아를 찾아냈습니다 !!!");
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
            case "의사" :
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

                doctor.setTitle("[기자의 특종]")
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setIcon(R.drawable.icon_doctor)
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
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
            case "군인" :
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
                soldier.setTitle("[기자의 특종]")
                        .setIcon(R.drawable.icon_soldier)
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
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
            case "테러리스트" :
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

                terrorist.setTitle("[기자의 특종]")
                        .setIcon(R.drawable.icon_terror)
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                play.setText("테러리스트의 정체가 밝혀졌습니다.");
                            }
                        })
                        .setCancelable(true)
                        .show();
                man_array[reporting_target].setClickable(false);
                man_array[reporting_target].setImageResource(R.drawable.icon_terror);
                man_turn_over[reporting_target] = true;
                break;
            case "기자" :
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

                reporter.setTitle("[기자의 특종]")
                        .setIcon(R.drawable.icon_reporter)
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
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
            case "연인" :
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

                couple.setTitle("[기자의 특종]")
                        .setIcon(R.drawable.icon_couple)
                        .setMessage("기자는 온 세상에 알립니다..\n"+(Integer.parseInt(man_num[reporting_target])+1)+"번째 사람이"+man_type[reporting_target]+"이란걸 말이죠")
                        .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
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
    }//기자 끝







    public boolean explosion(int km){
        int warning = (int)(Math.random() * playerAccount); //테러리스트 랜덤 지목대상
        int terror_locaton = 0;
        for(int f = 0 ; f < playerAccount ; f++){
            if(man_type[f].equals("테러리스트")){
                terror_locaton = Integer.parseInt(man_num[f]);
                //테러리스트 위치
            }
        }

        //warning == terror_location 맞추면 발동하는게 맞긴한데.. 확률이 너무 낮아서 그냥
        // 정체만 안들어난 상태면 마피아가 쏘면 터지게..
        if(man_turn_over[terror_locaton] == false){
            //그냥 테러 정체 숨겨져 있으면 발동
            return true;
        }
        return false;
    }

    private void restart_game(){
        iv = new ImageView(getApplicationContext());
        for (int x = 0; x < playerAccount; x++) {
            setting(); // 셔플로 패섞기 ,  모든 생명 부활 , 마피아 찾아서 죽여놓기 ,
            man_array[x].setImageResource(R.drawable.icon_citizen); // 모든 버튼이미지 초기화
            man_array[x].setClickable(true); // 클릭막은걸 풀어준다.
            man_turn_over[x] = false; // 뒤집힘을 리셋한다.
        }
        solider_life = 2; // 군인 목숨을 2개로 만든다.
        camera = 2; // 리셋시 기자 취재 가능
        day = 1; //재시작시 첫날부터 시작
        mafia_location.clear(); //마피아 위치 초기화
        death_man = 0;
        for(int x = 0 ; x < playerAccount ; x++){
            if(man_type[x].equals("마피아")){
                man_death[x] = true; // 이미 죽은 상태setting
                mafia_location.put("mafia"+x,man_num[x]);//마피아1에 마피아 위치를
            }//if_end
        }//for_end
        Log.i("리셋 마피아 배치 ", ""+mafia_location);
        play.setText("게임을 시작하지");
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
            //마피아 다 죽어버렸으면
            Intent game_over_message = new Intent(getApplicationContext(),GameOverActivity.class);
            user_game_result = true;
            game_over_message.putExtra("user_game_result",user_game_result);
            game_over_message.putExtra("man_type",man_type);
            game_over_message.putExtra("playerAccount",playerAccount);
            game_over_message.putExtra("death_man",death_man-2);// 죽은 사람 숫자
            /*startActivity(game_over_message); 반환값이 없을때 사용하는 방법*/
            startActivityForResult(game_over_message,123); // (보낼 인텐트,택배받을때 인증서명);
            Log.i("마피아 패배" , "시민팀 승리");
        }
        //패배기준
        //패 다 깠는데 마피아가 다 한명이라도 살아있는 경우
        /*if(man_turn_over.toString().contains("false")){

        }*/



        Log.i("죽은자는 몇명입니까?" , death_man-2+"");
        if(!(mafia_location.isEmpty()) && death_man == 12){ //마피아가 1명이라도 살아있는데 사망자가 12명(시민10명+마피아2명)명일경우
            Intent game_over_message = new Intent(getApplicationContext(),GameOverActivity.class);
            game_over_message.putExtra("user_game_result",user_game_result); //게임 결과 true or false
            game_over_message.putExtra("man_type",man_type);// 게임 인원 구성
            game_over_message.putExtra("playerAccount",playerAccount);// 게임플레이어 숫자
            game_over_message.putExtra("death_man",death_man-2);// 죽은 사람 숫자
            /*startActivity(game_over_message); 반환값이 없을때 사용하는 방법*/
            startActivityForResult(game_over_message,123); // (보낼 인텐트,택배받을때 인증서명);
            Log.i("마피아 승리" , "시민팀 패배");
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
            Toast.makeText(this,"12에서 12 그리기",Toast.LENGTH_SHORT);
            Intent intent_12 = new Intent(getApplicationContext(), Police12Activity.class);
            intent_12.putExtra("call_game_type",call_game_type);
            startActivityForResult(intent_12,123);
        }
        if(requestCode == 123 && resultCode == 777 && playing == 16){
            Toast.makeText(this,"12에서 16 그리기",Toast.LENGTH_SHORT);
            Intent intent_16 = new Intent(getApplicationContext(), Police12Activity.class);
            intent_16.putExtra("call_game_type",call_game_type);
            startActivityForResult(intent_16,123);
        }
        if(requestCode == 123 && resultCode == 777 && playing == 20){
            Toast.makeText(this,"12에서 20 그리기",Toast.LENGTH_SHORT);
            Intent intent_20 = new Intent(getApplicationContext() , Police20Activity.class);
            intent_20.putExtra("call_game_type",call_game_type);
            startActivityForResult(intent_20,123);
        }
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        night_time.pause();
        soundf.autoPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        night_time.start();
        super.onResume();
    }

}// 끝







