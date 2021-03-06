package com.example.letran.equipmentmanagement.models;

public class Device {
    private String id;
    private String name;
    private String description;
    private String issue;
    private String url_image;
    private String create_time;
    private String approver;
    private String creater;
    private String note;
    private String date_approve;
    private String approver_1;
    private String note_1;
    private String date_approve_1;

    public static final String TABLE_NAME = "devices";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NANE = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ISSUE = "issue";
    public static final String COLUMN_URL_IMAGE = "url_image";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_APPROVER = "approver";
    public static final String COLUMN_CREATER = "creater";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE_APPROVE = "date_approve";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT ,"
                    + COLUMN_NANE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_ISSUE + " TEXT,"
                    + COLUMN_URL_IMAGE + " TEXT,"
                    + COLUMN_CREATE_TIME + " TEXT,"
                    + COLUMN_APPROVER + " TEXT,"
                    + COLUMN_CREATER + " TEXT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_DATE_APPROVE + " TEXT"
                    + ")";

    public Device(){

    }

    public Device(String id, String name, String description, String issue, String url_image, String create_time, String approver, String creater, String note, String date_approve) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.issue = issue;
        this.url_image = url_image;
        this.create_time = create_time;
        this.approver = approver;
        this.creater = creater;
        this.note = note;
        this.date_approve = date_approve;
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

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateApprove() {
        return date_approve;
    }

    public void setDateApprove(String date_approve) {
        this.date_approve = date_approve;
    }

    public String getApprover_1() {
        return approver_1;
    }

    public void setApprover_1(String approver_1) {
        this.approver_1 = approver_1;
    }

    public String getNote_1() {
        return note_1;
    }

    public void setNote_1(String note_1) {
        this.note_1 = note_1;
    }

    public String getDateApprove_1() {
        return date_approve_1;
    }

    public void setDateApprove_1(String date_approve_1) {
        this.date_approve_1 = date_approve_1;
    }
}
