package com.example.newtrackmed.util

import android.content.Context
import androidx.annotation.StringRes

interface ResourceWrapper {
    fun getString(@StringRes id: Int): String
}

class ContextResourceWrapper(private val context: Context) : ResourceWrapper {
    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}