package umc.OnAirMate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umc.onairmate.databinding.ItemSectionBinding
import umc.onairmate.model.BookmarkSection

class OuterAdapter(
    private val sectionList: List<BookmarkSection>
) : RecyclerView.Adapter<OuterAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sectionList[position]
        holder.binding.sectionTitle.text = section.sectionTitle

        // 내부 RecyclerView 설정
        val innerAdapter = InnerAdapter(section.videos)
        holder.binding.innerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = innerAdapter
            setHasFixedSize(true)
        }
    }

    override fun getItemCount(): Int = sectionList.size
}
