package com.example.letran.equipmentmanagement.models;

public class User {

    private String id;
    private String name;
    private String password;
    private String permission;
    private String create_time;

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NANE = "name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PERMISSION = "permission";
    public static final String COLUMN_CREATE_TIME = "create_time";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT ,"
                    + COLUMN_NANE + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_PERMISSION + " TEXT,"
                    + COLUMN_CREATE_TIME + " TEXT"
                    + ")";

    public User(){

    }

    public User(String id, String name, String password, String permission, String create_time) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.permission = permission;
        this.create_time = create_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPermission() {
        return permission;
    }

    public String getCreate_time() {
        return create_time;
    }


}
