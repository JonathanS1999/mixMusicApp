package com.example.musicx.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.View;
import android.widget.Toast;

import com.example.musicx.equations.EcLineal;


public class ViewCardImg extends View {

    private Context context ;
    private Bitmap bitmap ;
    private Paint paint ;
    private int height = 0;
    private int width = 0;
    private int medioY = 0;
    private int medioX = 0;
    private float starty1=0;
    private float starty2=0;

    private Path path ;
    private float x2=0;
    private float x1=0;

    private float y2=0;
    private float y1=0;



    private int nfilas=1;
    private int ncolumnas=1;

    private EcLineal ec ;

    public ViewCardImg (Context context, Bitmap bitmap ){
        super( context );
        ec=new EcLineal();
        this.bitmap =bitmap ;
        this.context = context ;
        paint = new Paint();
    }

    @Override
    public void onDraw( Canvas canvas ){
        super.onDraw(canvas);
        paint=new Paint ();
        height=this.getHeight()-(int)(this.getHeight()*0.05);
        width =this.getWidth()-(int)(this.getWidth()*0.05);
        medioY = height / 2;
        medioX = width / 2;
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10.0f);
        paint.setPathEffect(new CornerPathEffect(10) );
        paint.setMaskFilter( new BlurMaskFilter( 1f, BlurMaskFilter.Blur.NORMAL));

        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width , (float)height, new int[] { Color.parseColor("#000000"), Color.parseColor("#000000") }, (float[])null, Shader.TileMode.CLAMP));

        path = new Path();
        starty1=this.getHeight()-(int)(this.getHeight()*0.92f);
        starty2=height-(height*0.08f);
        path.moveTo(width-(width*0.695f), this.getHeight()-(int)(this.getHeight()*0.95f) );
        path.lineTo(width-(width*0.305f), starty1 );
        path.lineTo(width-(width*0.305f) , height-(height*0.07f) );
        path.lineTo(width-(width*0.695f), this.getHeight()-(int)(this.getHeight()*0.035f) );
        path.close();
        canvas.drawPath(path, paint);
        paint.setMaskFilter( new BlurMaskFilter( 15f, BlurMaskFilter.Blur.NORMAL));
        canvas.drawPath(path, paint);
        paint.setMaskFilter( new BlurMaskFilter( 1.5f, BlurMaskFilter.Blur.NORMAL));


        nfilas=6;
        ncolumnas=6;



        int nElements = (  (nfilas+1)*(ncolumnas+1)  )*2;

        // buscando altura de ambas esquina
        float  diY1Y2 = (float)height-
                (this.getHeight()-(int)(this.getHeight()*0.95)) ;//............................

        float pointsYi=diY1Y2/nfilas;

        float  dfY1Y2 = starty2 - starty1 ;

        float pointsYf=dfY1Y2/nfilas;

        float  dix1x2 =(   width-(width*0.31f)     ) -  (  width-(width*0.69f)    )  ;

        float pointsXi=dix1x2/ncolumnas;

        float [] arrayCord= new float[nElements];

        try{




            int fils=0;
            int i=0;
            while (i<arrayCord.length ){
                for ( int j=0 ; j <= ncolumnas ; j++){
                    //0 - 2 - 4 - 6
                    arrayCord[i]=(width-(width*0.69f))+((pointsXi)*j);
                    i++;
                    //1 - 3 - 5 - 7
                    arrayCord[i]=ec.getY(
                            width-(width*0.31f),
                            width-(width*0.69f),
                            ( starty1+ (pointsYf*fils) ),

                            this.getHeight()-
                                    (int)(this.getHeight()*0.95) + (pointsYi*fils ),
                            arrayCord[(i-1)]
                    );
                    i++;
                }
                fils++;
            }
        }catch (Exception e){
            Toast.makeText(context, "  aqui? "+e, Toast.LENGTH_LONG ). show ();
        }

        canvas.drawBitmapMesh(bitmap, ncolumnas, nfilas, arrayCord, 0, null, 0, null);

        paint.setStyle(Paint.Style.STROKE);

        path = new Path();
        path.moveTo(width-(width*0.69f), this.getHeight()-(int)(this.getHeight()*0.95));
        path.lineTo(width-(width*0.31f), starty1);
        path.lineTo(width-(width*0.31f), starty2 );
        path.lineTo(width-(width*0.69f), (float) height );
        path.close();
        paint.setStrokeWidth(10.0f);
        paint.setStyle(Paint.Style.STROKE);


        canvas.drawPath(path, paint);
        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width, (float)height, new int[] { Color.parseColor("#50673AB7"), Color.parseColor("#50673AB7") }, (float[])null, Shader.TileMode.CLAMP));


        canvas.drawPath(path, paint);




        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width/6, (float)height, new int[] { Color.parseColor("#8E24AA"), Color.parseColor("#9C27B0") }, (float[])null, Shader.TileMode.CLAMP));
        paint.setStrokeWidth(5.0f);
        paint.setMaskFilter( new BlurMaskFilter( 4f, BlurMaskFilter.Blur.NORMAL));
        canvas.drawPath(path, paint);



        x1=width-(width*0.31f);
        y1=starty2;

        x2=width-(width*0.69f);
        y2=(float)height;

        EcLineal ec = new EcLineal();

        path = new Path();
        path.moveTo( width-(width*0.31f) , (float)medioY+(medioY*0.35f) );
        path.lineTo( width-(width*0.50f) , ec.getY(x2, x1, y2, y1,width-(width*0.50f) ) );
        path.lineTo(width-(width*0.31f), starty2);
        path.close();
        canvas.drawPath(path, paint);

        paint.setShader((Shader)new LinearGradient(width-(width*0.50f), ec.getY(x2, x1, y2, y1,width-(width*0.50f) ) , width-(width*0.31f), starty2 , new int[] { Color.parseColor("#909C27B0"), Color.parseColor("#000000") }, (float[])null, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        paint.setMaskFilter( new BlurMaskFilter( 0.5f, BlurMaskFilter.Blur.NORMAL));
        canvas.drawPath(path, paint);




        paint.setStrokeWidth(10.0f);
        paint.setPathEffect(new CornerPathEffect(10) );
        paint.setMaskFilter( new BlurMaskFilter( 1f, BlurMaskFilter.Blur.NORMAL));

        paint.setShader((Shader)new LinearGradient(width-(width*0.695f), this.getHeight()-(int)(this.getHeight()*0.95f), width-(width*0.305f), height-(height*0.07f), new int[] { Color. TRANSPARENT, Color.parseColor("#80000000") }, (float[])null, Shader.TileMode.CLAMP));

        path = new Path();
        starty1=this.getHeight()-(int)(this.getHeight()*0.92f);
        starty2=height-(height*0.08f);
        path.moveTo(width-(width*0.695f), this.getHeight()-(int)(this.getHeight()*0.95f) );
        path.lineTo(width-(width*0.305f), starty1 );
        path.lineTo(width-(width*0.305f) , height-(height*0.07f) );
        path.lineTo(width-(width*0.695f), this.getHeight()-(int)(this.getHeight()*0.035f) );
        path.close();
        canvas.drawPath(path, paint);

        paint=new Paint ();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2.0f);

        paint. setColor(Color.YELLOW );



    }

}