package com.example.musicx.custom

import android.R.attr.bitmap
import android.R.color
import android.content.Context
import android.graphics.*
import android.view.View


class FrameTheme(context: Context?) : View(context) {


    private var paint= Paint()
    private var rect = Rect(0, 0, 0, 0)
    private var rectF = RectF()



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.setAntiAlias(true)
        paint.setStrokeWidth(4F)
        val heigth = getHeight()
        val width = getWidth()
        val medioY = heigth / 2
        val medioX = width / 2
        var shader: Shader? = LinearGradient(
            30F,
            30F,
            (width - 30).toFloat(),
            (heigth - 30).toFloat(),
            intArrayOf(Color.BLACK, Color.BLACK, Color.parseColor("#673AB7")),
            null,
            Shader.TileMode.CLAMP
        )
        paint.setShader(shader)


        rect.set(30, 30, width - 30, heigth - 30)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 20F, 20F, paint)

        shader = LinearGradient(
            0F,
            0F,
            width.toFloat(),
            heigth.toFloat(),
            intArrayOf(
                Color.parseColor("#C51162"),
                Color.rgb(8, 20, 150),
                Color.parseColor("#C51162"),
                Color.rgb(8, 20, 150)
            ),
            null,
            Shader.TileMode.CLAMP
        )
        paint.setShader(shader)

        //Mascaras
        //paint. setMaskFilter( new BlurMaskFilter(0. f, BlurMaskFilter.Blur.NORMAL));
        rect.set(width - 50, 20, width - 20, medioY - 20)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(20, 20, 50, medioY - 20)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(20, 20, medioX - 20, 50)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(20, heigth - 50, medioX - 20, heigth - 20)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(width - 50, medioY + 20, width - 20, heigth - 20)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(20, medioY + 20, 50, heigth - 20)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(medioX + 20, 20, width - 20, 50)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        rect.set(medioX + 20, heigth - 50, width - 20, heigth - 20)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)


        rect.set(width - 57, medioY - 35, width - 27, medioY + 35)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)


        rect.set(27, medioY - 35, 57, medioY + 35)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)


        rect.set(medioX - 45, 27, medioX + 45, 57)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)


        rect.set(medioX - 45, heigth - 57, medioX + 45, heigth - 27)
        rectF.set(rect)
        canvas.drawRoundRect(rectF, 15F, 15F, paint)

        shader = RadialGradient(
            medioX.toFloat(),
            medioY.toFloat(),
            heigth.toFloat(),
            intArrayOf(Color.WHITE, Color.parseColor("#C51162")),
            null,
            Shader.TileMode.CLAMP
        )

        paint.setShader(shader)

        //Mascaras
        paint.setMaskFilter(BlurMaskFilter(0.5f, BlurMaskFilter.Blur.NORMAL))
        run {
            var i = medioY - 25
            while (i < medioY + 25) {
                canvas.drawLine((width - 50).toFloat(), i.toFloat(),
                    (width - 30).toFloat(), (i + 10).toFloat(), paint)
                i = i + 10
            }
        }
        run {
            var i = medioY - 25
            while (i < medioY + 25) {
                canvas.drawLine(34F, (i + 10).toFloat(), 50F, i.toFloat(), paint)
                i = i + 10
            }
        }
        var i = medioX - 40
        while (i < medioX + 40) {
            canvas.drawLine(i.toFloat(), (heigth - 50).toFloat(),
                (i + 10).toFloat(), (heigth - 30).toFloat(), paint)
            i = i + 10
        }


        /* shader  = new RadialGradient( medioX, medioY, heigth,
         new int []{ Color. WHITE, Color.parseColor("#64B5F6"), Color. WHITE} , null , Shader.TileMode.CLAMP);

   paint. setShader(shader);
   paint. setStrokeWidth(1);

  canvas.drawLine(width-40,30, width-30, medioY-30, paint );

  canvas.drawLine(30,30, 30, medioY-30, paint );*/
    }


}