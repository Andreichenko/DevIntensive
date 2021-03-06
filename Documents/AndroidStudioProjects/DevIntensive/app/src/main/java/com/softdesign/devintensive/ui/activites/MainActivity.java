package com.softdesign.devintensive.ui.activites;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.ext.MaskTextWatcher;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.MediaStoreFileHelper;
import com.softdesign.devintensive.utils.NetworkStatusChecker;
import com.softdesign.devintensive.utils.RoundedImageTransformation;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private DataManager mDataManager;
    private int mCurrentEditMode;
    private AppBarLayout.LayoutParams mAppBarParams;
    private File mPhotoFile;
    private Uri mSelectedImage;
    private Uri mCurrentProfileImage;
        private ImageView drawerUsrAvatar;


    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_img) ImageView mProfileImage;

    @BindView(R.id.phone_edit) EditText mUserPhone;
    @BindView(R.id.email_edit) EditText mUserMail;
    @BindView(R.id.vk_profile_edit) EditText mUserVk;
    @BindView(R.id.repo_edit) EditText mUserGit;
    @BindView(R.id.about_edit) EditText mUserBio;

    @BindViews({R.id.phone_edit, R.id.email_edit, R.id.vk_profile_edit, R.id.repo_edit, R.id.about_edit})
    List<EditText> mUserFields;

    @BindViews({R.id.user_info_rait_txt, R.id.user_info_code_lines_txt, R.id.user_info_projects_txt})
    List<TextView> mUserValueViews;

    @BindView(R.id.phone_edit_layout) TextInputLayout mUserPhoneLayout;
    @BindView(R.id.email_edit_layout) TextInputLayout mUserMailLayout;
    @BindView(R.id.vk_profile_edit_layout) TextInputLayout mUserVkLayout;
    @BindView(R.id.repo_edit_layout) TextInputLayout mUserGitLayout;


    static final ButterKnife.Setter<View, Boolean> EDIT_TEXT_ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
            view.setFocusable(value);
            view.setFocusableInTouchMode(value);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, "OnCreate");

        mDataManager = DataManager.getInstance();

        mUserPhone.addTextChangedListener(new MaskTextWatcher(mUserPhoneLayout, "+X XXX XXX-XX-XX xxxxxxxxx", MaskTextWatcher.PHONE_MASK));
        mUserMail.addTextChangedListener(new MaskTextWatcher(mUserMailLayout, "XXX@XX.XX", MaskTextWatcher.EMAIL_MASK));
        mUserVk.addTextChangedListener(new MaskTextWatcher(mUserVkLayout, "vk.com/XXX", MaskTextWatcher.URL_MASK));
        mUserGit.addTextChangedListener(new MaskTextWatcher(mUserGitLayout, "github.com/XXX", MaskTextWatcher.URL_MASK));


        setupToolBar();
        setupDrawer();

        initUserFields();
        initUserInfoValue();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
        mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
        changeEditMode(mCurrentEditMode);
    }

    @OnClick(R.id.fab)
    protected void fabOnClick() {
        if (mCurrentEditMode == 0) {
            changeEditMode(1);
        } else {
            if (isUserProfileError()) {
                showSnackbar(getString(R.string.error_correct_profile));
            } else {
                changeEditMode(0);
            }

        }
    }

    @OnClick(R.id.profile_placeholder)
    protected void profilePlaceholderOnClick() {
        showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
    }

    @OnClick(R.id.make_call_img)
    protected void makeCall() {
        Uri phoneUri = Uri.parse("tel:" + mUserPhone.getText());
        Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneUri);
        startActivity(callIntent);
    }

    @OnClick(R.id.send_email_img)
    protected void sendEmail() {
        Uri emailUri = Uri.parse("mailto:" + mUserMail.getText());
        Intent callIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
        startActivity(callIntent);
    }

    @OnClick(R.id.view_vk_profile_img)
    protected void viewVkProfile() {
        Uri vkProfileUri = Uri.parse("https://" + mUserVk.getText());
        Intent callIntent = new Intent(Intent.ACTION_VIEW, vkProfileUri);
        startActivity(callIntent);
    }

    @OnClick(R.id.view_repo_img)
    protected void viewRepo() {
        Uri repoUri = Uri.parse("https://" + mUserGit.getText());
        Intent callIntent = new Intent(Intent.ACTION_VIEW, repoUri);
        startActivity(callIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else if (mCurrentEditMode == 1) {
            changeEditMode(0);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isUserProfileError() {
        if (mUserPhoneLayout.isErrorEnabled() || mUserMailLayout.isErrorEnabled()
                || mUserVkLayout.isErrorEnabled() || mUserGitLayout.isErrorEnabled()) {
            return true;
        }
        return false;
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

        mCollapsingToolbar.setTitle(mDataManager.getPreferencesManager().getUserFullName());
        insertProfileImage(mDataManager.getPreferencesManager().loadUserPhoto());

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {

        drawerUsrAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.drawer_avatar_img);
        TextView drawerUserFullName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_name_txt);
        TextView drawerUserEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.drawer_user_email_txt);

        drawerUserFullName.setText(mDataManager.getPreferencesManager().getUserFullName());
        drawerUserEmail.setText(mDataManager.getPreferencesManager().getUserEmail());

        insertDrawerAvatar(mDataManager.getPreferencesManager().loadUserAvatar());

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);

                if (item.getItemId() == R.id.user_profile_menu) {

                }
                if (item.getItemId() == R.id.team_menu) {
                    Intent authIntent = new Intent(MainActivity.this, UserListActivity.class);
                    finish();
                    startActivity(authIntent);
                }

                if (item.getItemId() == R.id.login_menu) {
                    Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
                    finish();
                    startActivity(authIntent);
                }

                if (item.getItemId() == R.id.login_vk_menu) {
                    VKSdk.login(MainActivity.this, null);
                }

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    mDataManager.getPreferencesManager().saveUserPhoto(mSelectedImage);
                    insertProfileImage(mSelectedImage);
                }
                break;

            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if(resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    mDataManager.getPreferencesManager().saveUserPhoto(mSelectedImage);
                    insertProfileImage(mSelectedImage);
                }
        }

        if (!VKSdk.onActivityResult(requestCode, resultCode, data,
                new VKCallback<VKAccessToken>() {
                    @Override
                    public void onResult(VKAccessToken res) {
                        loadVkUserInfo();
                    }

                    @Override
                    public void onError(VKError error) {
                        //  ошибка авторизации
                        showSnackbar("Berechtigungsfehler: " + error.toString());
                    }
                })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    /**
     * переключает режим редактирования
     *
     * @param mode если 1 - режим редактирования, если 0 - режим просмотра
     */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mCurrentProfileImage = mDataManager.getPreferencesManager().loadUserPhoto();

            mFab.setImageResource(R.drawable.ic_done_black_24dp);

            ButterKnife.apply(mUserFields, EDIT_TEXT_ENABLED, true);

            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

            focusToEditText(mUserPhone);

            mCurrentEditMode = 1;
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);

            ButterKnife.apply(mUserFields, EDIT_TEXT_ENABLED, false);

            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));

            saveUserFields();

            if (mSelectedImage != null && !mSelectedImage.equals(mCurrentProfileImage)) {
                sendPhotoToServer();
                mCurrentProfileImage = mSelectedImage;
            }

            mCurrentEditMode = 0;
        }
    }

    private void sendPhotoToServer() {
        String userId = mDataManager.getPreferencesManager().getUserId();

        File file = MediaStoreFileHelper.getFileByUri(this, mSelectedImage);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<ResponseBody> call = mDataManager.uploadPhoto(userId, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        showSnackbar("Foto speichern auf eine Seite");
                    } else {
                        showSnackbar("Foto speichern nicht gemacht");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showSnackbar("Fehler: " + t.getMessage());
                }
            });
        } else {
            showSnackbar("Netzwerk derzeit nicht verfügbar");
        }

        return;
    }

    private void initUserFields() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserFields.get(i).setText(userData.get(i));
        }
    }

    private void saveUserFields() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserFields) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }

    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, R.string.error_permission_need, Snackbar.LENGTH_LONG)
                    .setAction(R.string.approve, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                loadPhotoFromCamera();
            }
        }
    }

    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                loadPhotoFromGallery();
                                //showSnackbar("Галлерея");
                                break;

                            case 1:
                                loadPhotoFromCamera();
                                //showSnackbar("Камера");
                                break;

                            case 3:
                                dialog.cancel();
                                //showSnackbar("Отмена");
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // передача файла в галлерею
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, image.getAbsolutePath());

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.user_foto)
                .into(mProfileImage);
    }

    private void insertDrawerAvatar(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .fit()
                .centerCrop()
                .transform(new RoundedImageTransformation())
                .placeholder(R.drawable.avatar)
                .into(drawerUsrAvatar);
    }

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    // Set focus on phone field, set cursor to the end field, popup soft keyboard
    private void focusToEditText(EditText editText) {
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void loadVkUserInfo() {
        // Prepare request
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "contacts, screen_name, site, about, photo_max_orig"));

        // Send request
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                // Processing response
                VKList vkList = (VKList) response.parsedModel;
                VKApiUserFull vkApiUserFull = (VKApiUserFull) vkList.get(0);

                List<String> userField = new ArrayList<>();
                userField.add(vkApiUserFull.fields.optString("mobile_phone", ""));
                userField.add("");
                userField.add("vk.com/" + vkApiUserFull.fields.optString("screen_name", ""));
                userField.add(vkApiUserFull.fields.optString("site", ""));
                userField.add(vkApiUserFull.fields.optString("about", ""));

                mDataManager.getPreferencesManager().saveUserPhoto(Uri.parse(vkApiUserFull.fields.optString("photo_max_orig", "")));
                mDataManager.getPreferencesManager().saveUserFullName(vkApiUserFull.toString());
                mDataManager.getPreferencesManager().saveUserProfileData(userField);

                finish();
                startActivity(getIntent());
            }
            @Override
            public void onError(VKError error) {

                showSnackbar("Keine Information: " + error.toString()
                );
            }
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });
    }
}