package com.test.myevent.base

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.test.myevent.IConfig
import com.test.myevent.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPermission()
    }

    fun initPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                        return
                    }
                    if (report.areAllPermissionsGranted()) {

                    } else {
                        finish()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, which ->
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(
            getString(R.string.ambil_dari_kamera),
            getString(R.string.ambil_dari_galeri),
            getString(R.string.batal)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.pilih_gambar_event))
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.ambil_dari_kamera) -> {
                    Dexter.withActivity(this)
                        .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        ).withListener(object : MultiplePermissionsListener {
                            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                if (report.areAllPermissionsGranted()) {
                                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(takePicture, IConfig.REQUEST_CAMERA)
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied) {
                                    // show alert dialog navigating to Settings
                                    showSettingsDialog()
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>,
                                token: PermissionToken
                            ) {
                                token.continuePermissionRequest();
                            }

                        }).check()
                }
                options[item] == getString(R.string.ambil_dari_galeri) -> {
                    Dexter.withActivity(this)
                        .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .withListener(object : MultiplePermissionsListener {
                            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                if (report.areAllPermissionsGranted()) {
                                    val pickPhoto = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    startActivityForResult(pickPhoto, IConfig.REQUEST_GALLERY)
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied) {
                                    // show alert dialog navigating to Settings
                                    showSettingsDialog()
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(
                                permissions: MutableList<PermissionRequest>,
                                token: PermissionToken
                            ) {
                                token.continuePermissionRequest();
                            }

                        })
                        .withErrorListener {
                            Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
                        }
                        .check()
                }
                options[item] == getString(R.string.batal) -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
}