package com.softdesign.devintensive.ui.activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AlexFrei on 19.07.16.
 */
public class ProfileUserActivity extends BaseActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "ProfileUserActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.about_edit)
    EditText mUserBio;
    @BindView(R.id.user_info_rait_txt)
    TextView mUserRating;
    @BindView(R.id.user_info_code_lines_txt) TextView mUserCodeLines;
    @BindView(R.id.user_info_projects_txt) TextView mUserProjects;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.repositories_list)
    ListView mRepoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);

        setupToolBar();
        initProfileData();
    }




    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoListView.setAdapter(repositoriesAdapter);
        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri repoUri = Uri.parse("https://" + repositories.get(position));
                Intent viewRepoIntent = new Intent(Intent.ACTION_VIEW, repoUri);
                startActivity(viewRepoIntent);
            }
        });
        setupListViewHeight(mRepoListView);

        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProjects.setText(userDTO.getProjects());

        mCollapsingToolbar.setTitle(userDTO.getFullName());


            final String userPhoto;
            if (userDTO.getPhoto().isEmpty()) {
                userPhoto = "null";
                Log.e(TAG, "onBindViewHolder: user with name " + userDTO.getFullName() + " has empty name");
            } else {
                userPhoto = userDTO.getPhoto();
        }

        DataManager.getInstance().getPicasso()
                .load(userPhoto)
                .error(R.drawable.user_bg)
                .placeholder(R.drawable.user_bg)
                .fit()
                .centerCrop()
                .into(mProfileImage);
    }

    public static void setupListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        View view = listAdapter.getView(0, null, listView);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        int totalHeight = view.getMeasuredHeight() * listAdapter.getCount();

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
