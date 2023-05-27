package com.podplay.android.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun ClickableDescription(
    text: String,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val pattern = Regex("""\bhttps?://\S+\b""") // Regular expression to match URLs
    val annotatedString = buildAnnotatedString {
        append(text)
        val matches = pattern.findAll(text)
        matches.forEach { matchResult ->
            val link = matchResult.value
            val startIndex = matchResult.range.first
            val endIndex = matchResult.range.last + 1
            addStyle(
                style = SpanStyle(textDecoration = TextDecoration.Underline),
                start = startIndex,
                end = endIndex,
            )
            addStringAnnotation(
                tag = "LINK",
                annotation = link,
                start = startIndex,
                end = endIndex,
            )
        }
    }
    SelectionContainer {
        ClickableText(
            text = annotatedString,
            onClick = {
                val annotations = annotatedString.getStringAnnotations("LINK", it, it)
                annotations.firstOrNull()?.let { stringAnnotation ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stringAnnotation.item))
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(
                            context,
                            "No application found to open URL",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier,
        )
    }
}
