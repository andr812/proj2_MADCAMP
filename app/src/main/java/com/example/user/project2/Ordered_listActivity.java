package com.example.user.project2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by user on 2018-01-03.
 */

public class Ordered_listActivity extends AppCompatActivity {

    private String user;
    private ArrayList<ArrayList<String>> foodlist= new ArrayList<ArrayList<String>>();

//    public Ordered_listActivity(){
//        OrderActivity tmp;
//        tmp=new OrderActivity();
//        this.foodlist=tmp.foodlist2;
//        this.user=tmp.USER;
//        Log.i("foodlist2!!!!", tmp.foodlist2.toString());
//    }


    ArrayList<Map<String, String>> DATALIST = new ArrayList<Map<String, String>>();
    TextView foodtxt, resulttxt, hiddentxt;
    ImageView layout2;
    String restaurant;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordered_list);
        Intent intent= getIntent();
        foodlist=(ArrayList<ArrayList<String>>) intent.getSerializableExtra("list");
        user=intent.getExtras().getString("name");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("강정이 기가 막혀", "0424729808" );
        map.put("마루", "0428229281" );
        map.put("훌랄라 치킨", "0428635577" );
        map.put("미스터 보쌈", "0428635379" );
        map.put("자니스펍", "0428610800" );

        foodtxt = (TextView)findViewById(R.id.foodtext);
        resulttxt = (TextView)findViewById(R.id.resulttext);
        hiddentxt = (TextView)findViewById(R.id.hiddentext);
        layout2 = (ImageView) findViewById(R.id.layout);
        String chosenman = new String();

        try {
            DATALIST = new HttpCall.foodGET().execute("http://52.79.128.200:3000/api/orders").get(); //chosename, ordernum 존재
            Log.i("datalistlistlist", DATALIST.toString());

            int foo2 = Integer.parseInt(DATALIST.get(DATALIST.size()-1).get("ordernum"));
            if( foo2 != 0 ){
                layout2.setVisibility(View.VISIBLE);
                resulttxt.setVisibility(View.VISIBLE);
                hiddentxt.setVisibility(View.VISIBLE);

                ArrayList<Integer> arrayList = new ArrayList<Integer>();
                for(int i=0; i<5; i++){
                    arrayList.add(0);
                }

                for(int j=0; j<DATALIST.size(); j++) //DATALIST.get(j).get("ordernum")
                {
                    for(int k=0; k<5; k++){
                        if( Integer.parseInt(DATALIST.get(j).get("ordernum")) == (k+1) ){
                            int m = arrayList.get(k);
                            arrayList.set(k, m+1);
                        }
                    }
                    if( Integer.parseInt(DATALIST.get(j).get("index")) == 1 ){
                        chosenman = DATALIST.get(j).get("name");
                    }
                }
                int max = 0;
                for(int j=0; j<5; j++){
                    if(arrayList.get(j) > arrayList.get(max)){
                        max = j;
                    }
                }
                Log.i("FOODLIST", String.valueOf(foodlist));
                //이제 max+1 = index of maximum voted food!
                for(int i=0; i<5; i++){
                    if( Integer.parseInt(foodlist.get(i).get(1)) == (max+1) ){
                        restaurant = foodlist.get(i).get(0);
                    }
                }

                foodtxt.setText( restaurant );
                resulttxt.setText( chosenman );
                if( chosenman == user ){
                    hiddentxt.setText( map.get( restaurant ) );
                }

                int max2 = max+1;
                if( max2 == 1){
                    layout2.setImageResource(R.drawable.food1);
                }
                if( max2 == 2){
                    layout2.setImageResource(R.drawable.food2);
                }

                if( max2 == 3){
                    layout2.setImageResource(R.drawable.food3);
                }

                if( max2 == 4){
                    layout2.setImageResource(R.drawable.food4);
                }

                if( max2 == 5){
                    layout2.setImageResource(R.drawable.food5);
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
