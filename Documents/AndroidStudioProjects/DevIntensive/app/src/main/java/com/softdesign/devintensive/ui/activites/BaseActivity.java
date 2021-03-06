package com.softdesign.devintensive.ui.activites;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.softdesign.devintensive.R;

import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Created by AlexFrei on 17.07.16.
 */
public class BaseActivity extends AppCompatActivity {
        private static final String TAG = ConstantManager.TAG_PREFIX + "BaseActivity";
        protected ProgressDialog mProgressDialog;

    public void showProgress() {
                if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    }
                 mProgressDialog.show();
                 mProgressDialog.setContentView(R.layout.progress_circle);
            }

                public void hideProgress() {
                if (mProgressDialog != null) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.hide();
                            mProgressDialog.dismiss();
                        }
                    }
                }


                public void showSplash() {
                   if (mProgressDialog == null) {
                            mProgressDialog = new ProgressDialog(this, R.style.custom_dialog);
                            mProgressDialog.setCancelable(false);

        }

                         mProgressDialog.show();
                         mProgressDialog.setContentView(R.layout.splash_screen);
    }

                 public void hideSplash() {
                     if (mProgressDialog != null) {
                           if (mProgressDialog.isShowing()) {
                                      mProgressDialog.hide();
                                      mProgressDialog.dismiss();
            }
        }
    }

                public void showError(String message, Exception error) {
                showToast(message);
                Log.e(TAG, String.valueOf(error));
            }
                public void showToast(String message) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
    }
