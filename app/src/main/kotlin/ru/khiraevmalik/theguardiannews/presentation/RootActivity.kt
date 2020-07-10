package ru.khiraevmalik.theguardiannews.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.khiraevmalik.theguardiannews.R
import ru.khiraevmalik.theguardiannews.routing.RootNavigator

class RootActivity : AppCompatActivity() {

    private val vm by viewModel<RootActivityViewModel>()

    private val navigator by lazy {
        RootNavigator(this, R.id.main_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) vm.openNewsListAsRoot()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        vm.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        vm.removeNavigator()
    }

}
