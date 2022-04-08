package com.iraimjanov.load_camera_upload_database

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.iraimjanov.load_camera_upload_database.adapters.RecyclerViewImageAdapter
import com.iraimjanov.load_camera_upload_database.databinding.ActivityMainBinding
import com.iraimjanov.load_camera_upload_database.databinding.AddDialogBinding
import com.iraimjanov.load_camera_upload_database.models.AppDatabase
import com.iraimjanov.load_camera_upload_database.models.MyImage
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentImagePath: String
    private lateinit var photoUri: Uri
    private lateinit var appDateBase:AppDatabase
    private lateinit var recyclerViewImageAdapter: RecyclerViewImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        appDateBase = AppDatabase.getInstance(this)

        binding.imageAdd.setOnClickListener {
            photoUri = FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID,
                createImageFile()
            )
            getImageContent.launch(photoUri)
        }

        appDateBase.myDao().getAllTypes().subscribeOn(Schedulers.io()).subscribe {
            runOnUiThread {
                recyclerViewImageAdapter = RecyclerViewImageAdapter(this , it as ArrayList<MyImage>)
                binding.recyclerView.adapter = recyclerViewImageAdapter
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(format, ".jpg", externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                val addBinding = AddDialogBinding.inflate(layoutInflater)
                val alertDialog = AlertDialog.Builder(this).create()
                var cancel = false

                Glide.with(this).load(currentImagePath).centerCrop().into(addBinding.imageView)

                alertDialog.setCanceledOnTouchOutside(false)

                addBinding.btnCancel.setOnClickListener {
                    alertDialog.cancel()
                }

                addBinding.btnSave.setOnClickListener {
                    val name = addBinding.edtName.text.toString().trim()
                    if (name.isNotEmpty()){
                        appDateBase.myDao().addImage(MyImage(currentImagePath , name))
                        cancel = true
                        alertDialog.cancel()
                    }else{
                        Toast.makeText(this, "Row empty", Toast.LENGTH_SHORT).show()
                    }

                }

                alertDialog.setOnCancelListener {
                    if (!cancel) {
                        deleteImage(currentImagePath)
                    }
                }

                alertDialog.setView(addBinding.root)
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()
            }
        }

    private fun deleteImage(patch: String) {
        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        for (i in externalFilesDir!!.listFiles().indices){
            if (externalFilesDir.listFiles()[i].absolutePath == patch){
                externalFilesDir.listFiles()[i].delete()
                break
            }
        }
    }
}