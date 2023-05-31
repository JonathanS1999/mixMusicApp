package com.example.musicx.custom

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import com.example.musicx.R

class AddFrameCircle(
    container: LinearLayout,
    context: Context,
    roundedBitmapDrawable: RoundedBitmapDrawable?,
    params: LinearLayout.LayoutParams,
    paramsR: RelativeLayout.LayoutParams
) {

    private var  image:ImageView

    init {
        val containerer2 = RelativeLayout(context)
        image = ImageView(context)
        image.setImageDrawable(roundedBitmapDrawable)
        image.setPadding(2, 2, 2, 2)
        container.addView(containerer2, params)
        image.setLayoutParams(paramsR)
        containerer2.addView(image)
        val circleTheme = CircleTheme(context)
        circleTheme.layoutParams = paramsR
        containerer2.addView(circleTheme)
    }

    fun getImageView():ImageView{
        return image
    }

}