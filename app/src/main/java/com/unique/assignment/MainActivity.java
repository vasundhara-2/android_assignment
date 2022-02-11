package com.unique.assignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String url = "http://machinetest.erpguru.in/service.asmx/GetCustomerRegisteredByApp_1_0";
    private ProgressBar loadingPB;
    private LinearLayoutManager mLayoutManager_vertical;
    private RecyclerView recyclerView;

    private SQLiteDatabase sqLiteDatabaseObj;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingPB = findViewById(R.id.idLoadingPB);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager_vertical = new LinearLayoutManager(getApplicationContext());
        mLayoutManager_vertical.setOrientation(RecyclerView.VERTICAL);
        pager = findViewById(R.id.viewPagerMain);

        getData();
        getImageData();
        SQLiteDataBaseBuild();
        SQLiteTableBuild();


    }

    private void getImageData() {
        loadingPB.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String imgUrl = "http://machinetest.erpguru.in/service.asmx/Sp_Get_Appimages_CA_1_0";
        StringRequest request = new StringRequest(Request.Method.POST, imgUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                // Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {

                    JSONObject respObj = new JSONObject(response);

                    JSONArray jsonArray = respObj.getJSONArray("data1");
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonMainCatObject = (JSONObject) jsonArray.get(i);
                        String name = jsonMainCatObject.getString("imagepath");
                        array.put(name);
                    }
                    ViewPagerAdapter adapter = new ViewPagerAdapter(MainActivity.this, array);
                    pager.setAdapter(adapter);
                    Log.d("jsonArray", array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", "75");

                return params;
            }

        };
        queue.add(request);
    }

    private void getData() {
        loadingPB.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                try {
                    JSONObject respObj = new JSONObject(response);

                    JSONArray jsonArray = respObj.getJSONArray("data1");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonMainCatObject = (JSONObject) jsonArray.get(i);
                        String name = jsonMainCatObject.getString("FName"); //add to arraylist
                        DataAdapter adapter = new DataAdapter(MainActivity.this, jsonArray, jsonMainCatObject);
                        recyclerView.setLayoutManager(mLayoutManager_vertical);

                        recyclerView.setAdapter(adapter);
                        Log.d("data1", name);
                    }
                    Log.d("data1", jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserId", "75");
                params.put("PageNo", "1");

                // at last we are
                // returning our params.
                return params;
            }

        };
        queue.add(request);
    }

    public void SQLiteDataBaseBuild() {

        sqLiteDatabaseObj = openOrCreateDatabase("AndroidDataBase", Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS DataItemTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR, village VARCHAR, route VARCHAR);");

    }


}