package com.martin.greendragonfly.data

import android.util.Log

class HomeworkRepository() {

    fun getClass() {
        Log.d(TAG, classUrl)

    }

    companion object {
        private val TAG = "HomeworkRepo"
        private val classUrl = "http://www.lqtedu.com/parent/par-note.jsp"
    }
}