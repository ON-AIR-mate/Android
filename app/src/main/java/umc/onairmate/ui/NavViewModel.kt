package umc.onairmate.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class NavViewModel  @Inject constructor(): ViewModel(){
    private val TAG = this.javaClass.simpleName

    private val _events = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val events: SharedFlow<Int> get() = _events

    fun emitReset(menuId: Int) { _events.tryEmit(menuId) }

}