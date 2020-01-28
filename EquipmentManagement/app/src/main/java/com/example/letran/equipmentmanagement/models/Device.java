package com.example.letran.equipmentmanagement.models;

public class Device {
    private String id;
    private String name;
    private String description;
    private String issue;
    private String url_image;
    private String create_time;
    private String approver;

    public static final String TABLE_NAME = "devices";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NANE = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ISSUE = "issue";
    public static final String COLUMN_URL_IMAGE = "url_image";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_APPROVER = "approver";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT ,"
                    + COLUMN_NANE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_ISSUE + " TEXT,"
                    + COLUMN_URL_IMAGE + " TEXT,"
                    + COLUMN_CREATE_TIME + " TEXT,"
                    + COLUMN_APPROVER + " TEXT"
                    + ")";

    public Device(){

    }

    public Device(String id, String name, String description, String issue, String url_image, String create_time, String approver) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.issue = issue;
        this.url_image = url_image;
        this.create_time = create_time;
        this.approver = approver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }
}
