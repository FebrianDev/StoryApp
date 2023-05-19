package com.febrian.storyapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast

class Helper(private val context: Context) {

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun moveActivity(c: Context, targetActivity: Activity) {
        val intent = Intent(c, targetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        c.startActivity(intent)
    }

    fun moveActivityWithFinish(activity: Activity, targetActivity: Activity) {
        val intent = Intent(context, targetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        activity.finish()
    }

    fun showLoading(isLoading: Boolean, view: View) {
        view.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun setEnableView(isEnable: Boolean, view: View) {
        view.isEnabled = isEnable
    }

}