package umc.onairmate.ui.lounge.pop_up

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import umc.onairmate.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CollectionCreateDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.lounge_dialog_create_collection, null)

        val titleEditText = view.findViewById<EditText>(R.id.editTitle)
        val spinner = view.findViewById<Spinner>(R.id.spinnerPrivacy)
        val createButton = view.findViewById<Button>(R.id.btnCreate)

        val options = listOf("비공개", "공개")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)

        createButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val isPrivate = spinner.selectedItem == "비공개"

            if (title.length < 3 || title.length > 20) {
                Toast.makeText(requireContext(), "제목은 3~20자여야 해요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(requireContext(), "컬렉션 생성: $title (${if (isPrivate) "비공개" else "공개"})", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }
}
