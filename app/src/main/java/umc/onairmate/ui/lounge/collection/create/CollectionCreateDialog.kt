package umc.onairmate.ui.lounge.collection.create

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import umc.onairmate.data.model.entity.CollectionVisibility
import umc.onairmate.data.model.request.CollectionCreateRequest
import umc.onairmate.databinding.DialogCreateCollectionBinding
import umc.onairmate.ui.home.room.HomeSortTypeSpinnerAdapter

// 컬렉션 생성 팝업
class CollectionCreateDialog(
    private val createCollectionCallback: CreateCollectionCallback
) : DialogFragment() {
    lateinit var binding: DialogCreateCollectionBinding

    var textLength: Int = 0
    val visibilityList = CollectionVisibility.entries.map { it.displayName }
    var selectedVisibility: String = visibilityList[0]

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCreateCollectionBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        val dialog = builder.create()

        initSpinner()
        setTextListener()
        setOnClickListener()

        // 뒤로가기 가능
        setCancelable(true)

        // 배경 투명 + 밝기 조절 (0.7)
        dialog.window?.let { window ->
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val layoutParams = window.attributes
            layoutParams.dimAmount = 0.7f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes = layoutParams
        }

        return dialog
    }

    private fun initSpinner() {
        val adapter = HomeSortTypeSpinnerAdapter(requireContext(), visibilityList)
        binding.spVisibility.adapter = adapter
        binding.spVisibility.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedVisibility = visibilityList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setTextListener(){
        binding.etCollectionTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textLength = s?.length ?: 0
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // 팝업 버튼 클릭리스너
    private fun setOnClickListener() {
        binding.btnChangeTitle.setOnClickListener {
            if (textLength in 3..20) {
                val requestData = CollectionCreateRequest(
                    title = binding.etCollectionTitle.text.toString(),
                    description = "",
                    visibility = CollectionVisibility.fromDisplayName(selectedVisibility)!!.apiName
                )

                createCollectionCallback.onCreateCollection(requestData)

                // 모든 함수 수행 후 팝업 닫기
                dismiss()
            } else {
                binding.tvTitleLengthErrorMsg.visibility = View.VISIBLE
            }

        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }
}

interface CreateCollectionCallback {
    fun onCreateCollection(requestData: CollectionCreateRequest) {}
}