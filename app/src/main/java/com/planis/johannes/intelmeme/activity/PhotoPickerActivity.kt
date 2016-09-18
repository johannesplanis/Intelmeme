package com.planis.johannes.intelmeme.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.planis.johannes.intelmeme.App
import com.planis.johannes.intelmeme.Constants
import com.planis.johannes.intelmeme.R
import com.planis.johannes.intelmeme.events.ImagePickedLocallyEvent
import com.planis.johannes.intelmeme.fragment.PickerLocalListFragment
import com.planis.johannes.intelmeme.fragment.PickerOptionsFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.FileNotFoundException



class PhotoPickerActivity : AppCompatActivity() {

    private var fragmentType = Constants.INIT

    private var memeBackground: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)


        supportFragmentManager.beginTransaction().add(R.id.flPickerContainer, fragment).commit()

    }


    private val fragment: Fragment
        get() {
            when (fragmentType) {
                Constants.INIT -> return PickerOptionsFragment()
                Constants.LOCALLY -> return PickerLocalListFragment()
                else -> return PickerOptionsFragment()
            }
        }

    override fun onBackPressed() {

        if (fragmentType != Constants.INIT) {
            fragmentType = Constants.INIT
            supportFragmentManager.beginTransaction().replace(R.id.flPickerContainer, fragment).commit()
        } else {
            finish()
        }
    }


    fun chooseLocally(view: View) {
        fragmentType = Constants.LOCALLY
        supportFragmentManager.beginTransaction().replace(R.id.flPickerContainer, fragment).commit()

    }

    fun chooseFromGalerry(view: View) {

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image_prompt))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, R.string.file_open_failure, Toast.LENGTH_SHORT).show()
                return
            }

            parseIntent(data)

        }
    }

    private fun parseIntent(data: Intent) {
        try {
            val inputStream = this.contentResolver.openInputStream(data.data)
            memeBackground = BitmapFactory.decodeStream(inputStream)
            App.getInstance().dataManager.bitmap = memeBackground

            val intent = Intent(this, MemeCreatorActivity::class.java)

            intent.putExtra(Constants.SOURCE_MODE, Constants.GALERRY)
            startActivity(intent)
        } catch (e: FileNotFoundException) {

        }

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    fun onEvent(event: ImagePickedLocallyEvent) {

        val handler = Handler()
        handler.postDelayed({ onBackPressed() }, 100)

        val intent = Intent(this, MemeCreatorActivity::class.java)
        intent.putExtra(Constants.SOURCE_MODE, Constants.LOCALLY)
        intent.putExtra(Constants.LOCAL_IMG_ID, event.id)
        startActivity(intent)
    }

    companion object {


        private val PICK_IMAGE = 1111
    }
}
