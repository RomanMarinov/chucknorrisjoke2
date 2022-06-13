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
import androidx.lifecycle.get
import com.airbnb.lottie.LottieAnimationView
import com.dev_marinov.chucknorrisjoke2.R
import com.dev_marinov.chucknorrisjoke2.databinding.ActivityMainBinding
import com.dev_marinov.chucknorrisjoke2.databinding.WindowsAlertdialogExitBinding

import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var viewModelStatusDialogExit: ViewModelStatusDialogExit
    private lateinit var binding: ActivityMainBinding
    var mySavedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        Log.e("333","=MainActivity=")
        mySavedInstanceState = savedInstanceState

        viewModelStatusDialogExit = ViewModelProvider(this)[ViewModelStatusDialogExit::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportActionBar?.hide() // скрыть экшен бар
        setWindow() // установки окна

        // при создании макета проверяем статус был ли перед созданием макета открыт диалог
        // если да (true), значит запустим его снова
        if (viewModelStatusDialogExit.status) {
            myAlertDialog()
        }

        val runnable1 = Runnable{ // анимация шарики при старте
            binding.animationView.playAnimation()
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable1, 0)
        binding.animationView.cancelAnimation()

        val runnable2 = Runnable{ // задержка 1,5 сек перед переходом во FragmentList
            if(mySavedInstanceState == null) {

            val fragmentList = FragmentList()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.llFragList, fragmentList)
            fragmentTransaction.commit()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable2, 1500)
    }

    private fun setWindow() {
        val window = window
        // FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS Флаг, указывающий, что это Окно отвечает за отрисовку фона для системных полос.
        // Если установлено, системные панели отображаются с прозрачным фоном, а соответствующие области в этом окне заполняются
        // цветами, указанными в Window#getStatusBarColor()и Window#getNavigationBarColor().
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent); // прозрачный статус бар
        window.navigationBarColor  = ContextCompat.getColor(this, android.R.color.black); // черный бар навигации
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
        bindingAlertDialogExit.btYes.setOnClickListener{
            dialog.dismiss()
            finish()
        }
    }
}