package com.softdesign.devintensive.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

/**
 * Created by AlexFrei on 19.07.16.
 */
public class RetainFragment extends Fragment {

    private List<User> mUsersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    public List<User> getUsersList() {


        return mUsersList;
    }

    public void setUsersList(List<User> usersList) {
        mUsersList = usersList;
    }
}
