package com.example.user.mafia_game;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GameOverActivity extends AppCompatActivity {

    MediaPlayer mp3;
    TextView result_text,textShow;
    Button check_result;
    EditText edit_name;
    View page1,page2;
    URL url;
    /*자바에서 네트워크를 배웠으면 설명안해도되는데 자바때 FileIO배우고 소켓통신 안배웠으니 설명필요
    URL은 인터넷주소에 대한 정보를 가지고 있는 객체이다. 요청 보낼 주소의 정보를 가진 객체
    기능마다 요청 주소가 다를테니 미리 선언
    */
    HttpURLConnection con;
    /*커넥션은 연결 URL은 보낼 주소 , 웹으로 URL주소에 연결하는 객체
    * 해당하는 URL주소에 연결하게끔 도와주는 객체
    * 연결하려면 바로 주소로 갈수는없고 연결해주는 객체가 필요한데 그게 바로 이 커넥션객체*/
    ImageView[] man_array; //플레이어 이미지 보여줄 뷰
    String[] man_type; // 플레이어 직업 배열
    TextView etView; // 결과창 & 입력창
    int player_account; // 플레이어수
    String game_result =""; // 게임 결과
    int death_man; // 죽은사람 수
    boolean x;
    String win_or_lose ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player_account = getIntent().getIntExtra("playerAccount", 0); //플레이어수
        man_array = new ImageView[player_account];
        man_type  = new String[player_account];
        TableRow cut1,cut2;

            setContentView(R.layout.activity_gameover);


        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        result_text = findViewById(R.id.result_text);
        check_result = findViewById(R.id.check_result);
        cut1 = findViewById(R.id.line_cut1);
        cut2 = findViewById(R.id.line_cut2);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        edit_name = findViewById(R.id.edit_name);

        death_man = getIntent().getIntExtra("death_man",0);
        x  = getIntent().getBooleanExtra("user_game_result",false);
        if(x == false){
            mp3 = MediaPlayer.create(getApplicationContext(), R.raw.lose_leshphon);
            mp3.start();
            win_or_lose = "敗北";
        }else{
            mp3 = MediaPlayer.create(getApplicationContext(), R.raw.victory);
            mp3.start();
            win_or_lose = "勝利";
        }
        man_array = new ImageView[player_account];
        man_type  = new String[player_account];

        textShow = findViewById(R.id.textShow);
        Log.i("プレイヤー数:",""+player_account);
        Log.i("死亡者数:",""+death_man);
        Log.i("勝負結果:",""+win_or_lose);

        man_type = getIntent().getStringArrayExtra("man_type");

        if(player_account == 12 ){
            cut1.setVisibility(View.INVISIBLE);
            cut2.setVisibility(View.INVISIBLE);
        }

        if(player_account == 16 ){
            cut2.setVisibility(View.INVISIBLE);
        }

        for (int a = 0; a < player_account; a++) {
            int id = getResources().getIdentifier("man" + a, "id", getPackageName());
            man_array[a] = findViewById(id);
        }


        for(int x = 0 ; x < player_account ; x++){
            switch(man_type[x]){
                case "市民" : man_array[x].setImageResource(R.drawable.icon_citizen);
                    break;
                case "マフィア" : man_array[x].setImageResource(R.drawable.icon_mafia2);
                    break;
                case "軍人" : man_array[x].setImageResource(R.drawable.icon_soldier2);
                    break;
                case "医者" : man_array[x].setImageResource(R.drawable.icon_doctor2);
                    break;
                case "恋人" : man_array[x].setImageResource(R.drawable.icon_couple2);
                    break;
                case "テロリスト" : man_array[x].setImageResource(R.drawable.icon_terror2);
                    break;
                case "記者" : man_array[x].setImageResource(R.drawable.icon_reporter2);
                    break;
            }
        }


        if(x == true){
            game_result = "げーむで勝ちました。";
            result_text.setTextColor(Color.BLUE);
            result_text.setText(game_result);
            Toast.makeText(GameOverActivity.this,game_result,Toast.LENGTH_LONG).show();
        }else{
            game_result = "げーむで負けました。";
            result_text.setTextColor(Color.RED);
            result_text.setText(game_result);
            Toast.makeText(GameOverActivity.this,game_result,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        mp3.pause();
        super.onPause();
    }



    public void btnOnClick(View view){
        switch (view.getId()){
            case R.id.check_result :
                Intent gogo_main = new Intent(this,MainActivity.class);
                mp3.stop();
                setResult(RESULT_OK,gogo_main);
                finish();
                break;
            case R.id.btnSend:
                String name = edit_name.getText().toString();
                String death_player = String.valueOf(death_man);
                String all_player = String.valueOf(player_account);

                HashMap<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("result", win_or_lose);
                params.put("death", death_player);
                params.put("player", all_player);

                String param = makeParams(params);

                try{
                    url = new URL("http://10.10.12.163:8888/www/insertMember");
                } catch (MalformedURLException e){
                    Toast.makeText(this,"間違った URLです。", Toast.LENGTH_SHORT).show();
                }

                try{
                    con = (HttpURLConnection) url.openConnection();

                    if(con != null){

                        con.setConnectTimeout(10000);  //연결제한시간. 0은 무한대기.
                        con.setUseCaches(false);      //캐쉬 사용여부
                        con.setRequestMethod("POST"); /*  URL 요청에 대한 메소드 설정 : POST
                         이게 붙어있다는건 스프링에서도 POST 처리를 해줘야한다는걸 의미 */
                        con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                        con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8");

                        OutputStream os = con.getOutputStream(); /* OutputSteams 연결된 커넥션에서 가져온다. 연결될때 아웃풋도 같이 연결되서
                        데이터도 같이 가는것이다. 커넥션으로부터 겟아웃풋스트림함수 사용 */
                        os.write(param.getBytes("UTF-8")); // 단말기와 웹서버 사이의 전송 인코딩 동일하게 씀
                        os.flush(); //내보내고
                        os.close(); //닫고까지 하면 데이터를 보낸 것이다.

                        //받아서 DB에 저장하고 저장확인 리턴 받아서 1이냐0이냐 확인해서 성공실패알림
                        /*con.getResponseCode() 이게 200(정샹)이냐 404냐 파악하는것*/
                        if(con.getResponseCode() == HttpURLConnection.HTTP_OK){

                            /*인풋스트림 꺼내서 UTF-8형식으로 받아올건데 reader를 통해서 통로 연결 */
                            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                            String line;
                            String page = "";

                            while ((line = reader.readLine()) != null){
                                page += line;
                            }

                            Toast.makeText(this, page, Toast.LENGTH_SHORT).show();

                        }

                    }
                }catch (Exception e){
                    Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                } finally {
                    if(con != null){
                        con.disconnect();// 사용종료후 처리
                    }
                }
                break;
            case R.id.btnParse:
                //출력버튼
                try{                                          //8888 톰캣 www은 마지막 패키지  명칭 맨 뒤쪽은 리퀘스트매핑
                    url = new URL("http://10.10.12.163:8888/www/selectMember");
                } catch (MalformedURLException e){ //이걸 왜 처리하는가? 나중에 변수처리해서 URL 받을때 URL형식이 아니면 발생
                    Toast.makeText(this,"間違った URLです。", Toast.LENGTH_SHORT).show();
                }
                //주소도 알앗겠다 본격접속
                try{
                    /*커넥션 객체는 URL로부터 오픈한다. 내가 연결하려는데에 연결해야하니까 내가 가려는 주소를 가진게
                    유알엘이니까 이 url통해 연결가능한 객체를 받아온것이다.내가 가고싶은데서 정보를 받아서*/
                    con = (HttpURLConnection) url.openConnection();

                    if(con != null){
                        con.setConnectTimeout(10000);  //연결제한시간. 0은 무한대기.  10초
                        con.setUseCaches(false);      //캐쉬 사용여부
                        //포스트가 없으니 겟방식
                        con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                        con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                        if(con.getResponseCode() == HttpURLConnection.HTTP_OK){

                            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                            String line;
                            String page = "";

                            while ((line = reader.readLine()) != null){
                                page += line;
                            }
                            jsonParse(page);
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
                break;
        }
    }

    //이름1=값1&이름2=값2&이름3=값3 형태로 변환시켜주는 함수
    public String makeParams(HashMap<String,String> params){
        StringBuffer sbParam = new StringBuffer();
        String key = "";
        String value = "";
        boolean isAnd = false; //HashMap의 담겨온 파라메터의 갯수가 2개 이상/이하 판단하는 변수

        //hashMap 반복문 돌리는것 해쉬맵은 반복돌리면 뭐가 먼저 나올지 모름
        //처음부터 끝까지 나와야할때 사용 9 뭐가 나올진 모르지만
        for(Map.Entry<String,String> elem : params.entrySet()){
            key = elem.getKey();
            value = elem.getValue();

            if(isAnd){
                sbParam.append("&"); // 컨캣과 같다.
            }

            sbParam.append(key).append("=").append(value); /*num = 1*/

            if(!isAnd){
                if(params.size() >= 2){
                    isAnd = true;
                }
            }
        }

        return sbParam.toString();
    }

    public void jsonParse(String page){
        JSONArray jarray = null; // 제이슨 형태의 데이터가 여러개 있는것
        JSONObject item = null; // 제이슨 형태의 회원정보 갯수

        try {
            jarray = new JSONArray(page);


            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < jarray.length(); i++) {
                item = jarray.getJSONObject(i);
                sb2.append(" ユーザー:");
                sb2.append(item.getString("name"));
                sb2.append(" ゲーム結果:");
                sb2.append(item.getString("result"));
                sb2.append("志望者:");
                sb2.append(item.getInt("death"));
                sb2.append("難易度:");
                sb2.append(item.getInt("player"));
                sb2.append("\n");
            }
            textShow.setText(sb2.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







}