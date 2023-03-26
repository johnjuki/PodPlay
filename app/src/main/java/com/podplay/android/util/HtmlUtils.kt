package com.podplay.android.util

import android.text.Html
import android.text.Spanned

object HtmlUtils {
    fun htmlToSpannable(htmlDesc: String): Spanned {
        var newHtmlDesc = htmlDesc.replace("\n".toRegex(), "")
        newHtmlDesc = newHtmlDesc.replace(
            "(<(/)img>)|(<img.+?>)".toRegex(), ""
        )
        return Html.fromHtml(
            newHtmlDesc,
            Html.FROM_HTML_MODE_LEGACY
        )
    }
}