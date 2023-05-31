package com.example.musicx.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.util.Random;

public class CustomView extends View {

    protected Context context ;
    protected int  aX=0, aY=0;
    protected String title ="";
    protected Bitmap bitmap;
    protected Random alet ;
    protected LinearLayout.LayoutParams params;
    protected RelativeLayout.LayoutParams paramsR;
    protected LinearLayout container;
    protected ImageView image ;
    protected int param=0;

    public CustomView(Context context, AttributeSet attrs){
    super(context,attrs);
    }

    public CustomView(Context context , Bitmap bitmap , String title , LinearLayout container ) {
        super(context);
        this.context = context;
        this.bitmap = bitmap;
        this.title = title;
        this.container = container;
        alet = new Random();
        params = new LinearLayout.LayoutParams (param , param );
        paramsR = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsR.addRule(RelativeLayout.CENTER_IN_PARENT);
    }









    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
        int width = getWidth();
        int height = getHeight();
        setVisibility(VISIBLE);
        if(width > 0 && height > 0) {

                aX = alet.nextInt(width);
                if (aX < ((width / 2) - (width / 8))) aX = ((width / 2) - (width / 8));

                aY = alet.nextInt(height);
                if (aY < ((height / 2)) - (height / 8)) aY = (height / 2) - (height / 8);

                param = aX;
                if (param > aY) param = aY;

                params.height = param;
                params.width = param;
                params.leftMargin = alet.nextInt(getWidth() - param);
                params.topMargin = alet.nextInt(getHeight() - param);

                Bitmap resize = Bitmap.createScaledBitmap(bitmap, param-2 , param-2 , true);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resize);
                roundedBitmapDrawable.setCircular(true);

                if (container.getChildCount() > 0) container.removeAllViews();

                new AddFrameCircle(container,context,roundedBitmapDrawable,params,paramsR);

                setVisibility(View.GONE);

        }else{
            if (container.getChildCount() > 0) container.removeAllViews();
        }
    } catch (Exception e) {
        Toast.makeText(context, e + "aX=" + aX + "  aY=" + aY + "  param" + param, Toast.LENGTH_LONG).show();
    }
    }





    protected void ergk(Canvas canvas) {
        try {
            int width = getWidth();
            int height = getHeight();
            setVisibility(VISIBLE);
            if(width > 0 && height > 0) {
                param=width;
                if (param>height) param=height;

                params.height = param;
                params.width = param;
                params.leftMargin = alet.nextInt(getWidth() - param);
                params.topMargin = alet.nextInt(getHeight() - param);

                Bitmap resize = Bitmap.createScaledBitmap(bitmap, param-2 , param-2 , true);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resize);
                roundedBitmapDrawable.setCircular(true);

                if (container.getChildCount() > 0) container.removeAllViews();

                new AddFrameCircle(container,context,roundedBitmapDrawable,params,paramsR);

                setVisibility(View.GONE);

            }else{
                if (container.getChildCount() > 0) container.removeAllViews();
            }
        } catch (Exception e) {
            Toast.makeText(context, e + "aX=" + aX + "  aY=" + aY + "  param" + param, Toast.LENGTH_LONG).show();
        }
    }

}
