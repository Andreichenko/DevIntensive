package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexFrei on 26.06.16.
 */
public class    PreferencesManager {
    private SharedPreferences mSharedPreferences;
    public static final String[]USER_FIELDS = {ConstantManager.USER_MAIL_KEY,
    ConstantManager.USER_GIT_KEY, ConstantManager.USER_BIO_KEY, ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_VK_KEY  };
    private static final String[] USER_VALUES = {ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE, ConstantManager.USER_COUNT_PROJECTS_VALUE};



    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i=0; i<USER_FIELDS.length;i++){
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();

    }

    public List<String> loadUserProfileData(){
        List<String> userFields = new ArrayList<>();
        for (int i=0; i<USER_FIELDS.length; i++){
            userFields.add(mSharedPreferences.getString(USER_FIELDS[i],"null"));
        }
        return userFields;
    }

    public void SaveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri LoadUserPhoto(){
        String tempUri =  mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/user_foto");
        return Uri.parse(tempUri);
    }
    public void saveAvatar(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }
    public Uri loadAvatar() {
        String imageUri = mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                "android.resource://softdesign.devintensive/drawable/avatar.jpg");
        return Uri.parse(imageUri);
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN, authToken);
        editor.apply();

    }
    public String getAuthToken() {
        String authToken = mSharedPreferences.getString(ConstantManager.AUTH_TOKEN, "null");
        return authToken;
    }
    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }
    public String getUserId() {
        String userId = mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
        return userId;

    }
    public void saveUserProfileValues(List<String> userValues) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], userValues.get(i));
        }
        editor.apply();
        }
    public List<String> loadUserProfileValues() {
        List<String> userValues = new ArrayList<>();
        for (int i = 0; i < USER_VALUES.length; i++){
            userValues.add(mSharedPreferences.getString(USER_VALUES[i], "0"));
        }
        return userValues;
    }

}
