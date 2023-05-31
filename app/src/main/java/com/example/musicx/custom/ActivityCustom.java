package com.example.musicx.custom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicx.R;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.background.MusicBackground;

import java.util.Objects;

public class ActivityCustom extends AppCompatActivity implements View.OnClickListener , OnThemeSelected , CustomList.OnCustomList {
    private ImageView setBackground;
    private Button btnSelected;
    private int themeS=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setBackground = findViewById(R.id.background);
        btnSelected = findViewById(R.id.btnSelected);
        btnSelected.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Listas");

            if (savedInstanceState == null) {
                FragmentManager fg = getSupportFragmentManager();
                FragmentTransaction ft = fg.beginTransaction();
                int config = getIntent().getExtras().getInt("config");
                ft.add(R.id.container, new CustomList(), "fragA").commit();
            }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicBackground.setBackground(setBackground,this);
    }

    @Override
    public void onClick(View v) {
        SettingsPlayingPreferences preferences = new SettingsPlayingPreferences(this);
        preferences.setSavedThemeList(themeS);
        Toast.makeText(this, "tema "+themeS+" aplicado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMyTheme(int theme) {
            themeS=theme+1;
    }

    @Override
    public int getThemeConfig() {
        return 1;
    }

    @Override
    public void startServiceMusic() {

    }
}
