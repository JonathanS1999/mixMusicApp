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

public class ThemeJS extends View
{
    private Context context;
    private Bitmap bitmap;
    private  Paint paint;
    private Path path;
    private int height = 0;
    private int width = 0;
    private int medioY = 0;
    private int medioX = 0;
    private Shader shader ;
    private String colorA;
    private String colorB;
    private float inicioFrameX=0;
    private float inicioFrameY=0;

    private CornerPathEffect cornerPathEffect;

    private float x2=0;
    private float x1=0;

    private float y2=0;
    private float y1=0;
    private String tite , duration , numIndex , artist;

    public ThemeJS(final Context context, final Bitmap bitmap, String colorA
            , String colorB , String tite , String duration , String numIndex , String artist) {
        super(context);
        this.context = context;
        this.bitmap = bitmap;
        this. colorA=colorA;
        this. colorB=colorB;
        this.tite=tite;
        this.duration=duration;
        this.numIndex=numIndex;
        this.artist=artist;
        paint = new Paint();
        cornerPathEffect = new CornerPathEffect(10);
    }

    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        height=this.getHeight();
        width =this.getWidth();
        medioY = height / 2;
        medioX = width / 2;

        try {
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6.0f);
            paint.setPathEffect(cornerPathEffect);
            paint.setMaskFilter(new BlurMaskFilter(1.5f, BlurMaskFilter.Blur.NORMAL));

            paint.setShader((Shader) new LinearGradient(0.0f, 0.0f, (float) width, (float) height, new int[]{Color.parseColor("#40" + colorA), Color.parseColor("#40" + colorB), Color.TRANSPARENT}, (float[]) null, Shader.TileMode.CLAMP));


            inicioFrameX = width - (width * 0.95f);
            inicioFrameY = height - (height * 0.95f);

            float inicX = width - (width * 0.97f);

            drawFrame(inicX, inicioFrameX, inicioFrameY, (float) width, (float) height,
                    canvas, (float) width - (width * 0.05f));


            paint.setShader((Shader) new LinearGradient(0.0f, 0.0f, (float) width, (float) height, new int[]{Color.parseColor("#" + colorA), Color.parseColor("#" + colorB), Color.TRANSPARENT}, (float[]) null, Shader.TileMode.CLAMP));

            inicioFrameX = width - (width * 0.97f);
            inicioFrameY = height - (height * 0.97f);

            inicX = width - (width * 1f);


            drawFrame(inicX, inicioFrameX, inicioFrameY, width - (width * 0.05f), height - (height * 0.05f),
                    canvas, (float) (width - (width * 0.10f)));


            drawPoligonEffect(canvas);


            inicioFrameX = width - (width * 0.97f);
            inicioFrameY = height - (height * 0.97f);

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6.0f);
            paint.setStyle(Paint.Style.STROKE);
            paint.setPathEffect(cornerPathEffect);

            paint.setShader((Shader) new LinearGradient(0.0f, 0.0f, (float) width / 6, (float) height, new int[]{Color.parseColor("#508E24AA"), Color.parseColor("#409C27B0")}, (float[]) null, Shader.TileMode.CLAMP));

            path = new Path();
            path.moveTo(inicioFrameX, inicioFrameY);
            path.lineTo((float) (width / 6), (float) medioY);
            path.lineTo(0.0f, (float) (height - 10));
            path.lineTo(inicioFrameX, inicioFrameY);
            path.close();

            //canvas.drawPath(path, paint);


            float[] mVerts = {
                    inicioFrameX, inicioFrameY,
                    (float) (width / 6), (float) medioY,
                    0.0f, (float) (height - 10),
                    0.0f, (float) (height - 10)
            };
            // canvas.drawBitmapMesh(bitmap, 1, 1, mVerts, 0, null, 0, null);
            canvas.drawBitmapMesh(bitmap, 1, 1, mVerts, 0, null, 0, null);
            canvas.drawPath(path, paint);


            paint = new Paint();
            paint.setTextSize(18);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(4.0f);


            paint.setShader((Shader) new LinearGradient(0.0f, 0.0f, (float) width, (float) height, new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")}, (float[]) null, Shader.TileMode.CLAMP));

            canvas.drawText(tite, medioX - (medioX*0.50f), medioY - 20, paint);
            canvas.drawText(artist, medioX - (medioX*0.50f), medioY, paint);

            canvas.drawText(numIndex, (width / 6) + 6, (float) medioY + 6, paint);
            canvas.drawText(duration, width - (width * 0.135f), medioY, paint);

        }catch (Exception e){
            Toast.makeText(context, e +" js theme 138 ",Toast.LENGTH_LONG).show();
        }
    }


    private void drawPoligonEffect(Canvas canvas ){


        float finalx=width-(width*0.10f);
        float finaly=height-(height*0.10f);


        EcLineal ec = new EcLineal();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1.0f);
        paint.setPathEffect(cornerPathEffect );
        paint.setStyle(Paint.Style.FILL);

        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width, (float)height, new int[] { Color.parseColor("#508E24AA"), Color.parseColor("#409C27B0") }, (float[])null, Shader.TileMode.CLAMP));

        path = new Path();

        x2=  (float) (width - (width * 0.10f));
        x1= width-(width*0.05f);

        y2= height-(height*0.05f);
        y1= inicioFrameY;

        float  med= medioY- (medioY*0.40f);

        float ix =ec.getX(x2,x1,y2,y1, med );
        path.moveTo(ix , med);

        med= medioY+ (medioY*0.40f);
        ix =ec.getX(x2,x1,y2,y1,  med );
        path.lineTo(ix , med);

        x2=  (float) (width - (width * 0.20f));
        x1= width-(width*0.15f);


        ix =ec.getX(x2,x1,y2,y1, med );
        path.lineTo(ix , med);

        med=medioY- (medioY*0.40f);

        ix =ec.getX(x2,x1,y2,y1, med );
        path.lineTo(ix , med);
        path.close();


        canvas.drawPath(path, paint);



        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2.0f);
        paint.setPathEffect(new CornerPathEffect(10) );
        paint.setStyle(Paint.Style.STROKE);
        paint. setMaskFilter( new BlurMaskFilter( 8.5f, BlurMaskFilter.Blur.NORMAL));

        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width, (float)height, new int[] { Color.parseColor("#508E24AA"), Color.parseColor("#409C27B0") }, (float[])null, Shader.TileMode.CLAMP));

        path = new Path();
        x2=  (float) (width - (width * 0.10f));
        x1= width-(width*0.05f);

        y2= height-(height*0.05f);
        y1= inicioFrameY;

        med= medioY- (medioY*0.40f);

        ix =ec.getX(x2,x1,y2,y1, med );
        path.moveTo(ix , med);

        med= medioY+ (medioY*0.40f);
        ix =ec.getX(x2,x1,y2,y1,  med );
        path.lineTo(ix , med);

        x2=  (float) (width - (width * 0.20f));
        x1= width-(width*0.15f);


        ix =ec.getX(x2,x1,y2,y1, med );
        path.lineTo(ix , med);

        med=medioY- (medioY*0.40f);

        ix =ec.getX(x2,x1,y2,y1, med );
        path.lineTo(ix , med);
        path.close();

        canvas.drawPath(path, paint);







        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setPathEffect(new CornerPathEffect(10) );
        paint.setStrokeWidth(1.0f);
        paint.setStyle(Paint.Style.FILL);

        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width, (float)height, new int[] { Color.parseColor("#508E24AA"), Color.parseColor("#409C27B0") }, (float[])null, Shader.TileMode.CLAMP));


        inicioFrameX=width-(width*0.90f);
        inicioFrameY=height-(height*0.97f);


        path.moveTo(inicioFrameX, inicioFrameY);
        path.lineTo(width-(width*0.15f) , inicioFrameY);
        path.lineTo(width-(width*0.20f), medioY+7);
        path.lineTo((width/6)+(width*0.10f), medioY+7);
        path.close();


        canvas.drawPath(path, paint);






        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2.0f);
        paint.setPathEffect(new CornerPathEffect(10) );
        paint.setStyle(Paint.Style.STROKE);
        paint. setMaskFilter( new BlurMaskFilter( 8.5f, BlurMaskFilter.Blur.NORMAL));

        paint.setShader((Shader)new LinearGradient(0.0f, 0.0f, (float)width, (float)height, new int[] { Color.parseColor("#508E24AA"), Color.parseColor("#409C27B0") }, (float[])null, Shader.TileMode.CLAMP));

        path = new Path();
        path.moveTo(80 , 50);
        path.lineTo(width - 130 , 50);
        path.lineTo(width - 200, medioY+7);
        path.lineTo(250 , medioY+7);
        //path.lineTo(150 , medioY-30);
        path.close();

        canvas.drawPath(path, paint);



        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2.0f);
        paint.setPathEffect(new CornerPathEffect(10) );
        paint.setStyle(Paint.Style.STROKE);

        path = new Path();

        path.moveTo((float)(width - 80), 50.0f);
        path.lineTo(width - 100 , 80);
        canvas.drawPath(path, paint);

        path.moveTo((float)(width - 100), 50.0f);
        path.lineTo(width - 80 , 80);
        canvas.drawPath(path, paint);
    }




    private void  drawFrame(float pi, float inicioFrameX, float inicioFrameY, float width, float height, Canvas canvas, float pt ){
        path = new Path();
        path.moveTo(inicioFrameX, inicioFrameY);
        path.lineTo(width, inicioFrameY);
        path.lineTo(  pt ,   (float) height );
        path.lineTo(pi, height );
        path.close();

        canvas.drawPath(path, paint);
    }

}