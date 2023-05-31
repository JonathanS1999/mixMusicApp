package com.example.musicx.stylesFonts

import android.text.SpannableString

class SearchSpannables(spannable1: SpannableString, spannable2: SpannableString) {

   private var spannableTitle : SpannableString ? = spannable1
   private var spannableArtist : SpannableString ? = spannable2

    fun getTitleSpannable(): SpannableString? {
        return spannableTitle
    }

    fun getArtistSpannable(): SpannableString? {
        return spannableArtist
    }
}