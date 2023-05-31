package com.example.musicx.DialogSeekBarTime

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import com.example.musicx.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogSeekBarTime : BottomSheetDialogFragment() {

private lateinit var seekBar : SeekBar
private  lateinit var onTimeSelected: OnTimeSelected

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnTimeSelected){
            onTimeSelected = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.layout_time_selected, container, false)
        seekBar = v.findViewById(R.id.seekBar)
       dialog!!.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
       dialog!!.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return v
    }


    override fun onResume() {
        super.onResume()
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    try {
                        onTimeSelected.setTimeSelected(seekBar.progress)
                    }catch(exception : Exception){
                        Toast.makeText(context ,"${exception}  dilog timer 55",Toast.LENGTH_SHORT).show()
                    }
                }
            })
            seekBar.max = 60000
        seekBar.progress = onTimeSelected.getTime()
    }



    interface OnTimeSelected{
        fun setTimeSelected(time: Int)
        fun getTime() : Int
    }
}