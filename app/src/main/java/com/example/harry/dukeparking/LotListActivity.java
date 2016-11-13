package com.example.harry.dukeparking;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LotListActivity extends AppCompatActivity {
    //key String: lot UID; value String: name, Stirng: capacity
    public final static String LOT_ID = "LOT_IDDDD";
    public static HashMap<String,Lot> lotMap;
    public SQLiteDatabase lotDb;
    public final static String dbName = "transactionDataBase";
    public final static String transactionTableName = "transactionTable";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_list);
        initialize();
        //processData();
        final List<Lot> lotList = new ArrayList<>(lotMap.values());
        LotListAdapter adapter = new LotListAdapter(lotList,getApplicationContext());
        final ListView lView = (ListView) findViewById(R.id.parking_lot_list);
        lView.setAdapter(adapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), LotDetailActivity.class);
                intent.putExtra(LOT_ID, lotList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void initialize(){
        lotMap = new HashMap<>();
        InputStream inputStream = getResources().openRawResource(R.raw.facilitylookup);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                Log.i("Test",row[0]+"      "+row[1]);
                int cap;
                if(row.length>2){
                    cap = Integer.parseInt(row[2]);
                }
                else{
                    cap = -1;
                }
                Lot oneLot = new Lot(row[1],row[0],cap);
                lotMap.put(row[1],oneLot);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
    }

    public void processData(){
//        dbHelper helper = new dbHelper(getApplicationContext());
        //create database
        lotDb = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS " + "transactionTable" + "("
                + "lot_id" + " TEXT," + "car_id" + " TEXT,"
                + "date_entry" + " TEXT," + "date_exit" + " TEXT" + ")";
        lotDb.execSQL(CREATE_TRANSACTION_TABLE);
        //read data
        InputStream inputStream = getResources().openRawResource(R.raw.refinedparkingtransactions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            Boolean is_firstRow = true;
            while ((csvLine = reader.readLine()) != null) {
                if(is_firstRow){
                    is_firstRow = false;
                }
                else{
                    String carId = "";
                    String lotId = "";
                    String dateEntry = "";
                    String dateExit = "";
                    String[] row = csvLine.split(",");
                    for(int i=0; i<row.length; i++){
                        switch (i) {
                            case 0:
                                carId = row[i];
                            case 1:
                                lotId = row[i];
                            case 4:
                                dateEntry = row[i];
                            case 5:
                                dateExit = row[i];
                        }
                    }
                    //insert into table
                    lotDb.execSQL("INSERT INTO "
                            + transactionTableName
                            + " ( lot_id, car_id, date_entry, date_exit)"
                            + " VALUES (" + Math.random() + "," + carId + "," + carId + "," + carId +");");
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
    }


//    public class dbHelper extends SQLiteOpenHelper {
//        // Database Name
//        public static final String DATABASE_NAME = "lotTransactionData";
//
//        // Table Name
//        public static final String TABLE_TRANSACTION = "transaction";
//
//        // Contacts Table Columns names
//        private static final String CAR_ID = "car_id";
//        private static final String LOT_ID = "lot_id";
//        private static final String DATE_ENTRY = "date_entry";
//        private static final String DATE_EXIT = "date_exit";
//
//        public dbHelper(Context context) {
//            super(context, DATABASE_NAME, null, 1);
//        }
//
//        // Creating Tables
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
//                    + LOT_ID + " TEXT PRIMARY KEY," + CAR_ID + " TEXT,"
//                    + DATE_ENTRY + " TEXT," + DATE_EXIT + " TEXT" + ")";
//            db.execSQL(CREATE_TRANSACTION_TABLE);
//        }
//
//        // Upgrading database
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            // Drop older table if existed
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
//
//            // Create tables again
//            onCreate(db);
//        }
//    }
}
