package umc.onairmate.ui.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import umc.onairmate.ui.pop_up.LoadingDialogFragment

private var loadingDialog: LoadingDialogFragment? = null

fun handleLoadingDialog(host: Any, isLoading: Boolean) {
    val fragmentManager: FragmentManager = when (host) {
        is FragmentActivity -> host.supportFragmentManager
        is Fragment -> host.parentFragmentManager
        else -> throw IllegalArgumentException("Host must be FragmentActivity or Fragment")
    }

    if (isLoading) {
        if (loadingDialog?.isAdded == true) return
        loadingDialog = LoadingDialogFragment()
        loadingDialog?.show(fragmentManager, "loading")
    } else {
        loadingDialog?.dismissAllowingStateLoss()
        loadingDialog = null
    }
}