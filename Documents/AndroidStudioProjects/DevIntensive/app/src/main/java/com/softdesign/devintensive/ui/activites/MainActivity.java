package com.softdesign.devintensive.ui.activites;


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
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ImageRounded;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private DataManager mDataManager;

    private int mCurrentEditMode = 0;

   private ImageView mCallImg;

    private CoordinatorLayout mCoordinatorLayout;

    private Toolbar mToolbar;

    private DrawerLayout mNavigationDrawer;

    private FloatingActionButton mFab;

    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserBio;

    private List<EditText> mUserInfoViews;

    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mDataManager = DataManager.getInstance();

       /* mRedButton = (Button) findViewById(R.id.red_btn);
        mGreenButton = (Button) findViewById(R.id.green_btn);
        mEditText = (EditText) findViewById(R.id.textview);

        mRedButton.setOnClickListener(this);
        mGreenButton.setOnClickListener(this);
        */
        mCallImg = (ImageView)findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mFab = (FloatingActionButton) findViewById(R.id.fab);


        mUserMail = (EditText) findViewById(R.id.mail_et);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserGit =(EditText) findViewById(R.id.git_et);
        mUserVk = (EditText) findViewById(R.id.vk_et);
        mUserBio = (EditText)findViewById(R.id.bio_et);



        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserBio);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserVk);

     //  ----не получается, вернусь потом mAvatarka = (ImageView) findViewById(R.id.image_avatar);
     //  -----закругление не получается mAvatarka.setImageBitmap(ImageRounded.getRoundedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.avatar)));


        mFab.setOnClickListener(this);
        setupToolbar();
        setupDrawer();



        if (savedInstanceState == null) {

            showSnackbar("Loading");
            showToast("gut gemacht");

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);

            showSnackbar("Load");
            showToast("meine App");

           /* mColorMode = savedInstanceState.getInt(ConstantManager.COLOR_MODE_KEY);
            if (mColorMode == Color.RED) {
                mEditText.setBackgroundColor(Color.RED);

            }else if (mColorMode == Color.GREEN){
                mEditText.setBackgroundColor(Color.GREEN);

            }
            */

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
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
        protected void onDestroy () {
            super.onDestroy();
            Log.d(TAG, "onDestroy");
        }

        @Override
        protected void onRestart () {
            super.onRestart();
            Log.d(TAG, "onRestart");
        }


        @Override
        public void onClick (View v){
            switch (v.getId()) {

                case R.id.fab:
                    showSnackbar("click");
                    if (mCurrentEditMode == 0){
                        changeEditMode(1);
                        mCurrentEditMode = 1;
                    }else {
                        changeEditMode(0);
                        mCurrentEditMode = 0;
                    }

                    showProgress();
                    runWithDelay();
                    break;

           /* case R.id.green_btn:
                mEditText.setBackgroundColor(Color.GREEN);
                mColorMode = Color.GREEN;
                break;
            case R.id.red_btn:
                mEditText.setBackgroundColor(Color.RED);
                mColorMode = Color.RED;
                break;
                */
            }
        }
  /* Реализация кнопки "назад"

   */
    @Override
    public void onBackPressed() {
            if (mNavigationDrawer.isDrawerVisible(GravityCompat.START)) {
                mNavigationDrawer.closeDrawer(GravityCompat.START);
            }else {

                super.onBackPressed();
            }
    }

    @Override
        protected void onSaveInstanceState(Bundle outState){
            super.onSaveInstanceState(outState);

            outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);

       /* Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.COLOR_MODE_KEY, mColorMode); */
        }

    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();

    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if  (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

    }

    private void runWithDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();

            }
        }, 5000);
    }

    private void setupDrawer(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
       // imageRounded(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);

                return false;
            }
        });{
            /**Реализация скругления
             *
             **/


            mImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_avatar);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
            RoundedBitmapDrawable rBD = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
            rBD.setCornerRadius(Math.max(bitmap.getHeight(),bitmap.getWidth())/2.0f);
            mImageView.setImageDrawable(rBD);





        }

    }
    private void changeEditMode(int mode) {
        if (mode == 1){
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
        for (EditText userValue : mUserInfoViews) {
            userValue.setEnabled(true);
            userValue.setFocusable(true);
            userValue.setFocusableInTouchMode(true);
        }

        } else {


            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }

        }

    }

    private void loadUserInfoValue(){

        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i=0; i< userData.size(); i++){
            mUserInfoViews.get(i).setText(userData.get(i));
        }


    }

    /* Сохранение пользовательских данных

     */

    private void saveUserInfoValue(){
        List<String> userData = new ArrayList<>();
        for (EditText userInfoView : mUserInfoViews){
            userData.add(userInfoView.getText().toString());

        }

        mDataManager.getPreferencesManager().saveUserProfileData(userData);

    }



    private Bitmap getRoundBitmap(int drawableRes){
             Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
                bitmap = ImageRounded.getRoundedBitmap(bitmap);
                return bitmap;
            }


    /*Скругление фото метод задает параметры bitmap
    создал класс ImageRounded

     */
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }



}
