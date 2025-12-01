package com.example.marketplaceapp.ui.item

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.databinding.FragmentItemDetailBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File

class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        val args = ItemDetailFragmentArgs.fromBundle(requireArguments())

        binding.itemTitle.text = args.itemTitle
        binding.itemDescription.text = args.itemDescription

        val sellerCity = extractCity(args.itemAddress)
        binding.sellerInfoText.text = "Seller: ${args.sellerName}\nCity: $sellerCity"

        val media = args.itemImageUri ?: ""
        Log.d("MEDIA_DEBUG", "RAW URI = '$media'")

        when {
            media.startsWith("image:") -> showImage(media.removePrefix("image:"))
            media.startsWith("video:") -> showVideo(media.removePrefix("video:"))
            else -> Log.e("MEDIA_DEBUG", "Unknown media format: $media")
        }

        binding.buttonMessage.setOnClickListener {
            val action = ItemDetailFragmentDirections.actionItemDetailFragmentToChatDetailFragment(
                chatName = args.sellerName,
                chatLastMessage = "Start your conversation..."
            )
            findNavController().navigate(action)
        }

        binding.itembtnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun showImage(pathOrFileName: String) {
        //to extract just the filename if it's a full path
        val fileName = File(pathOrFileName).name
        val file = File(requireContext().filesDir, fileName)

        Log.d("IMAGE_DEBUG", "Looking for: ${file.absolutePath}, exists=${file.exists()}")

        binding.playerView.visibility = View.GONE
        binding.itemImage.visibility = View.VISIBLE

        if (file.exists()) {
            val bmp = BitmapFactory.decodeFile(file.absolutePath)
            binding.itemImage.setImageBitmap(bmp)
        } else {
            Log.e("IMAGE_DEBUG", "Image not found at ${file.absolutePath}")
        }
    }

    private fun showVideo(pathOrFileName: String) {
        // Extract just the filename if it's a full path
        val fileName = File(pathOrFileName).name
        val file = File(requireContext().filesDir, fileName)

        Log.d("VIDEO_DEBUG", "Looking for: ${file.absolutePath}, exists=${file.exists()}")

        binding.itemImage.visibility = View.GONE
        binding.playerView.visibility = View.VISIBLE

        if (!file.exists()) {
            Log.e("VIDEO_DEBUG", "Video file not found at ${file.absolutePath}")
            return
        }

        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        val mediaItem = MediaItem.fromUri(Uri.fromFile(file))
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    private fun extractCity(address: String): String {
        return address.split(",").firstOrNull()?.trim() ?: address
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
