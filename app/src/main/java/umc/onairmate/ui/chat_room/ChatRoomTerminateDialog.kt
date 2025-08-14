package umc.onairmate.ui.chat_room

import androidx.fragment.app.DialogFragment
import umc.onairmate.databinding.DialogTerminateRoomBinding
import umc.onairmate.ui.pop_up.PopupClick

class ChatRoomTerminateDialog(
    private val clickFunc: PopupClick
) : DialogFragment() {
    lateinit var binding: DialogTerminateRoomBinding


}