package com.example.user.project2;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpCall {

    public static class contactPOST extends AsyncTask<String, String, String>{
        ArrayList<FacebookUserInfo.fbContact> fbcontactlist;
        JSONArray jsonArray;

        public contactPOST(JSONArray jsonarray){
            this.jsonArray = jsonarray;
        }

        @Override
        protected String doInBackground(String... urls) {

            Log.i("Test", String.valueOf(jsonArray));

            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음 ->이거를 JSON 형식으로 받는걸로 바꿔야하나?
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();

                //서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonArray.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌

                //서버로 부터 데이터를 받음
                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임


            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            tv_output.setText(result);//서버로 부터 받은 값을 출력해주는 부분
        }
    }

    public static class contactGET extends AsyncTask<String, String, ArrayList<Map<String, String>>> {

        @Override
        protected ArrayList<Map<String, String>> doInBackground(String... urls) {
            try{
                ArrayList<Map<String, String>> dataList;
                dataList = new ArrayList<Map<String, String>>();

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]); //url가져오기
                    con = (HttpURLConnection) url.openConnection();
                    con.connect(); //연결수행

                    //입력스트림생성 (서버에서 정보 받기)
                    InputStream stream = con.getInputStream();

                    reader= new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    //실제 reader에서 데이터를 가져오는 부분 -> node.js에서 데이터 가져오기
                    while((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(buffer.toString());

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        map.put("name", jsonObject.getString("name"));
                        map.put("phonenum", jsonObject.getString("phonenum"));
                        dataList.add(map);
                    }
                    Log.i("dataList", String.valueOf(dataList));

                    return dataList;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //doInBackground후, 텍스트뷰의 값을 바꿔주기
        protected void onPostExecute(ArrayList<Map<String, String>> result) {
            super.onPostExecute(result);
            //something do.....
        }
    }

    public static class galleryPOST extends AsyncTask<String, String, String>
    {
        JSONArray jsonArray;

        public galleryPOST(JSONArray json){
            this.jsonArray= json;
        }

        @Override
        protected String doInBackground(String... urls) {
            Log.i("POST", String.valueOf(jsonArray));
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control", "no-cache"); //no-cache....??
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.connect();

                OutputStream outStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonArray.toString());
                writer.flush();
                writer.close();

                //서버로 부터 데이터를 받음 ->여기부터 지우니까 POST가 제대로 작동이 되지않음(서버가 정보를 못받음) 왜????????
                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            tv_output.setText(result);//서버로 부터 받은 값을 출력해주는 부분
        }
    }

    public static class galleryGET extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) { //나중에  return 형식바꾸기!!!
            try{
                ArrayList<String> dataList;
                dataList = new ArrayList<String>();
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();

                    InputStream stream = con.getInputStream(); // 입력 스트림 생성
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer(); //실제 데이터 받는곳
                    String line = "";

                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(buffer.toString());
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        dataList.add(jsonArray.getJSONObject(i).getString("img"));
                    }

                    Log.i("dataList(gallery_GET)", String.valueOf(dataList));

                    return dataList;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분

            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class foodPOST extends AsyncTask<String, String, String>
    {
        JSONObject jsonObject;

        public foodPOST(JSONObject json){
            this.jsonObject= json;
        }

        @Override
        protected String doInBackground(String... urls) {
            Log.i("POST", String.valueOf(jsonObject));
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(urls[0]);
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Cache-Control", "no-cache"); //no-cache....??
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "text/html");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.connect();

                OutputStream outStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();

                //서버로 부터 데이터를 받음 ->여기부터 지우니까 POST가 제대로 작동이 되지않음(서버가 정보를 못받음) 왜????????
                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            tv_output.setText(result);//서버로 부터 받은 값을 출력해주는 부분
        }
    }

    public static class foodGET extends AsyncTask<String, String, ArrayList<Map<String, String>>> {

        @Override
        protected ArrayList<Map<String, String>> doInBackground(String... urls) {
            try{
                ArrayList<Map<String,String>> dataList;
                dataList = new ArrayList<Map<String, String>>();

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]); //url가져오기
                    con = (HttpURLConnection) url.openConnection();
                    con.connect(); //연결수행

                    //입력스트림생성 (서버에서 정보 받기)
                    InputStream stream = con.getInputStream();

                    reader= new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    //실제 reader에서 데이터를 가져오는 부분 -> node.js에서 데이터 가져오기
                    while((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(buffer.toString());

                    for(int i=0; i<jsonArray.length(); i++){
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name", jsonArray.getJSONObject(i).getString("name"));
                        map.put("ordernum", jsonArray.getJSONObject(i).getString("ordernum"));
                        map.put("index", jsonArray.getJSONObject(i).getString("index")); //this part is index or result
                        dataList.add(map);
                    }

                    Log.i("dataList", String.valueOf(dataList));

                    return dataList;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //doInBackground후, 텍스트뷰의 값을 바꿔주기
        protected void onPostExecute(ArrayList<Map<String, String>> result) {
            super.onPostExecute(result);
            //something do.....
        }
    }
}