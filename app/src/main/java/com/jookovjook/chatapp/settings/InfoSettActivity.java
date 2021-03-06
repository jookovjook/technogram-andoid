package com.jookovjook.chatapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.network.UpdateProfile;
import com.jookovjook.chatapp.utils.AuthHelper;

public class InfoSettActivity extends AppCompatActivity implements UpdateProfile.UpdateProfileSimpleCallback{

    String type, value;
    Toolbar mToolbar;
    EditText editText;
    ImageView doneButton;
    TextView responseText;
    UpdateProfile.UpdateProfileSimpleCallback callback = this;
    ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_sett);
        proceedBundle();
        setupLayouts();
    }

    void proceedBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            type = bundle.getString("type");
            value = bundle.getString("value");
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarHeight(){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void setupLayouts(){
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(value);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        mToolbar.setTitle(type);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.height = getStatusBarHeight() + getActionBarHeight();
        mToolbar.setLayoutParams(layoutParams);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) relativeLayout.getLayoutParams();
        lp.height = getActionBarHeight();
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        relativeLayout.setLayoutParams(lp);

        LinearLayout mainContent = (LinearLayout) findViewById(R.id.mainContent);
        CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) mainContent.getLayoutParams();
        lp2.setMargins(0, getStatusBarHeight() + getActionBarHeight(), 0, 0);
        mainContent.setLayoutParams(lp2);

        doneButton = (ImageView) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(value))
                {
                    onBackPressed();
                }else {
                    UpdateProfile updateProfile = new UpdateProfile(InfoSettActivity.this, callback);
                    switch (type) {
                        case InfoAdapter.USERNAME: {
                            updateProfile.addUsername(editText.getText().toString());
                            break;
                        }
                        case InfoAdapter.NAME:{
                            updateProfile.addName(editText.getText().toString());
                            break;
                        }
                        case InfoAdapter.SURNAME:{
                            updateProfile.addSurname(editText.getText().toString());
                            break;
                        }
                        case InfoAdapter.ABOUT:{
                            updateProfile.addAbout(editText.getText().toString());
                            break;
                        }
                        case SecurityAdapter.EMAIL:{
                            updateProfile.addEmail(editText.getText().toString());
                            break;
                        }
                    }
                    updateProfile.execute();
                    loadingSpinner.setVisibility(View.VISIBLE);
                    doneButton.setClickable(false);
                }
            }
        });

        responseText = (TextView) findViewById(R.id.responseText);

        loadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
    }

    @Override
    public void onSuccess(String s) {
        responseText.setText(s);
        String text = editText.getText().toString();
        //loadingSpinner.setVisibility(View.GONE);
        switch (type) {
            case InfoAdapter.USERNAME: {
                AuthHelper.setUsername(this, text);
                break;
            }
            case InfoAdapter.NAME:{
                AuthHelper.setNAME(this, text);
                break;
            }
            case InfoAdapter.SURNAME:{
                AuthHelper.setSURNAME(this, text);
                break;
            }
            case InfoAdapter.ABOUT:{
                AuthHelper.setABOUT(this, text);
                break;
            }
            case SecurityAdapter.EMAIL:{
                AuthHelper.setEMAIL(this, text);
                break;
            }
        }
        Intent intent=new Intent();
        intent.putExtra("RESULT_STRING", editText.getText());
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void onError(String message) {
        responseText.setText(message);
        loadingSpinner.setVisibility(View.GONE);
        doneButton.setClickable(true);
    }
}
