package com.softdesign.devintensive.ui.activites;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AlexFrei on 15.07.16.
 */
public class ProfileUserActivity extends AppCompatActivity {

                private Toolbar mToolbar;
        private ImageView mProfileImage, mGotoGitImg;
        private EditText mUserBio, mUserGit;
        private TextView mUserRating, mUserCodeLines, mUserProjects;
        private CollapsingToolbarLayout mCollapsingToolbarLayout;
        private CoordinatorLayout mCoordinatorLayout;

                private ListView mRepoListView;

                @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.user_profile_content);

                        mToolbar = (Toolbar) findViewById(R.id.toolbar);
                mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
                mUserBio = (EditText) findViewById(R.id.about_et);
                mUserRating = (TextView) findViewById(R.id.user_name_txt);
                mUserCodeLines = (TextView) findViewById(R.id.user_info_code_lines_txt);
                mUserProjects = (TextView) findViewById(R.id.user_projects);
                mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
                mRepoListView = (ListView) findViewById(R.id.user_list);
                mGotoGitImg = (ImageView) findViewById(R.id.git_et);
                mUserGit = (EditText) findViewById(R.id.github_et);

                       setupToolbar();
                initProfileData();
            }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                private void setupToolbar() {
                setSupportActionBar(mToolbar);
                ActionBar actionBar = getSupportActionBar();

                        if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
            }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    private void initProfileData() {
                UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);
                final List<String> repositories = userDTO.getRepositories();
                final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);

                        mRepoListView.setAdapter(repositoriesAdapter);

                        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        doOpenGit(mRepoListView.getAdapter().getItem(position).toString());
                                    }
                            });

                        mUserBio.setText(userDTO.getBio());
                mUserRating.setText(userDTO.getRating());
                mUserCodeLines.setText(userDTO.getCodeLines());
                mUserProjects.setText(userDTO.getProjects());

                        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

                        Picasso.with(this)
                                .load(userDTO.getPhoto())
                                .placeholder(R.drawable.user_foto)
                                .error(R.drawable.user_foto)
                                .into(mProfileImage);

                    }

                void doOpenGit(String url) {
                Intent openlinkIntentGit = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.txt_https) + url));
                startActivity(openlinkIntentGit);
            }
    }

