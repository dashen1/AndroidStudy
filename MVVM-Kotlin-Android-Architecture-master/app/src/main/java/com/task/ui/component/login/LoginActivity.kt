package com.task.ui.component.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.task.data.Resource
import com.task.data.dto.login.LoginResponse
import com.task.databinding.LoginActivityBinding
import com.task.ui.base.BaseActivity
import com.task.ui.component.recipes.RecipesListActivity
import com.task.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Created by AhmedEltaher
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("LogUtils", "onCreate执行开始")
//        var job = GlobalScope.launch(Dispatchers.Main) {
//            Log.d("LogUtils", "主线程： " + Thread.currentThread())
//            val asyncs = async(Dispatchers.IO) {
//                Thread.sleep(2000)
//                Log.d("LogUtils","子线程： " + Thread.currentThread())
//                "耗时执行完毕"
//            }
//            Log.d("LogUtils", "执行于此")
//            Log.d("LogUtils", asyncs.await())
//            Log.d("LogUtils", "launch执行结束")
//        }
//        Thread.sleep(5000)
//        Log.d("LogUtils", "onCreate 1执行结束")
//        Log.d("LogUtils", "onCreate 2执行结束")
//        Log.d("LogUtils", "onCreate 3执行结束")

        GlobalScope.launch(Dispatchers.Main) {
            Log.d("LogUtils","launch开始")
            Log.d("LogUtils","launch结束")
        }

    Log.d("LogUtils","执行一个自定义suspend修饰方法");


        binding.login.setOnClickListener { doLogin() }
    }

    override fun initViewBinding() {
        binding = LoginActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun observeViewModel() {
        observe(loginViewModel.loginLiveData, ::handleLoginResult)
        observeSnackBarMessages(loginViewModel.showSnackBar)
        observeToast(loginViewModel.showToast)
    }

    private fun doLogin() {
        loginViewModel.doLogin(
            binding.username.text.trim().toString(),
            binding.password.text.toString()
        )
    }

    private fun handleLoginResult(status: Resource<LoginResponse>) {

        when (status) {
            is Resource.Loading -> binding.loaderView.toVisible()
            is Resource.Success -> status.data?.let {
                binding.loaderView.toGone()
                navigateToMainScreen()
            }
            is Resource.DataError -> {
                binding.loaderView.toGone()
                status.errorCode?.let { loginViewModel.showToastMessage(it) }
            }
        }
    }

    private fun navigateToMainScreen() {
        val nextScreenIntent = Intent(this, RecipesListActivity::class.java)
        startActivity(nextScreenIntent)
        finish()
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }
}
