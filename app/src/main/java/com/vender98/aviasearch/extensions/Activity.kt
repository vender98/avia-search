package com.vender98.aviasearch.extensions

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AlertDialog
import com.vender98.aviasearch.R

fun Activity.showAlertDialog(
    title: CharSequence? = null,
    message: CharSequence,
    positiveText: CharSequence = getString(R.string.common_ok),
    negativeText: CharSequence? = null,
    onPositiveCallback: () -> Unit = { },
    onNegativeCallback: () -> Unit = { },
    onDialogDismissed: () -> Unit = { },
    isCancelable: Boolean = true
): Dialog =
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            dialog.dismiss()
            onPositiveCallback()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            dialog.dismiss()
            onNegativeCallback()
        }
        .setOnDismissListener { onDialogDismissed() }
        .setCancelable(isCancelable)
        .create()
        .also { it.show() }