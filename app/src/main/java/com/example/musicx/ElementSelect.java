package com.example.musicx;

import static com.example.musicx.AlbumDetailsAdapter.albumFilesTarget;
import static com.example.musicx.MainActivity.musicFilesGeneral;
import static com.example.musicx.MainActivity.musicFilesfavoritos;
import static com.example.musicx.MainActivity.musicFilesrecientes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicx.adapterScreemSize.AdapterScreemSize;
import com.example.musicx.background.MusicBackground;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Objects;

public class ElementSelect extends AppCompatActivity {
    public RecyclerView musicSelections ;
    public  ArrayList<Items> itemList;
    public  MyAdapterselected adapterSelected ;
    public  AlertDialog.Builder  alerta;
    public  AlertDialog titulo;
    private static ArrayList<MusicFiles> musicFilesElementSelected = new ArrayList<MusicFiles>();
    private static ArrayList<MusicFiles> mFilesES = new ArrayList<MusicFiles>();
   int elementSelected=0;
    private boolean selected_All_Elements = false;
    private ImageView setBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_select);
        // my toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_element_selected);
        AppBarLayout appBarLayout = findViewById(R.id.appbarlayoutmain);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setPadding(0,0,0,0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Seleccionar elementos");

        setBackground = findViewById(R.id.backgroundElementSelected);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setOutlineProvider(null);
        }
        musicSelections = findViewById (R. id. selectedelement);
        itemList = new ArrayList<>();
        int position = getIntent().getIntExtra("position", -1);


        elementSelected= getIntent().getIntExtra("elemtSelected", -1);
       if ( elementSelected ==12 ){
            mFilesES = musicFilesElementSelected;
        }else if ( elementSelected ==1 ){
            mFilesES = musicFilesrecientes;
        }else if ( elementSelected== 2){
            mFilesES = musicFilesfavoritos;
        }else if (elementSelected ==3) {
            mFilesES = albumFilesTarget;
        }else {
            mFilesES= musicFilesGeneral;
        }


        for (int  it=0 ; it < mFilesES.size() ;it++){
            itemList.add(  new Items(  mFilesES.get(it).getTitle() , mFilesES.get(it).getPatch() , getAlbumImagen ( mFilesES.get(it).getPatch() ) , false)   );
        }

        adapterSelected = new MyAdapterselected(mFilesES, this, itemList);

        new AdapterScreemSize(musicSelections,this ,0);

        musicSelections .setAdapter( adapterSelected );

        musicSelections. scrollToPosition(position);


        /*
     select_All.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if ( selected_All_Elements ==false ){
                for ( int i =0 ; i < adapterSelected.getCount(); i++){
                    adapterSelected.setCheckBoxTrue(i);
                }
        select_All.setImageDrawable(getResources().getDrawable(R.drawable.desall));
             }else {
                 for ( int i =0 ; i < adapterSelected.getCount(); i++){
                     adapterSelected.setCheckBoxFalse(i);
                 }
                 select_All.setImageDrawable(getResources().getDrawable(R.drawable.menuselect));

             }

             selected_All_Elements = ! selected_All_Elements;
         }
     });
*/


    }//fin onCreate


public void share (){
            StringBuilder countries = new StringBuilder();
            ArrayList<Uri> u = new ArrayList<>();
            for(Items hold: adapterSelected .getSelected()){
                if(hold.isCheckbox()){
                    countries.append(" ").append(hold.getCancion());
                    u.add( Uri.parse( hold.getDir() ) );
                }
            }
            if (u.size()  > 0){
                Intent compartirAudio = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
                compartirAudio.setType("audio/*");
                compartirAudio.putParcelableArrayListExtra ( Intent.EXTRA_STREAM, u );
                startActivity(Intent.createChooser(compartirAudio, "Compartir vía"));
            }else {
                Toast.makeText( getApplicationContext ()," Selecciona un archivo ",Toast.LENGTH_SHORT).show();
            }
}

private void addToQueue( View v){
        adapterSelected.addToQueue(v);
}
    private void delete(){
                try {
                    int iter =0 ;
                    for(Items hold: adapterSelected .getSelected()){
                        if(hold.isCheckbox()){
                            iter ++;
                        }
                    }
                    if ( iter > 0 ){
                        alerta = new AlertDialog.Builder(ElementSelect. this );
                        alerta. setMessage("¿ Eliminar "+iter+" archivos ?")
                                . setCancelable(false)
                                . setPositiveButton("si", new DialogInterface. OnClickListener(){
                                    @Override
                                    public void onClick (DialogInterface dialog, int which ){
                                        adapterSelected.deleteMusic();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface. OnClickListener(){
                                    @Override
                                    public void onClick (DialogInterface dialog, int which ){
                                        dialog. cancel();
                                    }
                                });
                        titulo = alerta. create();
                        titulo. setTitle("Se eliminará del dispositivo");
                        titulo. show();
                    }else {
                        Toast.makeText( getApplicationContext ()," Selecciona  un archivo ",Toast.LENGTH_SHORT).show();
                    }



                }catch (Exception e ){
                    Toast.makeText( getApplicationContext (),e+"  ",Toast.LENGTH_SHORT).show();
                }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicBackground.setBackground(setBackground,this);
    }



    public Bitmap getAlbumImagen (String path ){
        android.media.MediaMetadataRetriever mmr =new MediaMetadataRetriever();
        mmr. setDataSource(path);
        byte[] data = mmr. getEmbeddedPicture();
        if ( data != null ) return BitmapFactory. decodeByteArray(data, 0, data.length ) ;
        return null ;
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_musicas_seleccionadas ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==R.id.itemMS1 ) {

            try {
                musicFilesElementSelected = adapterSelected.get_E_seleccionados();
                if (musicFilesElementSelected.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                    intent.putExtra("position", 0).putExtra("elemtSelected", 12);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), " Selecciona un archivo ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                Toast.makeText(getApplicationContext(), " " + exception, Toast.LENGTH_LONG).show();
            }

        }else if( item.getItemId() ==R.id.itemMS2) {
            delete();

        }else if( item.getItemId() ==R.id.itemMS3 ){
            share();

        }else if( item.getItemId() ==R.id.itemMS5){
            addToQueue(item.getActionView());

        }
        return true;
    }





}