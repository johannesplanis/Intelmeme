package com.planis.johannes.intelmeme.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.planis.johannes.intelmeme.App
import com.planis.johannes.intelmeme.Constants
import com.planis.johannes.intelmeme.R
import com.planis.johannes.intelmeme.utils.L
import kotlinx.android.synthetic.main.activity_meme_creator.*
import java.io.*
import java.util.*

class MemeCreatorActivity : AppCompatActivity() {



    private var bitmap: Bitmap? = null

    private val bitmapUri: String? = null


    private var backgroundImage: Bitmap? = null
    private val colors = arrayOf(R.color.white, R.color.red, R.color.green, R.color.yellow, R.color.blue)
    private val textSizes = arrayOf(24f, 26f, 28f, 30f, 32f)
    private val memeTypes = arrayOf(TWO_LINES, TOP_LINE, BOTTOM_LINE)

    private var memeType = 0

    private var currentColor = 0

    private var currentTextSize = 0

    private var imageSourceMode = Constants.ERROR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme_creator)


        setupView()
    }



    private fun setupView() {



        val action = intent.action
        val type : String? = intent.type


        imageSourceMode = intent.getIntExtra(Constants.SOURCE_MODE, Constants.ERROR)


        if (Intent.ACTION_SEND == action) {
            if (type!!.startsWith("image/")) {
                imageSourceMode = Constants.SERVICE
            }
        }

        when (imageSourceMode) {

            Constants.LOCALLY -> {
                val id = intent.getIntExtra(Constants.LOCAL_IMG_ID, R.drawable.its_something)
                ivCreatorImageContainer!!.setImageDrawable(ContextCompat.getDrawable(this, id))
            }

            Constants.GALERRY -> {
                backgroundImage = App.getInstance().dataManager.bitmap
                if (null != backgroundImage) {
                    ivCreatorImageContainer.setImageBitmap(backgroundImage)
                } else {
                    ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.its_something))
                }
            }

            Constants.SERVICE -> {
                val imageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                L.d(imageUri.path)
                loadImageFromUri(imageUri)
            }

            else -> ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.its_something))
        }
    }

    private fun loadImageFromUri(imageUri: Uri?) {

        if (null != imageUri) {
            ivCreatorImageContainer.setImageURI(imageUri)
        }
    }


    private fun changeColor() {
        currentColor++
        if (currentColor == colors.size) {
            currentColor = 0
        }

        val colorId = colors[currentColor]
        val color = ContextCompat.getColor(this, colorId)


        tvCreatorTextFieldTop.setTextColor(color)
        tvCreatorTextFieldBottom.setTextColor(color)
    }

    private fun changeStyle() {
        memeType++
        if (memeType == memeTypes.size) {
            memeType = 0
        }
        setupMemeType()
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.tvCreatorChangeStyle -> changeStyle()
            R.id.tvCreatorTextFieldTop -> editMemeText(tvCreatorTextFieldTop)
            R.id.tvCreatorTextFieldBottom -> editMemeText(tvCreatorTextFieldBottom)
            R.id.btnCreatorChangeColor -> changeColor()
            R.id.btnCreatorSave -> generateMeme()
            R.id.btnCreatorChangeTextSize -> changeSize()
        }
    }

    private fun changeSize() {
        currentTextSize++
        if (currentTextSize == textSizes.size) {
            currentTextSize = 0
        }
        tvCreatorTextFieldTop!!.textSize = textSizes[currentTextSize]
        tvCreatorTextFieldBottom!!.textSize = textSizes[currentTextSize]

    }


    private fun editMemeText(tv: TextView) {

        val builder = AlertDialog.Builder(this)

        val dialogLayout = layoutInflater.inflate(R.layout.layout_creator_edit_meme_text, null, false) as LinearLayout
        val et = dialogLayout.findViewById(R.id.etCreatorTextEditor) as EditText
        if (getString(R.string.kliknij_eby_edytowac) != tv.text) {
            et.setText(tv.text)
        } else {
            et.hint = tv.text
        }
        builder.setView(dialogLayout).setMessage(getString(R.string.insert_meme_text)).setPositiveButton(getString(R.string.save)) { dialog, which -> tv.text = et.text }.setNegativeButton(getString(R.string.cancel), null).create().show()
    }


    private fun setupMemeType() {
        when (memeTypes[memeType]) {
            TWO_LINES -> {
                tvCreatorTextFieldTop!!.visibility = View.VISIBLE
                tvCreatorTextFieldBottom!!.visibility = View.VISIBLE
            }
            TOP_LINE -> {
                tvCreatorTextFieldTop!!.visibility = View.VISIBLE
                tvCreatorTextFieldBottom!!.visibility = View.GONE
            }
            BOTTOM_LINE -> {
                tvCreatorTextFieldTop!!.visibility = View.GONE
                tvCreatorTextFieldBottom!!.visibility = View.VISIBLE
            }
        }
    }

    private fun generateMeme() {
        tvCreatorCopyright!!.visibility = View.VISIBLE
        val v = rlCreatorSnapshotContainer
        v!!.isDrawingCacheEnabled = true

        val bitmap = Bitmap.createBitmap(v.drawingCache)
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        this.bitmap = bitmap

        saveToProvider(bitmap)
        setupAlertDialog()

        tvCreatorCopyright!!.visibility = View.INVISIBLE

        v.isDrawingCacheEnabled = false
        v.destroyDrawingCache()
    }

    private fun saveMeme() {

        val hasPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE)
        } else {

            storeImage(bitmap)
        }

    }


    private fun shareBitmapFromProvider() {

        val imagePath = File(cacheDir, "images")
        val fileToShare = File(imagePath, "meme.png")
        val contentUri = FileProvider.getUriForFile(this, "com.planis.johannes.intelmeme.fileprovider", fileToShare)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.type = "image/*"
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_meme)))
        } else {
            Toast.makeText(this@MemeCreatorActivity, "Wystąpił błąd!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    storeImage(bitmap)

                } else {

                    Toast.makeText(this, "Wystąpił błąd, przyznaj uprawnienie zapisu na karcie żeby kontynuować", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupAlertDialog() {

        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.layout_meme_preview, null, false) as RelativeLayout
        val iv = dialogLayout.findViewById(R.id.imageView) as ImageView
        val rlShare = dialogLayout.findViewById(R.id.rlPreviewShareContainer) as RelativeLayout
        val rlDownload = dialogLayout.findViewById(R.id.rlPreviewDownloadContainer) as RelativeLayout
        rlShare.setOnClickListener { shareBitmapFromProvider() }
        rlDownload.setOnClickListener { saveMeme() }
        iv.setImageBitmap(bitmap)
        builder.setView(dialogLayout).create().show()
    }


    /**
     * pod zapisywanie określonego obrazka na telefonie

     * @param b
     * *
     * @return
     */
    private fun storeImage(b: Bitmap?): String {

        var fileOutputStream: FileOutputStream? = null
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(path, "Intelmeme_" + Calendar.getInstance().timeInMillis + ".jpg")
        try {
            fileOutputStream = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val bos = BufferedOutputStream(fileOutputStream!!)
        b?.compress(Bitmap.CompressFormat.JPEG, 100, bos)

        try {
            bos.flush()
            bos.close()
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (file.exists()) {

            Snackbar.make(rlCreatorSnapshotContainer!!, R.string.meme_saved_info, Snackbar.LENGTH_LONG).show()
        }
        return file.absolutePath
    }


    private fun deleteImage(path: String) {

        if (null != bitmapUri) {

            val file = File(bitmapUri)

            if (file.exists()) {

                file.delete()
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(bitmapUri))))
            }
        }
    }

    private fun saveToProvider(b: Bitmap) {
        try {

            val cachePath = File(cacheDir, "images")
            cachePath.mkdirs()
            val stream = FileOutputStream(cachePath.path +"/meme.png")
            b.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {

        private val TWO_LINES = 1114
        private val TOP_LINE = 1115
        private val BOTTOM_LINE = 1116
        private val REQUEST_WRITE_STORAGE = 112
    }


}
