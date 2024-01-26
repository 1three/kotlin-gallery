package fastcampus.part1.gallery

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fastcampus.part1.gallery.databinding.ItemImageBinding
import fastcampus.part1.gallery.databinding.ItemLoadMoreBinding

/**
 * ListAdapter
 *
 * 1. <T : Any!, VH : RecyclerView.ViewHolder!> : Item, ViewHolder
 * 2. 다양한 타입 Item 넣기
 * 3. DiffUtil
 *    Item(Data) 변경 확인
 *    only 변경된 UI update
 * */

class ImageAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<ImageItems, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<ImageItems>() {
            override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem === newItem // 같은 데이터를 참조(===)하는지
            }

            override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem == newItem // 같은 데이터(==)인지
            }

        }
    ) {
    override fun getItemCount(): Int {
        val originSize = currentList.size
        return if (originSize == 0) 0 else originSize + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount - 1 == position) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when (viewType) {
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding)
            }

            // ITEM_LOAD_MORE
            else -> {
                val binding = ItemLoadMoreBinding.inflate(inflater, parent, false)
                LoadMoreViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }

            is LoadMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
        }
    }

    interface ItemClickListener {
        fun onLoadMoreClick()
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_LOAD_MORE = 1
    }
}

/**
 * 2*n 형태 앨범
 *
 * 사진, 사진
 * 사진, 사진
 * ...
 * 사진, 사진 불러오기
 * */

/**
 * sealed class
 *
 * 1. 제한된 클래스 계층 정의 시 사용
 * 2. 컴파일 시, when 표현식으로 모든 경우 처리 (No need for 'else')
 * 3. 상속 불가
 * */
sealed class ImageItems {
    data class Image(val uri: Uri) : ImageItems()
    data object LoadMore : ImageItems()
}

// Type (ITEM_IMAGE or ITEM_LOAD_MORE)에 맞는 ViewHolder
class ImageViewHolder(private val binding: ItemImageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ImageItems.Image) {
        binding.previewImageView.setImageURI(item.uri)
    }
}

class LoadMoreViewHolder(private val binding: ItemLoadMoreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(itemClickListener: ImageAdapter.ItemClickListener) {
        itemView.setOnClickListener {
            itemClickListener.onLoadMoreClick()
        }
    }
}