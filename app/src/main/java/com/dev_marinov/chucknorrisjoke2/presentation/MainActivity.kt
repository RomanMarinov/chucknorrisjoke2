package com.dev_marinov.chucknorrisjoke2.presentation

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.dev_marinov.chucknorrisjoke2.R
import com.dev_marinov.chucknorrisjoke2.databinding.ActivityMainBinding
import com.dev_marinov.chucknorrisjoke2.databinding.WindowsAlertdialogExitBinding
import com.dev_marinov.chucknorrisjoke2.presentation.jokes.JokesFragment


class MainActivity : AppCompatActivity() {

    private lateinit var viewModelStatusDialogExit: ViewModelStatusDialogExit
    private lateinit var binding: ActivityMainBinding
    private var mySavedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySavedInstanceState = savedInstanceState

        viewModelStatusDialogExit = ViewModelProvider(this)[ViewModelStatusDialogExit::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // при создании макета проверяем статус был ли перед созданием макета открыт диалог
        // если да (true), значит запустим его снова
        if (viewModelStatusDialogExit.status) {
            myAlertDialog()
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        myAlertDialog()
    }

    private fun myAlertDialog() {
        val bindingAlertDialogExit: WindowsAlertdialogExitBinding = DataBindingUtil
            .inflate(LayoutInflater.from(this), R.layout.windows_alertdialog_exit, null, false)

        val dialog = Dialog(this)
        dialog.setContentView(bindingAlertDialogExit.root)
        dialog.setCancelable(true)
        dialog.show()

        // костыль для повторного открытия диалога если перевернули экран
        viewModelStatusDialogExit.status = true
        dialog.setOnDismissListener {
            viewModelStatusDialogExit.status = false
        }

        bindingAlertDialogExit.tvTitle.text = resources.getString(R.string.do_you_wish)
        bindingAlertDialogExit.btNo.text = resources.getString(R.string.no)
        bindingAlertDialogExit.btYes.text = resources.getString(R.string.yes)

        bindingAlertDialogExit.btNo.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }
        bindingAlertDialogExit.btYes.setOnClickListener {
            dialog.dismiss()
            finish()
        }
    }
}