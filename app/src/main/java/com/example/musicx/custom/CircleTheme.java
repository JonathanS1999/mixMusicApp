package com.example.musicx.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class CircleTheme extends View {

   private Paint paint ;
   private Context context;
   private Random alet;

    public CircleTheme(Context context ) {
        super(context);
            this.context = context;
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(3.7f);
            paint.setStyle(Paint.Style.STROKE);
            alet = new Random();
    }

    public CircleTheme(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            Shader shader;
            int width = getWidth();
            int height = getHeight();
            if(width>height) width=height;
            else if(height>width) height=width;
            if (alet.nextInt(2) == 1) {
                shader = new RadialGradient(width / 2, height / 2, height/2,
                        new int[]{
                                Color.rgb(1 + alet.nextInt(254)
                                        , 1 + alet.nextInt(254),
                                        1 + alet.nextInt(254)),
                                Color.rgb(1 + alet.nextInt(254),
                                        1 + alet.nextInt(254),
                                        1 + alet.nextInt(254)),
                                Color.rgb(1 + alet.nextInt(254),
                                        1 + alet.nextInt(254),
                                        1 + alet.nextInt(254))}, null, Shader.TileMode.CLAMP);
            } else {
                shader = new LinearGradient(0, 0, width, height,
                        new int[]{
                                Color.rgb(1 + alet.nextInt(254),
                                        1 + alet.nextInt(254),
                                        1 + alet.nextInt(254)),
                                Color.rgb(1 + alet.nextInt(254),
                                        1 + alet.nextInt(254),
                                        1 + alet.nextInt(254)),
                                Color.rgb(1 + alet.nextInt(254),
                                        1 + alet.nextInt(254),
                                        1 + alet.nextInt(254))}, null, Shader.TileMode.CLAMP);
            }
            paint.setShader(shader);
            int rad = getWidth() / 2;
            if (rad > (int) (getHeight() / 2)) rad = (getHeight() / 2);
            canvas.drawCircle(rad , rad , rad - 2, paint);
        }catch (Exception e){
            Toast.makeText(context , e+" 72 circle theme",Toast.LENGTH_LONG).show();
        }
    }
}
