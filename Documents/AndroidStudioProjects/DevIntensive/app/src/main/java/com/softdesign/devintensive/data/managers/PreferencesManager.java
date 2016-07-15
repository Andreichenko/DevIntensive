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
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
              ConstantManager.USER_MAIL_KEY,
                        ConstantManager.USER_VK_KEY,
                        ConstantManager.USER_GIT_KEY,
                        ConstantManager.USER_BIO_KEY
        };

            private static final String[] USER_NAME = {
                    ConstantManager.USER_FIRST_NAME,
                    ConstantManager.USER_MAIL_KEY
            };

            private static final String[] USER_VALUES = {
                    ConstantManager.USER_RAITING_VALUE,
                    ConstantManager.USER_CODE_LINES_VALUE,
                    ConstantManager.USER_PROJECT_VALUE
            };

public PreferencesManager() {
        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();

        }


            // public void saveUserProfileData(List<String> userFields) {
            public void saveUserProfileData(String[] userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();


        for (int i = 0; i < USER_FIELDS.length; i++) {

                    editor.putString(USER_FIELDS[i], String.valueOf(userFields[i]));
        }
        editor.apply();
        }

            public void saveUserProfileValues(int[] userValues) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();

                for (int i = 0; i < USER_VALUES.length; i++) {
                    editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
                }
                editor.apply();
            }

            public void saveUserName(String[] userFieldsName) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(USER_NAME[0], String.valueOf(userFieldsName[0] + " " + userFieldsName[1]));
                editor.putString(USER_NAME[1], String.valueOf(userFieldsName[2]));
                editor.apply();
            }


public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY, "null"));
        return userFields;
        }

            public List<String> loadUserNameProfile() {
                List<String> userFieldName = new ArrayList<>();
                userFieldName.add(mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME, "null"));
                userFieldName.add(mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME, "null"));
                userFieldName.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "null"));
                return userFieldName;
            }

            public List<String> loadUserProfileValues() {
                List<String> userValues = new ArrayList<>();
                userValues.add(mSharedPreferences.getString(ConstantManager.USER_RAITING_VALUE, "0"));
                userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE, "0"));
                userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE, "0"));
                return userValues;
            }

            public Uri loadUserPhoto() {
                return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                        "android.resource://com.softdesign.devintensive.drawable/user_foto"));
            }

public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
        }


            public String loadUserAvatar() {
                return mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                        "android.resource://com.softdesign.devintensive.drawable/ava");
            }

            public void saveUserAvatar(String str) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(ConstantManager.USER_AVATAR_KEY, str);
                editor.apply();
            }

            public Uri loadUserPhotoImg() {
                return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_IMG_KEY,
        "android.resource://com.softdesign.devintensive.drawable/user_bg"));
        }

            public void saveUserPhotoImg(String str) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(ConstantManager.USER_PHOTO_IMG_KEY, str);
                editor.apply();
            }

/**
 * получение номера телефона для интента
 *
 * @return
 */
public String getUserPhone() {
        return mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "");
        }

/**
 * получение почты для интента
 *
 * @return
 */
public String getUserMail() {
        return mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "");
        }

/**
 * получение ссылки на страницу VK для интента
 *
 * @return
 */
public String getUserVK() {
        return mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "");
        }

/**
 * получение ссылки на страницу gitа для интента
 *
 * @return
 */
public String getUserGit() {
        return mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "");
        }



            public void saveAuthToken(String AuthToken) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(ConstantManager.AUTH_TOKEN_KEY, AuthToken);
                editor.apply();
            }

            public String getAuthToken() {
                return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
            }

            public void saveUserId(String userId) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(ConstantManager.USER_ID_KEY, userId);
                editor.apply();
            }

            public String getUserId() {
                return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
            }

            // region========= Валидация строк --


        //endregion

         }