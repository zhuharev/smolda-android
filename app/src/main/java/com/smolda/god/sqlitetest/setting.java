package com.smolda.god.sqlitetest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class setting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener  {

    public static String SMOLENSK_ONLY = "smolensk_only";
    public static String IMPORTANT_ONLY = "important_only";
    public static String SHOW_IMAGES = "show_images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxImportantOnly);
        cb.setChecked(settings.getBoolean(IMPORTANT_ONLY, false));
        cb.setOnCheckedChangeListener(this);

        CheckBox cbShowImages = (CheckBox) findViewById(R.id.checkBoxShowImages);
        cbShowImages.setChecked(settings.getBoolean(SHOW_IMAGES, true));
        cbShowImages.setOnCheckedChangeListener(this);

        CheckBox cbSmolenskOnly = (CheckBox) findViewById(R.id.checkBoxSmolenskOnly);
        cbSmolenskOnly.setChecked(settings.getBoolean(SMOLENSK_ONLY, false));
        cbSmolenskOnly.setOnCheckedChangeListener(this);



   /*     cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                          @Override
                          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                              important_only = isChecked;
                              SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                              SharedPreferences.Editor editor = settings.edit();
                              editor.putBoolean("important_only", important_only);

                              Log.i("ALE", "" + important_only);

                              // Commit the edits!
                              editor.commit();
                          }
                      }
        );*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        String tag;
        int id = buttonView.getId();
        switch (id) {
            case R.id.checkBoxImportantOnly:
                tag = IMPORTANT_ONLY;
                break;
            case R.id.checkBoxShowImages:
                tag = SHOW_IMAGES;
                break;
            case R.id.checkBoxSmolenskOnly:
                tag = SMOLENSK_ONLY;
                break;
            default:
                return;
        }
        editor.putBoolean(tag, isChecked);
        editor.commit();
    }

    @Override
    protected void onStop(){
        super.onStop();


    }


}
