package com.example.musicx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.musicx.background.MusicBackground;
import com.example.musicx.custom.ActivityCustom;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static com.example.musicx.MainActivity.BACKGROND;
import static com.example.musicx.MainActivity.BACKGROND_THEME;
import static com.example.musicx.MainActivity.THEME_CUSTOM_BACKGROUND;

public class ThemesActivity extends AppCompatActivity {

    private ImageView setBackground;
    private LinearLayout linearLayout;
    Toolbar toolbar;
    ActivityResultLauncher <Intent>   activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK ) {
                        if (result.getData() != null){
                            Uri uri = result.getData().getData();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Drawable drawable = Drawable.createFromStream(inputStream,uri.toString());
                                setBackground.setImageDrawable(drawable);
                                setBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                SharedPreferences.Editor editor = getSharedPreferences(BACKGROND_THEME,MODE_PRIVATE).edit();
                                editor.putString(THEME_CUSTOM_BACKGROUND, result.getData().getData().toString());
                                editor.apply();
                            } catch (FileNotFoundException e) {
                                Toast.makeText(getApplicationContext()," error ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        toolbar = findViewById(R.id.toolbar_themes);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Fondo");
        setBackground = findViewById(R.id.backgroundActivityThemes);
        linearLayout = findViewById(R.id.containerThemes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicBackground.setBackground(setBackground,this);
        if(!(linearLayout.getChildCount()>0))
        loadThemes();
    }

    public void loadThemes(){
        int[] backgrounds ={
                R.drawable.activity_main_background,
                R.drawable.activity_background2,
                R.drawable.activity_background3,
                R.drawable.activity_background4,
                R.drawable.activity_background5,
                R.drawable.activity_background6,
                R.drawable.gradiente
        };

        customBackground();
        for ( int i = 0;   i < backgrounds.length  ;i ++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(backgrounds[i]);
            imageView.setTag(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int  position = Integer.parseInt(v.getTag().toString()) ;
                    setBackground.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), backgrounds[position]));
                    SharedPreferences.Editor editor = getSharedPreferences(BACKGROND_THEME,MODE_PRIVATE).edit();
                    editor.putInt(BACKGROND, position);
                    editor.putString(THEME_CUSTOM_BACKGROUND, null);
                    editor.apply();
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                   120,140);
            params.setMargins(5,5,5,5);

            imageView.setLayoutParams(params);
            linearLayout.addView(imageView);
        }
    }

    public void customBackground(){
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic__image_theme));
        imageView.setOnClickListener(v -> cargarImagen());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                120,140);
        params.setMargins(5,5,5,5);
        imageView.setLayoutParams(params);
        linearLayout.addView(imageView);

    }

    private void cargarImagen(){
        Intent intent ;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }else{
            intent = new Intent(Intent.ACTION_PICK);
        }
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activityResultLauncher.launch(Intent.createChooser(intent, "selecciona la aplicacion"));
    }

}