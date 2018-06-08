package com.gabar.kotlinwithfirebase.utilities

import android.content.Context
import android.content.SharedPreferences


class SharedPrefUtil(private val mContext: Context) {
    private var mSharedPreferences: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null


    fun saveString(key: String, value: String) {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences!!.edit()
        mEditor!!.putString(key, value)
        mEditor!!.commit()
    }

    fun saveInt(key: String, value: Int) {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences!!.edit()
        mEditor!!.putInt(key, value)
        mEditor!!.commit()
    }

    fun saveBoolean(key: String, value: Boolean) {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences!!.edit()
        mEditor!!.putBoolean(key, value)
        mEditor!!.commit()
    }


    fun getString(key: String): String? {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(key, null)
    }

    fun getString(key: String, defaultValue: String): String? {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(key, defaultValue)
    }

    fun getInt(key: String): Int {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getInt(key, defaultValue)
    }

    fun getBoolean(key: String): Boolean {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun clear() {
        mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE)
        mSharedPreferences!!.edit().clear().apply()
    }
}
