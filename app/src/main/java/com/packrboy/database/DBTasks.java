package com.packrboy.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.packrboy.classes.Shipment;
import com.packrboy.logging.L;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arindam.paaltao on 04-Aug-15.
 */
public class DBTasks {

    public static final int AVAILABLE_TASKS = 0;
    public static final int PENDING_TASKS = 1;
    public static final int COMPLETED_TASKS = 2;
    private TasksHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBTasks(Context context) {
        mHelper = new TasksHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertTasks(int table, ArrayList<Shipment> shipmentArrayList, boolean clearPrevious) {
        if (clearPrevious) {
            deleteTasks(table);
        }


        //create a sql prepared statement
        String sql = "INSERT INTO " + ((table == AVAILABLE_TASKS) ? TasksHelper.TABLE_AVAILABLE_TASKS :(table == PENDING_TASKS) ? TasksHelper.TABLE_PENDING_TASKS : TasksHelper.TABLE_COMPLETED_TASKS) + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < shipmentArrayList.size(); i++) {
            Shipment current = shipmentArrayList.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, current.getRequestType());
            statement.bindLong(3, current.getItemId());
            statement.bindString(4, current.getItemQuantity());
            statement.bindString(5, current.getItemType());
            statement.bindString(6, current.getDeliveryType());
            statement.bindString(7, current.getTransitStatus());
            statement.bindString(8, current.getImageURL());
            statement.bindString(9, current.getStreetNo());
            statement.bindString(10, current.getRoute());
            statement.bindString(11, current.getState());
            statement.bindString(12, current.getCity());
            statement.bindString(13, current.getPostalCode());
            statement.bindDouble(14,current.getLatitude());
            statement.bindDouble(15, current.getLongitude());


            statement.execute();
        }
        //set the transaction as successful and end the transaction
        L.m("inserting entries " + shipmentArrayList.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Shipment> readShipments(int table) {
        ArrayList<Shipment> shipmentArrayList = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {TasksHelper.COLUMN_ID,
                TasksHelper.COLUMN_REQUEST_TYPE,
                TasksHelper.COLUMN_SHIPMENT_ID,
                TasksHelper.COLUMN_ITEM_QUANTITY,
                TasksHelper.COLUMN_ITEM_TYPE,
                TasksHelper.COLUMN_DELIVERY_TYPE,
                TasksHelper.COLUMN_TRANSIT_STATUS,
                TasksHelper.COLUMN_IMAGE_URL,
                TasksHelper.COLUMN_STREET_NO,
                TasksHelper.COLUMN_ROUTE,
                TasksHelper.COLUMN_LOCALITY,
                TasksHelper.COLUMN_CITY,
                TasksHelper.COLUMN_POSTAL_CODE,
                TasksHelper.COLUMN_LATITUDE,
                TasksHelper.COLUMN_LONGITUDE
        };
        Cursor cursor = mDatabase.query(((table == AVAILABLE_TASKS) ? TasksHelper.TABLE_AVAILABLE_TASKS :(table == PENDING_TASKS) ? TasksHelper.TABLE_PENDING_TASKS : TasksHelper.TABLE_COMPLETED_TASKS), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                Shipment shipment = new Shipment();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank movie object to contain our data
                shipment.setRequestType(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_REQUEST_TYPE)));
                shipment.setItemId(cursor.getInt(cursor.getColumnIndex(TasksHelper.COLUMN_SHIPMENT_ID)));
                shipment.setItemQuantity(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_ITEM_QUANTITY)));
                shipment.setItemType(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_ITEM_TYPE)));
                shipment.setDeliveryType(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_DELIVERY_TYPE)));
                shipment.setTransitStatus(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_TRANSIT_STATUS)));
                shipment.setImageURL(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_IMAGE_URL)));
                shipment.setStreetNo(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_STREET_NO)));
                shipment.setRoute(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_ROUTE)));
                shipment.setCity(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_LOCALITY)));
                shipment.setState(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_CITY)));
                shipment.setPostalCode(cursor.getString(cursor.getColumnIndex(TasksHelper.COLUMN_POSTAL_CODE)));
                shipment.setLatitude(cursor.getDouble(cursor.getColumnIndex(TasksHelper.COLUMN_LATITUDE)));
                shipment.setLongitude(cursor.getDouble(cursor.getColumnIndex(TasksHelper.COLUMN_LONGITUDE)));
                //add the task to the list of task objects which we plan to return
                shipmentArrayList.add(shipment);
            }
            while (cursor.moveToNext());
        }
        return shipmentArrayList;
    }

    public void deleteTasks(int table) {
        mDatabase.delete(((table == AVAILABLE_TASKS) ? TasksHelper.TABLE_AVAILABLE_TASKS :(table == PENDING_TASKS) ? TasksHelper.TABLE_PENDING_TASKS : TasksHelper.TABLE_COMPLETED_TASKS), null, null);
    }

    private static class TasksHelper extends SQLiteOpenHelper {
        public static final String TABLE_PENDING_TASKS = "pending_tasks";
        public static final String TABLE_AVAILABLE_TASKS = "available_tasks";
        public static final String TABLE_COMPLETED_TASKS = "completed_tasks";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_REQUEST_TYPE = "requestID";
        public static final String COLUMN_SHIPMENT_ID = "shipmentID";
        public static final String COLUMN_ITEM_QUANTITY = "itemQuantity";
        public static final String COLUMN_ITEM_TYPE = "itemType";
        public static final String COLUMN_DELIVERY_TYPE = "deliveryType";
        public static final String COLUMN_TRANSIT_STATUS = "transitStatus";
        public static final String COLUMN_IMAGE_URL = "imageURL";

        public static final String COLUMN_STREET_NO = "pickupStreetNo";
        public static final String COLUMN_ROUTE = "pickupRoute";
        public static final String COLUMN_LOCALITY = "pickupLocality";
        public static final String COLUMN_CITY = "pickupCity";
        public static final String COLUMN_POSTAL_CODE = "pickupPostalCode";
        public static final String COLUMN_LATITUDE = "pickupLatitude";
        public static final String COLUMN_LONGITUDE = "pickupLongitude";

        private static final String CREATE_TABLE_AVAILABLE_TASKS = "CREATE TABLE " + TABLE_AVAILABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_REQUEST_TYPE + " TEXT," +
                COLUMN_SHIPMENT_ID + " INTEGER," +
                COLUMN_ITEM_QUANTITY + " TEXT," +
                COLUMN_ITEM_TYPE + " TEXT," +
                COLUMN_DELIVERY_TYPE + " TEXT," +
                COLUMN_TRANSIT_STATUS + " TEXT," +
                COLUMN_IMAGE_URL + " TEXT," +
                COLUMN_STREET_NO + " TEXT," +
                COLUMN_ROUTE + " TEXT," +
                COLUMN_LOCALITY + " TEXT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_POSTAL_CODE + " TEXT," +
                COLUMN_LATITUDE + " TEXT," +
                COLUMN_LONGITUDE + " TEXT" +
                ");";
        private static final String CREATE_TABLE_PENDING_TASKS = "CREATE TABLE " + TABLE_PENDING_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_REQUEST_TYPE + " TEXT," +
                COLUMN_SHIPMENT_ID + " INTEGER," +
                COLUMN_ITEM_QUANTITY + " TEXT," +
                COLUMN_ITEM_TYPE + " TEXT," +
                COLUMN_DELIVERY_TYPE + " TEXT," +
                COLUMN_TRANSIT_STATUS + " TEXT," +
                COLUMN_IMAGE_URL + " TEXT," +
                COLUMN_STREET_NO + " TEXT," +
                COLUMN_ROUTE + " TEXT," +
                COLUMN_LOCALITY + " TEXT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_POSTAL_CODE + " TEXT," +
                COLUMN_LATITUDE + " TEXT," +
                COLUMN_LONGITUDE + " TEXT" +
                ");";
        private static final String CREATE_TABLE_COMPLETED_TASKS = "CREATE TABLE " + TABLE_COMPLETED_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_REQUEST_TYPE + " TEXT," +
                COLUMN_SHIPMENT_ID + " INTEGER," +
                COLUMN_ITEM_QUANTITY + " TEXT," +
                COLUMN_ITEM_TYPE + " TEXT," +
                COLUMN_DELIVERY_TYPE + " TEXT," +
                COLUMN_TRANSIT_STATUS + " TEXT," +
                COLUMN_IMAGE_URL + " TEXT," +
                COLUMN_STREET_NO + " TEXT," +
                COLUMN_ROUTE + " TEXT," +
                COLUMN_LOCALITY + " TEXT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_POSTAL_CODE + " TEXT," +
                COLUMN_LATITUDE + " TEXT," +
                COLUMN_LONGITUDE + " TEXT" +
                ");";
        private static final String DB_NAME = "tasks_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public TasksHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_AVAILABLE_TASKS);
                db.execSQL(CREATE_TABLE_PENDING_TASKS);
                db.execSQL(CREATE_TABLE_COMPLETED_TASKS);
                L.m("create table box office executed");
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_AVAILABLE_TASKS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_PENDING_TASKS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_COMPLETED_TASKS + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                L.t(mContext, exception + "");
            }
        }
    }
}
