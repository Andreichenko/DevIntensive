package com.softdesign.devintensive.ui.activites;


import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.widget.ThemedSpinnerAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ImageRounded;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    @BindViews({R.id.phone_et, R.id.mail_et, R.id.vk_et, R.id.git_et, R.id.bio_et})
    List<EditText> mEditTexts;

    @BindViews({R.id.rating_count, R.id.code_strings_count, R.id.projects_count})
    List<TextView> mTextViews;

    final ButterKnife.Setter<View, Boolean> setEditTextParams = new ButterKnife.Setter<View, Boolean>() {
        // TODO: 07.07.16 делаем сеттер Butterknife для фокуса
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
            view.setFocusable(value);
            view.setFocusableInTouchMode(value);

            if (view == mEditTexts.get(0) && value == true) {
                // TODO: 07.07.16 наводим фокус на поле ввода телефона и показываем клавиатуру
                view.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    };

    @BindViews({R.id.call_img, R.id.button_send_email, R.id.button_open_vk, R.id.button_open_repos})
    List<ImageView> mImageViews;

    final ButterKnife.Action<ImageView> setOnClickListeners = new ButterKnife.Action<ImageView>() {
        // TODO: 07.07.16 Action put
        @Override
        public void apply(@NonNull ImageView imageViews, int index) {
            imageViews.setOnClickListener(MainActivity.this);
        }
    };
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.main_profile_image)
    ImageView mMainProfileImage;

    private File mPhotoFile = null;
    private Uri mSelectedImage;
    private DataManager mDataManager;

    private AppBarLayout.LayoutParams mAppBarParams = null;

    private boolean mCurrentEditMode = false;

    // TODO: 05.07.16 инициализация данных
    // TODO: 06.07.16 неверная инициализация данных
    // TODO: 08.07.16 исправил





    @Override  // TODO: 07.07.16 Метод onCreate() вызывается при создании или перезапуске активности.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, " выполнен метод onCreate");
        ButterKnife.bind(this);
        ButterKnife.apply(mImageViews, setOnClickListeners);

        mDataManager = new DataManager();



        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        try {
            setupToolbar();
            setupDrawer();
            initUserData();
            initUserValue();
            Picasso.with(this)
                    .load(mDataManager.getPreferencesManager().LoadUserPhoto())
                    .placeholder(R.drawable.user_foto)
                    .error(R.drawable.user_foto)
                    .into(mMainProfileImage);
            if (savedInstanceState == null) {
                //выполняется, если активность создана впервые
            } else {
                //выполняется, если активность уже создалась
                mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //маска для номера телефона
        mEditTexts.get(0).addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }



    /** За onCreate() всегда следует вызов onStart(), но перед onStart() не обязательно должен
     * идти onCreate(), onStart() может вызываться и для возобновления работы приостановленного
     * приложения.*/
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "выполнен метод onStart");

    }
        @Override // TODO: 07.07.16 Метод onResume() вызывается после onStart(). Также может вызываться после onPause(). 
        protected void onResume() {
            super.onResume();
            Log.d(TAG, "выполнен метод onResume");
        }

    /** Метод onPause() вызывается после сворачивания текущей активности или перехода к
     * новому. От onPause() можно перейти к вызову либо onResume(), либо onStop().*/
        @Override
        protected void onPause() {
            super.onPause();
            Log.d(TAG, "выполнен метод onPause");
        }

    /** Метод onStop() вызывается, когда окно становится невидимым для пользователя. Это может
     * произойти при её уничтожении, или если была запущена другая активность (существующая или
     * новая), перекрывшая окно текущей активности. */
        @Override
        protected void onStop() {
            super.onStop();
            Log.d(TAG, "выполнен метод onStop");

        }
    /** Метод вызывается по окончании работы активности, при вызове метода finish() или в случае,
     * когда система уничтожает этот экземпляр активности для освобождения ресурсов. */
        @Override
        protected void onDestroy () {
            super.onDestroy();
            Log.d(TAG, "выполнен метод onDestroy");
        }

    /** Если окно возвращается в приоритетный режим после вызова onStop(), то в этом случае
     * вызывается метод onRestart(). Т.е. вызывается после того, как активность была остановлена и
     * снова была запущена пользователем. Всегда сопровождается вызовом метода onStart(). */
        @Override
        protected void onRestart () {
            super.onRestart();
            Log.d(TAG, "выполнен метод onRestart");
        }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

       /* Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.COLOR_MODE_KEY, mColorMode); */
    }

    /**
     * Получает и обрабатывает результат из другой активности
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null){
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);

                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile!= null){
                    mSelectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(mSelectedImage);
                }
        }



    }

    /**
     * Обновляет изображение профиля
     * @param selectedImage ссылка на изображение
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .placeholder(R.drawable.user_foto)
                .error(R.drawable.user_foto)
                .into(mMainProfileImage);
        mDataManager.getPreferencesManager().SaveUserPhoto(selectedImage);
        Call<ResponseBody> call = mDataManager.uploadPhoto(selectedImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "Удачно");

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackBar("Не удалось загрузить фотографию на сервер");
            }
        });
    }

    /**
     * Возвращает диалог смены фотографии
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_FOTO:
                String[] selectItems = {getString(R.string.dialog_selection_load_from_galery),
                        getString(R.string.dialog_selection_take_photo), getString(R.string.dialog_selection_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.dialog_title_change_photo));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                // TODO: 07.07.16   загрузить из галереи
                                loadPhotoFromGalery();
                                break;

                            case 1:
                                // TODO: 07.07.16   сделать снимоk
                                loadPhotoFromCamera();
                                break;

                            case 2:
                                // TODO: 07.07.16   отменить
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }

    /**
     * Настраивает DrawerLayout
     */
    private void setupDrawer() throws IOException {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerHeader(navigationView, mDataManager.getPreferencesManager().loadAvatar(),
                mDataManager.getPreferencesManager().loadUserName(),
                mDataManager.getPreferencesManager().loadUserData().get(1));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setCheckable(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    /**

     * Настраивает шапку NavigationView
     * @param avatar ссылка на картинку
     * @param name имя
     * @param email почта
     */

    private void setupDrawerHeader(NavigationView parent, Uri avatar, String name, String email) throws IOException {
        View view = parent.getHeaderView(0);
        if (avatar != null) {

            ImageView imageView = (ImageView) view.findViewById(R.id.image_avatar);
            Picasso.with(MainActivity.this)
                    .load(avatar)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .transform(new ImageRounded()).into(imageView);
        }
        if (name != null) {
            TextView textView = (TextView) view.findViewById(R.id.user_name_txt);
            textView.setText(name);
        }
        if (email != null) {
            TextView textView = (TextView) view.findViewById(R.id.user_email_txt);
            textView.setText(email);
        }
    }



    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Показывает Snackbar
     * @param message
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настраивает Toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mDataManager.getPreferencesManager().loadUserName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (mCurrentEditMode) {
                    changeEditMode(false);
                } else {
                    changeEditMode(true);
                }
                break;

            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_FOTO);
                break;

            //обработчики нажатия на кнопку "позвонить", "отправить email" и тд...
            case R.id.call_img:
                String number = mEditTexts.get(0).getText().toString();
                if (!number.equals("") || !number.equals("null")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));

                    //проверка разрешения на звонок
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.CALL_PHONE
                        }, ConstantManager.PERMISSION_CALL_REQUEST_CODE);

                        Snackbar.make(mCoordinatorLayout, R.string.snackbar_message_need_take_permissions, Snackbar.LENGTH_LONG)
                                .setAction("Разрешить", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openApplicationSettings();
                                    }
                                }).show();
                    } else {
                        try {
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException e) {
                            showSnackBar(getString(R.string.main_activity_error_not_found_call_app));
                        }
                    }
                }
                break;

            case R.id.button_send_email:
                String email = mEditTexts.get(1).getText().toString();
                if (!email.equals("") || !email.equals("null")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Отправка письма..."));
                    } catch (ActivityNotFoundException e) {
                        showSnackBar(getString(R.string.main_activity_error_not_found_email_app));
                    }
                }

                break;

            case R.id.button_open_vk:
                String vkUrl = mEditTexts.get(2).getText().toString();
                if (!vkUrl.equals("") || !vkUrl.equals("null")) {
                    Intent vkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/" + vkUrl));

                    try {
                        startActivity(vkIntent);
                    } catch (ActivityNotFoundException e) {
                        showSnackBar(getString(R.string.main_activity_error_not_found_vk_app));
                    }
                }
                break;

            case R.id.button_open_repos:
                String gitUrl = mEditTexts.get(3).getText().toString();
                if (!gitUrl.equals("") || !gitUrl.equals("null")) {
                    Intent gitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/" + gitUrl));

                    try {
                        startActivity(gitIntent);
                    } catch (ActivityNotFoundException e) {
                        showSnackBar(getString(R.string.main_activity_error_not_found_git_app));
                    }
                }
                break;
        }
    }

    /**
     * Переключает режимы редактирования
     * @param mode если false - просмотр, если true - редактирование
     */
    private void changeEditMode(boolean mode) {
        if (mode) {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);

            ButterKnife.apply(mEditTexts, setEditTextParams, mode);

            /*for (EditText editText : mEditTexts) {
                  editText.setEnabled(true);
                  editText.setFocusable(true);
                  editText.setFocusableInTouchMode(true);

                if (editText == mEditTexts.get(0)) { //наводим фокус на поле ввода телефона и показываем клавиатуру
                    editText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }*/
            showProfileHlaceholder();
            lockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            mCurrentEditMode = true;
        } else {
            boolean valid = true;
            for (EditText editText : mEditTexts) {
                if (!formatTextInEditText(editText)) {
                    valid = false;
                    editText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    break;
                }
            }
            if (valid) {
                mFab.setImageResource(R.drawable.ic_done_black_24dp);
                /*for (EditText editText : mEditTexts) {
                    editText.setEnabled(false);
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    saveUserData();
                }*/
                saveUserData();

                ButterKnife.apply(mEditTexts, setEditTextParams, mode);

                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
                mCurrentEditMode = false;
            }
        }
    }

    /**
     * Возвращает true, если item прошел валидацию и false, если не прошел
     * @param editText view, над которым производятся действия
     * @return
     */
    private boolean formatTextInEditText(EditText editText) {
        switch (editText.getId()) {
            case R.id.phone_et:
                String number = editText.getText().toString();
                if (number.length() >= 7 && number.length() <= 20) {
                    return true;
                } else {
                    showSnackBar(getString(R.string.user_profile_invalidate_number));
                    return false;
                }

            case R.id.mail_et:
                String email = editText.getText().toString();
                try {
                    if (email.split("@")[0].length() >= 3
                            && email.split("@")[1].split("\\.")[0].length() >=2
                            && email.split("\\.")[1].length() >= 2) {
                        return true;
                    } else {
                        showSnackBar(getString(R.string.user_profile_invalidate_email));
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) { //ошибка выскакивает, если в строке нету @ и .
                    showSnackBar(getString(R.string.user_profile_invalidate_email));
                    return false;
                }

            case R.id.vk_et:
                String vk = editText.getText().toString();
                if (vk.equals("null") || vk.length() < 5) {
                    showSnackBar(getString(R.string.user_profile_invalidate_vk));
                    return false;
                } else {
                    return true;
                }

            case R.id.git_et:
                String github = editText.getText().toString();
                if (github.equals("null")) {
                    showSnackBar(getString(R.string.user_profile_invalidate_github));
                    return false;
                } else {
                    return true;
                }

            default:
                return true;
        }
    }

    /**
     * Меняет текст EditText'a на значение из сохраненных данных
     * @param editTexts коллекция из EditText'ов
     * @param values коллекция строк сохраненных данных
     */
    private void changeTextOnTextView(List<EditText> editTexts, List<String> values){
        for (int i = 0; i < editTexts.size(); i++){
            try{
                if (values.get(i).equals("null")){
                    editTexts.get(i).setText("");
                } else {
                    editTexts.get(i).setText(values.get(i));
                }
            } catch (NullPointerException e){
                editTexts.get(i).setText("");
            }
        }
    }

    /**
     * Загружает пользовательские данные
     */
    private void initUserData(){
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++){
            mEditTexts.get(i).setText(userData.get(i));
        }
    }

    /**
     * Сохраняет пользовательские данные
     */
    private void saveUserData(){
        List<String> userData = new ArrayList<>();

        for (EditText userField : mEditTexts){
            userData.add(userField.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    /**
     * Загружает фото из галереи
     */
    private void loadPhotoFromGalery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.dialog_title_select_picture)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Загружает фотографию, сделанную с помощью камеры
     */
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCapturePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 30.06.2016 обработать ошибку
            }

            if (mPhotoFile != null) {
                takeCapturePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCapturePhoto, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, R.string.snackbar_message_need_take_permissions, Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    /**
     * Обрабатывает ответ об разрешении или запрете на разрешения
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 30.06.2016 обработка разрешения (например, сообщение)
                showSnackBar("Разрешение на камеру получено!");
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 30.06.2016 обработка разрешения (например, сообщение)
                showSnackBar("Разрешение на сохранение данных получено!");
            }
        }

        if (requestCode == ConstantManager.PERMISSION_CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 30.06.2016 обработка разрешения (например, сообщение)
                showSnackBar("Разрешение на исходящие звонки получено!");
            }
        }
    }

    /**
     * Возвращает ссылку на файл изображения
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //место, где сохраняется наш файл
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Скрывает элементы добавления фото в режиме редактирования
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Отображает элементы добавления фото в режиме редактирования
     */
    private void showProfileHlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Блокирует toolbar от скролла во время редактирования
     */
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     * Разблокирует toolbar после редактирования
     */
    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL| AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     * Обработка android runtime permissons
     */
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    private void initUserValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mTextViews.get(i).setText(userData.get(i));

        }
    }
    private void loadUserData() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        changeTextOnTextView(mEditTexts, userData);
    }

}




























