package io.nearby.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Marc on 2017-02-02.
 */

public class NewSpottedActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_spotted_activity);

        findViewById(R.id.send_button).setOnClickListener(this);
        findViewById(R.id.upload_picture_button).setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.spotted_text);

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
}
