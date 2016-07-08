package com.softdesign.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexFrei on 26.06.16.
 */
public class DevIntensiveAplication extends Application {

    private static SharedPreferences sSharedPreferences;


    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;


    } @Override
    public void onCreate() {
        super.onCreate();

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}


// TODO: 08.07.16 старый код запомнить 


   /* public static SharedPreferences mSharedPreferences;
    
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_BIO_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_GIT_KEY};





    @Override
    public void onCreate() {
        super.onCreate();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void SaveUserProfileData(List<String> UserFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i=0; i<USER_FIELDS.length; i++){
            editor.putString(USER_FIELDS[i],UserFields.get(i));


        }
        editor.apply();
    }

    public List<String> loadUserProfileData(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY,null ));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY,null ));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY,null ));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY,null ));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY,null ));
        return userFields;
    }*/


