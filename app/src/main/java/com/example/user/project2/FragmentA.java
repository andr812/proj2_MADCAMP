package com.example.user.project2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageDownloader;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by user on 2018-01-03.
 */

public class FragmentA extends Fragment{


    private static ArrayList<Contact> contactlist = new ArrayList<>();
    private TextView tv_outPut;

    public static class Contact {
        String name = "Default";
        String link_page = "Default"; //link 받기
    }

    public static ArrayList<Contact> getContactList() {
        return contactlist;
    }

    CallbackManager callbackManager;
    TextView tv_output;
    LinearLayout contactlayout, fblayout;
    ProgressDialog mDialog;
    JSONObject jObject;
    ArrayList<FacebookUserInfo.fbContact> fbcontactlist = new ArrayList<>();
    JSONArray jsonArray = new JSONArray();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.tab2gallery, container, false);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1contact, container, false);

        callbackManager = CallbackManager.Factory.create(); //facebook for developer class관련

        fblayout = rootView.findViewById(R.id.fblayout);

        final LoginButton loginButton = rootView .findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setReadPermissions(Arrays.asList( "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("a", "hello");
                mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage("Retrieving data...");
                mDialog.show();


                String accesstoken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        fblayout.setVisibility(View.INVISIBLE);
                        mDialog.dismiss();
                        Log.d("response", response.toString());
                        getData(object); //fbcontactlist 구축완료
                        new HttpCall.contactPOST(jsonArray).execute("http://52.79.128.200:3000/api/pbooks");
                        //new JSONTask().execute("http://52.79.128.200:3000/api/pbooks");
                        Log.d("a", getActivity().toString());
                        startActivity(new Intent(getActivity(), GetphonecontactActivity.class));
                        getActivity().finish();
                    }
                });

                //Request Graph API
                Bundle parameters = new Bundle();
                parameters.putString("fields", " taggable_friends"); //이부분에서 원하는 데이터 선정
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("h", "h");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("h", "h");
            }
        });

        //If already login
        if(AccessToken.getCurrentAccessToken() !=null)
        {
            //Just set User Id
            //txtEmail.setText("Already loged in!");
        }
        return rootView;
    }

    private void getData(JSONObject object) {
        try{
            //URL profile_picture = new URL("https://graph.facebook.com/" +object.getString("id")+"/picture?width=250&height=250");
            //Picasso.with(this).load(profile_picture.toString()).into(imgAvatar);

            JSONArray d = object.getJSONObject("taggable_friends").getJSONArray("data");
            JSONObject o;
            for(int i=0; i<d.length(); i++){
                FacebookUserInfo.fbContact contact_ele = new FacebookUserInfo.fbContact();
                contact_ele.name = d.getJSONObject(i).getString("name");
                contact_ele.img_src = d.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url");
                fbcontactlist.add(contact_ele); //->JSON으로 가공해서 실제 넘길 정보 contact list!
            }

            for(int i=0; i<fbcontactlist.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", fbcontactlist.get(i).name);
                jsonObject.put("phonenum", fbcontactlist.get(i).img_src);
                jsonArray.put(jsonObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printKeyHash() {
        try{
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.example.user.facebooklogin", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT)); //print our key Hash in Base64 format to add to Facebook App
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

}
