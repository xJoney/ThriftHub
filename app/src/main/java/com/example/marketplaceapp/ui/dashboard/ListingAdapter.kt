import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplaceapp.R
import com.example.marketplaceapp.databinding.ItemListingBinding
import com.example.marketplaceapp.ui.dashboard.ListingData
import java.io.File

class ListingAdapter(
    private val items: List<ListingData>,
    private val onItemClick: (ListingData) -> Unit
) : RecyclerView.Adapter<ListingAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemListingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.txtTitle.text = item.item
        holder.binding.txtUpdated.text = item.description
        holder.binding.txtPrice.text = "$${item.price}"

        val media = item.imageUri ?: ""

        when {
            media.startsWith("video:") -> {
                val path = media.removePrefix("video:")
                val clean = path.replace("file://", "").replace("file:/", "")

                val thumb = ThumbnailUtils.createVideoThumbnail(
                    clean,
                    MediaStore.Images.Thumbnails.MINI_KIND
                )

                if (thumb != null) {
                    holder.binding.itemImage.setImageBitmap(thumb)
                } else {
                    holder.binding.itemImage.setImageResource(R.drawable.ic_video_placeholder)
                }
            }

            media.startsWith("image:") || media.contains("image", ignoreCase = true) -> {
                val path = media.removePrefix("image:")
                val bitmap = BitmapFactory.decodeFile(path)
                if (bitmap != null) {
                    holder.binding.itemImage.setImageBitmap(bitmap)
                } else {
                    holder.binding.itemImage.setImageResource(R.drawable.ic_image_placeholder)
                }
            }

            media.isNotEmpty() -> {
                val bitmap = BitmapFactory.decodeFile(media)
                if (bitmap != null) {
                    holder.binding.itemImage.setImageBitmap(bitmap)
                } else {
                    holder.binding.itemImage.setImageResource(R.drawable.ic_image_placeholder)
                }
            }

            else -> {
                holder.binding.itemImage.setImageResource(R.drawable.ic_image_placeholder)
            }
        }

        holder.binding.root.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
