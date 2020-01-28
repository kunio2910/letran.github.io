package com.example.letran.equipmentmanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.letran.equipmentmanagement.models.Device;
import com.example.letran.equipmentmanagement.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "equipment_management_db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Device.CREATE_TABLE);
        Log.e("test","test");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public long insertUser(User user) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(User.COLUMN_ID, user.getId());
        values.put(User.COLUMN_NANE, user.getName());
        values.put(User.COLUMN_PASSWORD, user.getPassword());
        values.put(User.COLUMN_PERMISSION, user.getPermission());
        values.put(User.COLUMN_CREATE_TIME, user.getCreate_time());

        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public User getUser(int id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_NANE, User.COLUMN_PASSWORD,User.COLUMN_PERMISSION,User.COLUMN_CREATE_TIME},
                User.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        User user = new User();
        user.setId(cursor.getString(cursor.getColumnIndex(User.COLUMN_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NANE)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));
        user.setPermission(cursor.getString(cursor.getColumnIndex(User.COLUMN_PERMISSION)));
        user.setCreate_time(cursor.getString(cursor.getColumnIndex(User.COLUMN_CREATE_TIME)));
        // close the db connection
        cursor.close();

        return user;
    }

    public List<User> getAllUser() {

        List<User> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + User.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(User.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(User.COLUMN_NANE)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));
                user.setPermission(cursor.getString(cursor.getColumnIndex(User.COLUMN_PERMISSION)));
                user.setCreate_time(cursor.getString(cursor.getColumnIndex(User.COLUMN_CREATE_TIME)));

                users.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return users;
    }

    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + User.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_NANE, user.getName());
        values.put(User.COLUMN_PASSWORD, user.getPassword());
        values.put(User.COLUMN_PERMISSION, user.getPermission());
        values.put(User.COLUMN_CREATE_TIME, user.getCreate_time());

        // updating row
        return db.update(User.TABLE_NAME, values, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + User.TABLE_NAME);
    }

    //Device

    public long insertDevice(Device device) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Device.COLUMN_ID, device.getId());
        values.put(Device.COLUMN_NANE, device.getName());
        values.put(Device.COLUMN_DESCRIPTION, device.getDescription());
        values.put(Device.COLUMN_ISSUE, device.getIssue());
        values.put(Device.COLUMN_URL_IMAGE, device.getUrl_image());
        values.put(Device.COLUMN_CREATE_TIME, device.getCreate_time());
        values.put(Device.COLUMN_APPROVER, device.getApprover());
        // insert row
        long id = db.insert(Device.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Device getDevice(int pid) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Device.TABLE_NAME,
                new String[]{Device.COLUMN_ID, Device.COLUMN_NANE, Device.COLUMN_DESCRIPTION,Device.COLUMN_ISSUE,Device.COLUMN_URL_IMAGE,Device.COLUMN_CREATE_TIME,Device.COLUMN_APPROVER},
                Device.COLUMN_ID + "=?",
                new String[]{String.valueOf(pid)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Device device = new Device();
        device.setId(cursor.getString(cursor.getColumnIndex(Device.COLUMN_ID)));
        device.setName(cursor.getString(cursor.getColumnIndex(Device.COLUMN_NANE)));
        device.setDescription(cursor.getString(cursor.getColumnIndex(Device.COLUMN_DESCRIPTION)));
        device.setIssue(cursor.getString(cursor.getColumnIndex(Device.COLUMN_ISSUE)));
        device.setUrl_image(cursor.getString(cursor.getColumnIndex(Device.COLUMN_URL_IMAGE)));
        device.setCreate_time(cursor.getString(cursor.getColumnIndex(Device.COLUMN_CREATE_TIME)));
        device.setApprover(cursor.getString(cursor.getColumnIndex(Device.COLUMN_APPROVER)));
        // close the db connection
        cursor.close();

        return device;
    }

    public List<Device> getAllProducts() {

        List<Device> devices = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Device.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Device device = new Device();
                device.setId(cursor.getString(cursor.getColumnIndex(Device.COLUMN_ID)));
                device.setName(cursor.getString(cursor.getColumnIndex(Device.COLUMN_NANE)));
                device.setDescription(cursor.getString(cursor.getColumnIndex(Device.COLUMN_DESCRIPTION)));
                device.setIssue(cursor.getString(cursor.getColumnIndex(Device.COLUMN_ISSUE)));
                device.setUrl_image(cursor.getString(cursor.getColumnIndex(Device.COLUMN_URL_IMAGE)));
                device.setCreate_time(cursor.getString(cursor.getColumnIndex(Device.COLUMN_CREATE_TIME)));
                device.setApprover(cursor.getString(cursor.getColumnIndex(Device.COLUMN_APPROVER)));

                devices.add(device);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return devices;
    }

    public int getDevicesCount() {
        String countQuery = "SELECT  * FROM " + Device.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateDevice(Device product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Device.COLUMN_NANE, product.getName());
        values.put(Device.COLUMN_DESCRIPTION, product.getDescription());
        values.put(Device.COLUMN_ISSUE, product.getIssue());
        values.put(Device.COLUMN_URL_IMAGE, product.getUrl_image());
        values.put(Device.COLUMN_CREATE_TIME, product.getCreate_time());
        values.put(Device.COLUMN_APPROVER, product.getApprover());

        // updating row
        return db.update(Device.TABLE_NAME, values, Device.COLUMN_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteDevice(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Device.TABLE_NAME, Device.COLUMN_ID + " = ?",
                new String[]{String.valueOf(device.getId())});
        db.close();
    }

    public void deleteAllDevice() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Device.TABLE_NAME);
    }
}
