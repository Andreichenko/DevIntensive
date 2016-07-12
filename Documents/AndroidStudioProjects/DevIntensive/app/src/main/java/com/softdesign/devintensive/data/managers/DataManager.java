package com.softdesign.devintensive.data.managers;

import android.preference.PreferenceManager;
import android.content.Context;
import android.net.Uri;
import java.io.File;
import java.net.URI;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import com.softdesign.devintensive.data.network.RestService;
import com.softdesign.devintensive.data.network.ServiceGenerator;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.DevIntensiveApplication;

/**
 * Created by AlexFrei on 26.06.16.
 */
public class DataManager {

    public static DataManager INSTANCE = null;

    private PreferencesManager mPreferencesManager;

    private RestService mRestService;


    public DataManager() {

        this.mPreferencesManager = new PreferencesManager();
        mRestService = ServiceGenerator.createService(RestService.class);

    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {

        return mPreferencesManager;
    }


    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq) {
        return mRestService.loginUser(userLoginReq);
    }

    public Call<ResponseBody> uploadPhoto(Uri photoUri) {
        File photo = new File(photoUri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        MultipartBody.Part bodyPart = MultipartBody.Part
                .createFormData("photo", String.valueOf(photoUri.hashCode()).substring(0, 7),
                        requestBody);
        return mRestService.uploadImage(bodyPart);
    }
}
