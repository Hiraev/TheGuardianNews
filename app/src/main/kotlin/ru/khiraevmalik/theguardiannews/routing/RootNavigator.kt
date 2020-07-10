package ru.khiraevmalik.theguardiannews.routing

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class RootNavigator(
        activity: FragmentActivity,
        @IdRes containerId: Int
) : SupportAppNavigator(activity, containerId) {

    override fun setupFragmentTransaction(
            command: Command,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction
    ) {

    }

}
