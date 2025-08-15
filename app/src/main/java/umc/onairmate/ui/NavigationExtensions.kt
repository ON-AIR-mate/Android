package umc.onairmate.ui.nav

import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

typealias NavControllerLiveData = MutableLiveData<NavController>

fun BottomNavigationView.setupWithNavController(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent
): LiveData<NavController> {

    val graphIdToTagMap = mutableMapOf<Int, String>()
    val selectedController = NavControllerLiveData()
    var firstGraphTag: String? = null

    // 그래프별 NavHostFragment 생성/재사용 및 초기 표시 설정
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = "bottomNav#$index"
        val navHostFragment = obtainNavHostFragment(
            fragmentManager, fragmentTag, navGraphId, containerId
        )

        // 메뉴 itemId와 동일한 startDestinationId를 key로 매핑
        val startDestinationId = navHostFragment.navController.graph.startDestinationId
        graphIdToTagMap[startDestinationId] = fragmentTag

        if (index == 0) {
            fragmentManager.beginTransaction()
                .setPrimaryNavigationFragment(navHostFragment)
                .show(navHostFragment)
                .commitNow()
            selectedController.value = navHostFragment.navController
            firstGraphTag = fragmentTag
        } else {
            fragmentManager.beginTransaction()
                .hide(navHostFragment)
                .commitNow()
        }
    }

    // 탭 전환 시 해당 NavHost로 전환
    setOnItemSelectedListener { item ->
        val newlySelectedTag = graphIdToTagMap[item.itemId] ?: return@setOnItemSelectedListener false
        val currentTag = graphIdToTagMap[selectedController.value?.graph?.startDestinationId] ?: firstGraphTag

        // 루트로 pop
        val targetController = (fragmentManager.findFragmentByTag(newlySelectedTag) as NavHostFragment).navController
        targetController.popBackStack(targetController.graph.startDestinationId, false)

        if (newlySelectedTag == currentTag) {
            return@setOnItemSelectedListener true
        }

        val newlySelectedFragment = fragmentManager.findFragmentByTag(newlySelectedTag) as NavHostFragment
        val currentFragment = fragmentManager.findFragmentByTag(currentTag!!) as NavHostFragment

        // 터치 이벤트 제어
        currentFragment.view?.apply {
            isClickable = false
            isFocusable = false
        }
        newlySelectedFragment.view?.apply {
            isClickable = true
            isFocusable = true
        }

        fragmentManager.beginTransaction()
            .hide(currentFragment)
            .show(newlySelectedFragment)
            .setPrimaryNavigationFragment(newlySelectedFragment)
            .setReorderingAllowed(true)
            .commit()

        selectedController.value = newlySelectedFragment.navController
        true
    }


    // 딥링크 처리(선택적)
    setupDeepLinks(
        navGraphIds, fragmentManager, graphIdToTagMap, containerId, intent
    ) { controller ->
        selectedController.value = controller
        this.selectedItemId = controller.graph.startDestinationId
    }

    return selectedController
}

private fun obtainNavHostFragment(
    fragmentManager: FragmentManager,
    fragmentTag: String,
    navGraphId: Int,
    containerId: Int
): NavHostFragment {
    val existing = fragmentManager.findFragmentByTag(fragmentTag) as? NavHostFragment
    if (existing != null) return existing
    val navHost = NavHostFragment.create(navGraphId)
    fragmentManager.beginTransaction()
        .add(containerId, navHost, fragmentTag)
        .commitNow()
    return navHost
}

private fun BottomNavigationView.setupDeepLinks(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    graphIdToTagMap: Map<Int, String>,
    containerId: Int,
    intent: Intent,
    onControllerReady: (NavController) -> Unit
) {
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = "bottomNav#$index"
        val navHostFragment = obtainNavHostFragment(fragmentManager, fragmentTag, navGraphId, containerId)
        val controller = navHostFragment.navController
        if (controller.handleDeepLink(intent)) {
            fragmentManager.beginTransaction()
                .setPrimaryNavigationFragment(navHostFragment)
                .show(navHostFragment)
                .apply {
                    graphIdToTagMap.values.filter { it != fragmentTag }
                        .forEach { hide(fragmentManager.findFragmentByTag(it)!!) }
                }
                .commitNow()
            onControllerReady(controller)
        }
    }
}
