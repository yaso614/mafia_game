package com.example.user.mafia_game;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        MediaPlayer mediaPlayer,night_time;
        View mainview;
    GestureDetector detector;

    PopupMenu pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //미디어플레이어 사용식
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.main_bgm); //미플 문법
        night_time = MediaPlayer.create(getApplicationContext(), R.raw.night); //미플 문법
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //줄그인애임 위의 사운드풀 동일
        night_time.setAudioStreamType(AudioManager.STREAM_MUSIC); //줄그인애임 위의 사운드풀 동일
        mainview = findViewById(R.id.mainview);

        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                coco();
             }
        });

        mainview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return true;
            }
        });

    }//onCreate_end onCreate_end onCreate_end onCreate_end onCreate_end onCreate_end onCreate_end


    public void coco(){

        pop = new PopupMenu(MainActivity.this, mainview);
        //MainActiviy.this == getApplicationContext()

        MenuInflater inflater = pop.getMenuInflater(); //new로 가져오는게 아닌 이미 있는거 가져옴
        Menu menu = pop.getMenu(); // 비어있는데서 가져왔으니 비어있을 메뉴다.

        inflater.inflate(R.menu.main_popup_menu , menu); //전개 .. 이 코드가 실행되면 (A,B) A를 B에 넣는다.

        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.popup_mafia_1:
                        mainview.setBackgroundResource(R.drawable.mafia_view1);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_1); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_mafia_2:
                        mainview.setBackgroundResource(R.drawable.mafia_view2);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_2); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_mafia_3:
                        mainview.setBackgroundResource(R.drawable.mafia_view3);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_3); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_mafia_4:
                        mainview.setBackgroundResource(R.drawable.mafia_view4);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_4); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_mafia_5:
                        mainview.setBackgroundResource(R.drawable.mafia_view5);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_5); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_citizen:
                        mainview.setBackgroundResource(R.drawable.citizen_view1);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_1); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_couple:
                        mainview.setBackgroundResource(R.drawable.couple_view1);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_2); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_doctor:
                        mainview.setBackgroundResource(R.drawable.doctor_view3);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.doctor_song); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_police:
                        mainview.setBackgroundResource(R.drawable.police_view1);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_4); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_soldier:
                        mainview.setBackgroundResource(R.drawable.soldier_view1);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.soldier_song); //미플 문법
                        mediaPlayer.start();
                        break;
                    case R.id.popup_reporter:
                        mainview.setBackgroundResource(R.drawable.reporter_view1);
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.daytime); //미플 문법
                        mediaPlayer.start();
                        break;
                }
                return false;
            }
        });
//화면에 보여주기 - menu는 popup안에 있으니까. popup출력
        pop.show();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(0,1,0,"マフィアゲーム");

        SubMenu playgame = menu.addSubMenu("ゲームする");
        SubMenu play_police = playgame.addSubMenu("警察で プレー");
        play_police.add(0,2,0,"12名");
        play_police.add(0,3,0,"16名");
        play_police.add(0,4,0,"20名");
        SubMenu play_mafia = playgame.addSubMenu("マフィアでプレー(未具現)");
        play_mafia.add(0,5,0,"12名(未具現)");
        play_mafia.add(0,6,0,"16名(未具現)");
        play_mafia.add(0,7,0,"20名(未具現)");
        MenuItem rule_description = menu.add(0,8,0,"ゲーム説明");
        MenuItem game_end = menu.add(0,9,0,"ゲーム終了");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case 1 : Toast.makeText(this,"マフィアげーむを楽しんで下さい",Toast.LENGTH_SHORT).show();
                return true;
            case 2 : //경찰 플레이 12명
                mediaPlayer.pause();
                Intent intent_12people = new Intent(getApplicationContext(), Police12Activity.class);
                startActivity(intent_12people);
                return true;
            case 3 : //경찰 플레이 16명
                mediaPlayer.pause();
                Intent intent_16people = new Intent(getApplicationContext(), Police16Activity.class);
                startActivity(intent_16people);
                return true;
            case 4 : //경찰 플레이 20명
                mediaPlayer.pause();
                Intent intent_20people = new Intent(getApplicationContext() , Police20Activity.class);
                startActivity(intent_20people);
                return true;
            case 5 :
                return true;
            case 6 :
                return true;
            case 7 :
                return true;
            case 8 : //플레이 룰

                //인텐트로 넘기기
                Intent intent_play_rule = new Intent(MainActivity.this, RuleExplain.class);
                startActivity(intent_play_rule);
                return true;
                //레이아웃 전개자 인플레이터 사용방법
                /*LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                ScrollView sv = (ScrollView) inflater.inflate(R.layout.play_rule , null);
                setContentView( sv );
                return true;*/
            case 9 :
                finish();
                return true;




        }
        return false;
    }


    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();
    }
}// 끝







