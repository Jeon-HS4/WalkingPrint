package com.example.walkingprint;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CFragment extends Fragment {

    Button goToBButton;
    ListView lv;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c, container, false);

        goToBButton = view.findViewById(R.id.button3);
        lv = view.findViewById(R.id.listView);

        new DataFetchTask().execute();

        goToBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // B 프래그먼트로 전환
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new BFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private class DataFetchTask extends AsyncTask<Void, Void, String> {

        String sendMsg = "";
        String receiveMsg = "";
        String serverIp = "http://172.30.1.79:8080/0614/mapList.jsp"; // 연결할 jsp주소

        @Override
        protected String doInBackground(Void... params) {
            try {
                String str;
                URL url = new URL(serverIp);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                        // 중간에 받아온 데이터 출력
                        System.out.println(str); // 또는 Log 등을 사용하여 로그에 출력
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }


        @Override
        protected void onPostExecute(String data) {
            // 데이터를 받아와서 UI에 표시하는 작업 수행
            List<String> dataList = parseJsonData(data);
            // 어댑터 생성
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
            // ListView에 어댑터 설정
            lv.setAdapter(adapter);
        }
        private List<String> parseJsonData(String jsonData) {
            List<String> dataList = new ArrayList<>();
            try {
                JSONObject jsonMain = new JSONObject(jsonData);
                JSONArray jsonArray = jsonMain.getJSONArray("List");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject itemObject = jsonArray.getJSONObject(i);
                    // 적절한 키값으로 데이터를 가져와서 리스트에 추가
                    String item = itemObject.getString("walkId");
                    dataList.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dataList;
        }

    }

}