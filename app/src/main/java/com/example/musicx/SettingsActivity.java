package com.example.musicx;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.musicx.DialogSeekBarTime.DialogSeekBarTime;
import com.example.musicx.FragmentsSettings.LockScreem;
import com.example.musicx.FragmentsSettings.SettingsFragment;
import com.example.musicx.FragmentsSettings.SettingsReproductor;
import com.example.musicx.FragmentsSettings.SettingsScreem;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.background.MusicBackground;

import java.util.Locale;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnSettingsListener , DialogSeekBarTime.OnTimeSelected , SettingsReproductor.OnSettingsReproductorPrefers {

   private  ImageView background ;

    //fragments
    private LockScreem fragLockScreem;
    private SettingsScreem settingsScreem;
    private SettingsReproductor settingsReproductor;

    private SettingsFragment settingsFragment;
    private boolean frag1 ,frag2;

    private int type=1;
    private TextView textViewPref;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("frag1",frag1);
        outState.putBoolean("frag2",frag2);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seetings);
        background = findViewById(R.id.background);
        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingsFragment.isVisible()) {
                    finish();
                }else{
                    if (findViewById(R.id.containerB) != null) {
                        finish();
                        }else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Configuracion");
                    }
                }
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Configuracion");

        settingsFragment = new SettingsFragment();

        settingsScreem = new SettingsScreem();
        fragLockScreem = new LockScreem();
        settingsReproductor = new SettingsReproductor();

        if (savedInstanceState != null){
            frag1=savedInstanceState.getBoolean("frag1");
            frag2=savedInstanceState.getBoolean("frag2");
        }

        Toast.makeText(getApplicationContext(),frag1+" "+frag2,Toast.LENGTH_SHORT).show();
            if (findViewById(R.id.containerB) != null){
                if(!frag1) getSupportFragmentManager().beginTransaction().add(R.id.containerB, settingsScreem ,"frag_sS").commit();
                frag1=true;
            }else{
                if ( !frag2) getSupportFragmentManager().beginTransaction().add(R.id.container, settingsFragment,"frag_sF").commit();
                frag2=true;
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }

    private void setBackground(){
        MusicBackground.setBackground(background,this);
    }

    @Override
    public void setFragmentSelected(int selected) {
        switch ( selected) {
            case 1:
            if (findViewById(R.id.containerB) != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.containerB, settingsScreem).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsScreem).commit();
            }
            break;

            case 2:
                if (findViewById(R.id.containerB) != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerB, fragLockScreem).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragLockScreem).commit();
                }
                break;

            case 4:
                if (findViewById(R.id.containerB) != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerB, settingsReproductor).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsReproductor).commit();
                }
                break;
        }
    }



    @Override
    public void setTimeSelected(int time  ) {
        if ( textViewPref != null) {
            SettingsPlayingPreferences settingsPlayingPreferences = new SettingsPlayingPreferences(this);
            if (type == 1) {
                String textTime = String.format(Locale.US, " %d s", time);
                settingsPlayingPreferences.setSavedTimeStart(time);
                textViewPref.setText(textTime);
            } else {
                String textTime = String.format(Locale.US, " %d s", time);
                settingsPlayingPreferences.setSavedTimeFinish(time);
                textViewPref.setText(textTime);
            }
        }else{
            Toast.makeText(this, textViewPref+"",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getTime() {
        SettingsPlayingPreferences settingsPlayingPreferences =  new SettingsPlayingPreferences(this);
        if ( type ==1) {
            return settingsPlayingPreferences.getTimeStart();
        }else {
            return settingsPlayingPreferences.getTimeFinish();
        }
    }

    @Override
    public void setTextView(TextView textView, int type) {
        textViewPref = textView;
        this.type= type;
    }
}