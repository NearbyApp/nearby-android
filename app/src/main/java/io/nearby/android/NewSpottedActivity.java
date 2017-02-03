package io.nearby.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by Marc on 2017-02-02.
 */

public class NewSpottedActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    private Toolbar mToolbar;
    private EditText mEditText;
    private ImageButton mSendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_spotted_activity);

        findViewById(R.id.upload_picture_button).setOnClickListener(this);
        mSendButton = (ImageButton) findViewById(R.id.send_button);
        mSendButton.setOnClickListener(this);
        mSendButton.setClickable(false);
        mSendButton.setEnabled(false);

        mEditText = (EditText) findViewById(R.id.spotted_text);
        mEditText.addTextChangedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:
                String text = mEditText.getText().toString();

                break;
            case R.id.upload_picture_button:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() > 0){
            mSendButton.setClickable(true);
            mSendButton.setEnabled(true);
        }
        else {
            mSendButton.setClickable(false);
            mSendButton.setEnabled(false);
        }
    }
}
