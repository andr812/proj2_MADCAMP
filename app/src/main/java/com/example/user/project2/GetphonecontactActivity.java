package com.example.user.project2;


import android.support.v7.app.AppCompatActivity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class GetphonecontactActivity extends AppCompatActivity{
    EditText et;
    EditText et2;
    Button btn_to_server;
    Button btn_to_client;
    Button book;
    TextView tv_json;
//    TextView tv_parsing;
    StringBuffer sb = new StringBuffer();
    ArrayList<String> nameList=new ArrayList<>();
    ArrayList<String> pnumberList=new ArrayList<>();
    Iterator it = nameList.iterator();
    Iterator it2 = nameList.iterator();
    ArrayList <ContentProviderOperation> ops = new ArrayList <ContentProviderOperation> ();
    String Name;
    String Number;
    String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getphonecontact);

        et = (EditText)findViewById(R.id.et);
        et2=(EditText)findViewById(R.id.et2);
        btn_to_server = (Button)findViewById(R.id.btn_to_server);
        btn_to_client = (Button)findViewById(R.id.btn_to_client);
        book = (Button)findViewById(R.id.book);
        tv_json = (TextView)findViewById(R.id.tv);
//        tv_parsing = (TextView)findViewById(R.id.tv_parsing);
        btn_to_server.setOnClickListener(myClickListener);
        btn_to_client.setOnClickListener(myClickListener);
        book.setOnClickListener(myClickListener);
    }

    View.OnClickListener myClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btn_to_server :
                    String startJson = "[";
                    String endJson ="]";

                    if(!sb.toString().equals(""))
                    {
                        sb.append(",");
                    }
                    temp = "{\"이름\""+":"+"\""+et.getText().toString()+"\""+ ","
                            + "\"전화번호\""+":" + "\"" + et2.getText().toString() + "\"" + "}";
                    sb.append(temp);
                    tv_json.setText(startJson+sb+endJson);
                    break;

                case R.id.btn_to_client :
                    try
                    {
                        JSONArray jsonArray = new JSONArray(tv_json.getText().toString());
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            nameList.add(jsonObject.getString("이름"));
                            pnumberList.add(jsonObject.getString("전화번호"));

                        }
                        //String buffer flush
                        sb.setLength(0);
                        temp="";
                        for(int i=0;i<nameList.size();i++){
                            Name=nameList.get(i);
                            Number=pnumberList.get(i);
                            ops.add(ContentProviderOperation.newInsert(
                                    ContactsContract.RawContacts.CONTENT_URI)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                    .build());
                            //------------------------------------------------------ Names
                            if (Name != null) {
                                ops.add(ContentProviderOperation.newInsert(
                                        ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE,
                                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                        .withValue(
                                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                                Name).build());
                            }

                            //------------------------------------------------------ Number
                            if (Number != null) {
                                ops.add(ContentProviderOperation.
                                        newInsert(ContactsContract.Data.CONTENT_URI)
                                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                        .withValue(ContactsContract.Data.MIMETYPE,
                                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, Number)
                                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                        .build());
                            }
                            // Asking the Contact provider to create a new contact
                            try {
                                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//                                tv_parsing.setText("Succeed");
                                ops.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    break;
                case  R.id.book:
                    Intent intent= new Intent(v.getContext(), ContentProviderActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    };
}