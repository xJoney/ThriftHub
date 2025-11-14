package com.example.marketplaceapp


import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream

class AddItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)

        dbHelper = DatabaseHelper(this)
        dbHelper.readableDatabase

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val itemInput = findViewById<EditText>(R.id.itemInput)
        val addressInput = findViewById<EditText>(R.id.addressInput)
        val priceInput = findViewById<EditText>(R.id.priceInput)
        val descInput = findViewById<EditText>(R.id.descInput)

        val addButton = findViewById<Button>(R.id.addButton)

        val users = dbHelper.getAllUsers()

        //add button - waits for user to click add, inserts user inputs into db
        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val item = itemInput.text.toString()
            val address = addressInput.text.toString()
            val price = priceInput.text.toString()
            val description = descInput.text.toString()
            val imageUri = selectedImageUri?.toString() ?: ""

            if (dbHelper.insertUser(name, item, address, price, description, imageUri)) {
                Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show()
            }
            val users = dbHelper.getAllUsers()
        }

        findViewById<Button>(R.id.btnAddImage).setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Select Image")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> takePhotoLauncher.launch(null)
                    1 -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageLauncher.launch(intent)
                    }
                }
            }
            builder.show()
        }

//        val clear = findViewById<Button>(R.id.clear)
//        clear.setOnClickListener {
//            dbHelper.clearAllUsers()
//            val users = dbHelper.getAllUsers()
//            outputText.text = users.joinToString("\n")
//            Toast.makeText(this, "All records deleted", Toast.LENGTH_SHORT).show()
//        }

        //need to move this to your listings page
        val delete = findViewById<Button>(R.id.deletebtn)
        delete.setOnClickListener {
            val item = itemInput.text.toString()
            if (dbHelper.delUser(item)) {
                Toast.makeText(this, "Deleted item!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }

        // update button - find the row with name and updates new price and description from user input
        //need to move this to your listings page
        val update = findViewById<Button>(R.id.updatebtn)
        update.setOnClickListener {
            val name = nameInput.text.toString()
            val price = priceInput.text.toString()
            val description = descInput.text.toString()
            if (dbHelper.updateUser(name,price,description)) {
                Toast.makeText(this, "Updated item!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show()
            }
            val users = dbHelper.getAllUsers()
        }

        //back button to return to homepage
        val back = findViewById<Button>(R.id.backbtn)
        back.setOnClickListener {
            finish()
        }
    }

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val pickedUri = result.data?.data

            if (pickedUri != null) {
                val safeUri = copyUriToInternalStorage(pickedUri)

                if (safeUri != null) {
                    selectedImageUri = safeUri  //store safe local URI
                    findViewById<ImageView>(R.id.itemImagePreview)
                        .setImageURI(safeUri)
                }
            }
        }
    }


    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val imageView = findViewById<ImageView>(R.id.itemImagePreview)
            imageView.setImageBitmap(bitmap)

            val imageUri = Uri.parse(
                MediaStore.Images.Media.insertImage(contentResolver, bitmap, "item_photo", null)
            )
            selectedImageUri = imageUri
        }
    }

    private fun copyUriToInternalStorage(uri: Uri): Uri? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(filesDir, "image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}
