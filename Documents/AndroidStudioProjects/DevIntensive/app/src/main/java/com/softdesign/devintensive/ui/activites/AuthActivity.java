package com.softdesign.devintensive.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.NetworkStatus;

import com.softdesign.devintensive.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by AlexFrei on 06.07.16.
 */
public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    @BindViews({R.id.auth_login, R.id.auth_password})
    List<EditText> mAuthFields;

    @BindView(R.id.auth_button)
    Button mAuthButton;

    @BindView(R.id.forgot_pass)
    TextView mForgotPass;

    @BindView(R.id.auth_coordinatorlayout)
    CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();
        mAuthButton.setOnClickListener(this);
        mForgotPass.setOnClickListener(this);
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.auth_button:
                signIn();

                break;

            case R.id.forgot_pass:
                rememberPassword();


        }
    }
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.FORGOT_PASS_URL));
        startActivity(rememberIntent);

    }

    private void loginSuccess(UserModelRes userModel) {
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserProfile(userModel);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
        this.finish();
    }

    private void signIn() {
        if (NetworkStatus.isNetworkAvailable(this)) {
            String login, password;
            login = mAuthFields.get(0).getText().toString();
            password = mAuthFields.get(1).getText().toString();

            if (login.equals("")) {
                showSnackbar(getString(R.string.auth_login_login_empty));
                return;
            }

            if (password.equals("")) {
                showSnackbar(getString(R.string.auth_password_password_empty));
                return;
            }

            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(login, password));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    switch (response.code()) {
                        case 200:
                            loginSuccess(response.body());
                            break;
                        case 404:
                            showSnackbar(getString(R.string.auth_error_incor_login_or_pass));
                            break;
                        default:
                            showSnackbar(getString(R.string.auth_error_error_message));
                            break;
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    showSnackbar(getString(R.string.auth_error_error_message));

                }
            });



        }else{
            showSnackbar(getString(R.string.auth_error_available_net));
        }
    }

    private void saveUserValues(UserModelRes userModel) {
        List<String> userValues = new ArrayList<>();
        userValues.add(String.valueOf(userModel.getData().getUser().getProfileValues().getRating()));
        userValues.add(String.valueOf(userModel.getData().getUser().getProfileValues().getLinesCode()));
        userValues.add(String.valueOf(userModel.getData().getUser().getProfileValues().getProjects()));

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
    }

    private void saveUserProfile(UserModelRes userModel) {
        List<String> userValues = new ArrayList<>();
        userValues.add(String.valueOf(userModel.getData().getUser().getContacts().getPhone())
                .replaceAll("\\(", "").replaceAll("\\)", ""));
        userValues.add(String.valueOf(userModel.getData().getUser().getContacts().getEmail()));
        userValues.add(String.valueOf(userModel.getData().getUser().getContacts().getVk())
                .split("/")[1]);
        userValues.add(String.valueOf(userModel.getData().getUser().getRepositories().getRepo())
                .replaceFirst("/", "mark").split("mark")[1]);
        userValues.add(String.valueOf(userModel.getData().getUser().getPublicInfo().getBio()));
        mDataManager.getPreferencesManager().saveUserData(userValues);
        mDataManager.getPreferencesManager().saveUserName(
                userModel.getData().getUser().getSecondName(),
                userModel.getData().getUser().getFirstName());
        mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar()));
        mDataManager.getPreferencesManager().saveAvatar(Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto()));
    }


}
