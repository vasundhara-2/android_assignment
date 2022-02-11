package com.unique.assignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {


    private final JSONArray jsonArray;
    JSONObject jsonMainObject;

    Context context;
    private String name, routeName, villageName, srNo;
    SQLiteDatabase sqLiteDatabaseObj;
    String NameHolder = "vasu", VillageHolder = "magi", RouteHolder = "hjjh", SQLiteDataBaseQueryHolder;

    public DataAdapter(MainActivity mainActivity, JSONArray jsonArray, JSONObject jsonMainObject) {
        this.jsonArray = jsonArray;
        this.jsonMainObject = jsonMainObject;
        context = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {

        DataItems dataItems = new DataItems();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMainCatObject = (JSONObject) jsonArray.get(position);
                srNo = jsonMainCatObject.getString("SrNo");
                name = jsonMainCatObject.getString("FName") + jsonMainCatObject.getString("LName");
                routeName = jsonMainCatObject.getString("RouteName");
                villageName = jsonMainCatObject.getString("VillageName");
                dataItems.setId(srNo);
                dataItems.setName(name);
                dataItems.setVillage(villageName);
                dataItems.setRoute(routeName);
            }

            if (position % 3 == 0) {
                holder.txt_color.setBackgroundColor(Color.RED);
            } else if (position % 3 == 1) {

                holder.txt_color.setBackgroundColor(Color.GREEN);
            } else if (position % 3 == 2) {

                holder.txt_color.setBackgroundColor(Color.BLUE);
            }
            holder.txt_namedata.setText(name);
            holder.txt_routedata.setText(routeName);
            holder.txt_villagedata.setText(villageName);
            holder.txt_srno.setText(srNo);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to save data?");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    InsertDataIntoSQLiteDatabase("vasu", "fdhg", "fhgjj");
                                    Toast.makeText(context, "Data Inserted Successfully", Toast.LENGTH_LONG).show();

                                    dialog.dismiss();
                                }
                            });

                    builder.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();


                    return true;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_namedata, txt_routedata, txt_villagedata, txt_srno, txt_color;

        public MyViewHolder(View rowView) {
            super(rowView);

            txt_srno = rowView.findViewById(R.id.txt_srno);
            txt_color = rowView.findViewById(R.id.txt_color);
            txt_namedata = rowView.findViewById(R.id.txt_namedata);
            txt_routedata = rowView.findViewById(R.id.txt_routedata);
            txt_villagedata = rowView.findViewById(R.id.txt_villagedata);

        }


    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void InsertDataIntoSQLiteDatabase(String name, String village, String route) {
        try {
            SQLiteDataBaseQueryHolder = "INSERT INTO DataItemTable (name,village,route) VALUES('" + name + "', '" + village + "', '" + route + "');";

            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
