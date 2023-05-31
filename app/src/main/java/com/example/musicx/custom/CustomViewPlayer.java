package com.example.musicx.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class CustomViewPlayer extends CustomView {

    public CustomViewPlayer(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public CustomViewPlayer(Context context , Bitmap bitmap , String title , LinearLayout container ) {
    super(context,bitmap,title,container);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            int width = getWidth();
            int height = getHeight();
            setVisibility(VISIBLE);
            if(width > 0 && height > 0) {
                param=width;
                if (param>height) param=height;

                param=param-(param/7);

                params.height = param;
                params.width = param;

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
