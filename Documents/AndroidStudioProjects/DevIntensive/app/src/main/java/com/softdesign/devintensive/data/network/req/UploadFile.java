package com.softdesign.devintensive.data.network.req;

import android.net.Uri;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by AlexFrei on 14.07.16.
 */
public class UploadFile {

    private MultipartBody.Part mBody;

    public UploadFile() {
    }
    public MultipartBody.Part photo(Uri uri){
        File file = new File(uri.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        return mBody;
    }

    public MultipartBody.Part avatar(Uri uri){
        File file = new File(uri.getPath());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        mBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        return mBody;
    }




}
