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
    intent: Intent,
    onTabSelected: ((menuId: Int, controller: NavController) -> Unit)? = null,
    onTabReselected: ((menuId: Int, controller: NavController) -> Unit)? = null
): LiveData<NavController> {

    val startIdToTag = mutableMapOf<Int, String>()
    val selectedController = MutableLiveData<NavController>()
    var firstTag: String? = null

    // 탭별 NavHost 생성: 첫 탭 attach, 나머지 detach(뷰 제거 = 클릭 누수 원천 차단)
    navGraphIds.forEachIndexed { index, navGraphId ->
        val tag = "bottomNav#$index"
        val host = obtainNavHostFragment(fragmentManager, tag, navGraphId, containerId)
        val startId = host.navController.graph.startDestinationId
        startIdToTag[startId] = tag

        fragmentManager.beginTransaction().apply {
            if (index == 0) {
                attach(host)
                setPrimaryNavigationFragment(host)
                selectedController.value = host.navController
                firstTag = tag
            } else {
                detach(host)
            }
        }.setReorderingAllowed(true).commitNow()
    }

    // 탭 선택
    setOnItemSelectedListener { item ->
        val newTag = startIdToTag[item.itemId] ?: return@setOnItemSelectedListener false
        val curTag = startIdToTag[selectedController.value?.graph?.startDestinationId] ?: firstTag

        val newHost = fragmentManager.findFragmentByTag(newTag) as NavHostFragment

        // 대상 탭은 항상 루트로 정리
        newHost.navController.popBackStack(newHost.navController.graph.startDestinationId, false)

        if (newTag == curTag) {
            onTabReselected?.invoke(item.itemId, newHost.navController)
            return@setOnItemSelectedListener true
        }

        val curHost = fragmentManager.findFragmentByTag(curTag!!) as NavHostFragment

        fragmentManager.beginTransaction()
            .detach(curHost)          // 뷰 제거 → 뒤 화면 클릭 이슈 없음
            .attach(newHost)          // 새 탭 뷰 생성
            .setPrimaryNavigationFragment(newHost)
            .setReorderingAllowed(true)
            .commitNow()

        selectedController.value = newHost.navController
        onTabSelected?.invoke(item.itemId, newHost.navController)
        true
    }

    // (옵션) 딥링크
    setupDeepLinks(navGraphIds, fragmentManager, startIdToTag, containerId, intent) { controller ->
        selectedController.value = controller
        this.selectedItemId = controller.graph.startDestinationId
    }

    return selectedController
}

private fun obtainNavHostFragment(
    fm: FragmentManager,
    tag: String,
    navGraphId: Int,
    containerId: Int
): NavHostFragment {
    val existing = fm.findFragmentByTag(tag) as? NavHostFragment
    if (existing != null) return existing
    val host = NavHostFragment.create(navGraphId)
    fm.beginTransaction()
        .add(containerId, host, tag)
        .setReorderingAllowed(true)
        .commitNow()
    return host
}

private fun BottomNavigationView.setupDeepLinks(
    navGraphIds: List<Int>,
    fm: FragmentManager,
    startIdToTag: Map<Int, String>,
    containerId: Int,
    intent: Intent,
    onReady: (NavController) -> Unit
) {
    navGraphIds.forEachIndexed { index, navGraphId ->
        val tag = "bottomNav#$index"
        val host = obtainNavHostFragment(fm, tag, navGraphId, containerId)
        val controller = host.navController
        if (controller.handleDeepLink(intent)) {
            fm.beginTransaction()
                .setPrimaryNavigationFragment(host)
                .apply {
                    startIdToTag.values.filter { it != tag }
                        .forEach { detach(fm.findFragmentByTag(it)!!) }
                    attach(host)
                }
                .setReorderingAllowed(true)
                .commitNow()
            onReady(controller)
        }
    }
}
