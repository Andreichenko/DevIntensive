package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveAplication;

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



    public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveAplication.getSharedPreferences();
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
}
