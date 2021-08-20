package org.techtown.gsheetwithimage;

public class Configuration {
    // google apps script 연결, 자체 제작 api 활용
    public static final String APP_SCRIPT_WEB_APP_URL = "https://script.google.com/macros/s/AKfycby76pzSZHc7sOsmezdvh0MNiNOZ0ywrLt0swLC5KRdla1gdsHkCXZ0LWHIkuYhUi2I/exec";
    public static final String ADD_USER_URL = APP_SCRIPT_WEB_APP_URL;

    // 학번, 이름, 이미지 변수
    public static final String KEY_ID = "uId";
    public static final String KEY_NAME = "uName";
    public static final String KEY_IMAGE = "uImage";

    //google spreadsheet insert 동작 변수
    public  static final String KEY_ACTION = "action";
}
