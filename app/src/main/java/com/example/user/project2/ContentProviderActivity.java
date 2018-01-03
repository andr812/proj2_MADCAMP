package com.example.user.project2;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ContentProviderActivity extends Activity implements View.OnClickListener{

    private ArrayList<Map<String, String>> dataList;
    private ArrayList<Map<String, String>> DATALIST;
    public ListView mListview;
    private FloatingActionButton mBtnAddress, getbutton;
    JSONArray jsonArray = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider); //xml파일
        mListview = (ListView) findViewById(R.id.listview);
        mBtnAddress = (FloatingActionButton) findViewById(R.id.btnAddress);
        mBtnAddress.setOnClickListener(this);
        getbutton = (FloatingActionButton) findViewById(R.id.GETbtn);
        getbutton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddress:

                dataList = new ArrayList<Map<String, String>>();
                Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

                while (c.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    HashMap<String, String> map = new HashMap<String, String>();
                    // 연락처 id 값
                    String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    // 연락처 대표 이름
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                    map.put("name", name);

                    // ID로 전화 정보 조회
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);

                    // 데이터가 있는 경우
                    if (phoneCursor.moveToFirst()) {
                        String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        map.put("phone", number);
                        try {
                            jsonObject.put("name", name);
                            jsonObject.put("phonenum", number);
                            Log.i("HIHIHIHI", jsonObject.toString());
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    phoneCursor.close();
                    dataList.add(map);
                }// end while
                c.close();
//                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
//                        dataList,
//                        android.R.layout.simple_list_item_2,
//                        new String[]{"name", "phone"},
//                        new int[]{android.R.id.text1, android.R.id.text2});
//                mListview.setAdapter(adapter);
                new HttpCall.contactPOST(jsonArray).execute("http://52.79.128.200:3000/api/pbooks");
                break;

            case R.id.GETbtn:
                try {
                    DATALIST = new HttpCall.contactGET().execute("http://52.79.128.200:3000/api/pbooks").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), DATALIST, android.R.layout.simple_list_item_2, new String[]{"name", "phonenum"}, new int[]{android.R.id.text1, android.R.id.text2});
                mListview.setAdapter(adapter);
                break;

        }
    }
}