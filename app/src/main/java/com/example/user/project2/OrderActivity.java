package com.example.user.project2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018-01-03.
 */

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayAdapter<String> listAdapter;
    public ArrayList<ArrayList<String>> foodlist2 = new ArrayList<ArrayList<String>>();
    public String USER = new String();
    ArrayList<String> inner = new ArrayList<String>();
    ArrayList<String> nameslist = new ArrayList<String>();
    Button votebtn, revotebtn, resultbtn;
    EditText nametxt;
    private ArrayList result = new ArrayList();

    public ArrayList<ArrayList<String>> getfoodlist2(){
        return foodlist2;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        ArrayList<String> inner1 = new ArrayList<String>();
        inner1.add("강정이 기가 막혀");
        inner1.add("1");
        foodlist2.add(inner1);
        nameslist.add("강정이 기가 막혀");

        ArrayList<String> inner2 = new ArrayList<String>();
        inner2.add("마루");
        inner2.add("2");
        foodlist2.add(inner2);
        nameslist.add("마루");

        ArrayList<String> inner3 = new ArrayList<String>();
        inner3.add("훌랄라 치킨");
        inner3.add("3");
        foodlist2.add(inner3);
        nameslist.add("훌랄라 치킨");

        ArrayList<String> inner4 = new ArrayList<String>();
        inner4.add("마스터 보쌈");
        inner4.add("4");
        foodlist2.add(inner4);
        nameslist.add("미스터 보쌈");

        ArrayList<String> inner5 = new ArrayList<String>();
        inner5.add("자니스펍");
        inner5.add("5");
        foodlist2.add(inner5);
        nameslist.add("자니스펍");

        Log.d("Foodlist2222222", foodlist2.toString());
        final ListView list = (ListView)findViewById(R.id.foodlist);
        votebtn = (Button)findViewById(R.id.votebtn);
        votebtn.setOnClickListener(this);
        revotebtn = (Button)findViewById(R.id.revotebtn);
        revotebtn.setOnClickListener(this);
        resultbtn = (Button)findViewById(R.id.resultbtn);
        resultbtn.setOnClickListener(this);
        nametxt = (EditText)findViewById(R.id.nametxt);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameslist);

        list.setAdapter(listAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Object vo = (Object) parent.getAdapter().getItem(position);
                result.clear();
                result.add(nametxt.getText().toString());
                USER = nametxt.getText().toString();

                result.add(position+1);
                Log.i("Problem!!!!!!!", result.toString());
                //Log.i("Object vo", String.valueOf(result.get(0)));
                //Log.i("position", String.valueOf(result.get(1))); //position+1값을 넘겨주면 될듯 , 아니면 list에서 빼오기
            }
        });
    }

//     if(nametxt.getText().toString().length() != 0) {

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.votebtn:
                Toast.makeText(getApplicationContext(), "Vote Success", Toast.LENGTH_LONG).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", result.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put("ordernum", result.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("CLICKEd", "!!!!!!!!!!!!!!!!!");
                new HttpCall.foodPOST(jsonObject).execute("http://52.79.128.200:3000/api/orders");
                break;

            case R.id.revotebtn:
                Toast.makeText(getApplicationContext(), "Revote Success", Toast.LENGTH_LONG).show();
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject2.put("name", result.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject2.put("ordernum", result.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("CLICKEd", "!!!!!!!!!!!!!!!!!");
                new HttpCall.foodPOST(jsonObject2).execute("http://52.79.128.200:3000/api/orders/:name");
                break;

            case R.id.resultbtn:
               Intent intent=new Intent(getApplicationContext(),Ordered_listActivity.class);
               intent.putExtra("name",USER);
               intent.putExtra("list",foodlist2);
               startActivity(intent);
        }

    }
}
