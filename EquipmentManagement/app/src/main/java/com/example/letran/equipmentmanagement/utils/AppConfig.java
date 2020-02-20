package com.example.letran.equipmentmanagement.utils;

import com.example.letran.equipmentmanagement.models.Device;

import java.util.ArrayList;
import java.util.List;

public class AppConfig {
    public static String GET_ALL_DEVICE = "https://letrantrong.000webhostapp.com/ManageDevices/devices/get_all_devices.php";
    public static String CREATE_DEVICE = "https://letrantrong.000webhostapp.com/ManageDevices/devices/create_device.php";
    public static String DELETE_DEVICE = "https://letrantrong.000webhostapp.com/ManageDevices/devices/delete_device.php";
    public static String UPDATE_DEVICE = "https://letrantrong.000webhostapp.com/ManageDevices/devices/update_device.php";
    public static String GET_DEVICE_DETAILS = "https://letrantrong.000webhostapp.com/ManageDevices/devices/get_device_details.php";
    public static String APPROVE_DEVICE = "https://letrantrong.000webhostapp.com/ManageDevices/devices/approve_device.php";

    public static String GET_ALL_USERS = "https://letrantrong.000webhostapp.com/ManageDevices/users/get_all_users.php";
    public static String REGISTRATION = "https://letrantrong.000webhostapp.com/ManageDevices/users/registration.php";
    public static String DELETE_USER = "https://letrantrong.000webhostapp.com/ManageDevices/users/delete_user.php";
    public static String UPDATE_USER = "https://letrantrong.000webhostapp.com/ManageDevices/users/update_user.php";
    public static String GET_USER_DETAILS = "https://letrantrong.000webhostapp.com/ManageDevices/users/get_user_details.php";
    public static String CHECK_USER_LOGIN = "https://letrantrong.000webhostapp.com/ManageDevices/users/checklogin.php";
    public static String CHECK_USER_NAME = "https://letrantrong.000webhostapp.com/ManageDevices/users/checkname.php";
    public static String UPDATE_AVATAR = "https://letrantrong.000webhostapp.com/ManageDevices/users/update_avatar.php";
    public static String GET_AVATAR = "https://letrantrong.000webhostapp.com/ManageDevices/users/get_user_avatar.php";

    public static String GET_ALL_IMAGE = "https://letrantrong.000webhostapp.com/ManageDevices/image/get_all_images.php";
    public static String CREATE_IMAGE = "https://letrantrong.000webhostapp.com/ManageDevices/image/create_image.php";
    public static String DELETE_IMAGE = "https://letrantrong.000webhostapp.com/ManageDevices/image/delete_image.php";
    public static String UPDATE_IMAGE = "https://letrantrong.000webhostapp.com/ManageDevices/image/update_image.php";
    public static String GET_IMAGE_DETAILS = "https://letrantrong.000webhostapp.com/ManageDevices/image/get_image_details.php";

    public static String BASE64 = "";
    public static String METHOD_GET = "GET";
    public static String METHOD_POST = "POST";
    public static String PERMISSION_DEFAULT = "1";

    public static String NAME_USER;
    public static String PASSWORD_USER;
    public static String PERMISSION_USER;
    public static String ID_USER;
    public static String CREATE_TIME_USER;
    public static String AVATAR;
    public static int FLAG = 0; //0 update again, 1 get list current
    public static List<Device> LST_DEVICES = new ArrayList<>();
    public static List<Device> LST_DEVICES_APPROVED = new ArrayList<>();
    public static List<Device> LST_DEVICES_WAIT_APPROVED = new ArrayList<>();
}
