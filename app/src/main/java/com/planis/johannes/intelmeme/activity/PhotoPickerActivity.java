package com.planis.johannes.intelmeme.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.planis.johannes.intelmeme.App;
import com.planis.johannes.intelmeme.Constants;
import com.planis.johannes.intelmeme.R;
import com.planis.johannes.intelmeme.events.ImagePickedLocallyEvent;
import com.planis.johannes.intelmeme.fragment.PickerLocalListFragment;
import com.planis.johannes.intelmeme.fragment.PickerOptionsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.ButterKnife;

public class PhotoPickerActivity extends AppCompatActivity {


    private static final int PICK_IMAGE = 1111;

    private int fragmentType = Constants.INIT;

    Bitmap memeBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);

        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction().add(R.id.flPickerContainer,getFragment()).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    private android.support.v4.app.Fragment getFragment(){
        switch (fragmentType){
            case Constants.INIT:
                return new PickerOptionsFragment();
            case Constants.LOCALLY:
                return new PickerLocalListFragment();
            default:
                return new PickerOptionsFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if (fragmentType!= Constants.INIT){
            fragmentType = Constants.INIT;
            getSupportFragmentManager().beginTransaction().replace(R.id.flPickerContainer,getFragment()).commit();
        }else{
            finish();
        }
    }


    public void chooseLocally() {
        fragmentType = Constants.LOCALLY;
        getSupportFragmentManager().beginTransaction().replace(R.id.flPickerContainer,getFragment()).commit();

    }

    public void chooseFromGalerry(){
        // TODO: 3/29/2016 pojawiają się dwie opcje system/documents  Czy da się coś z tym zrobić?
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            if (data == null) {
                Toast.makeText(this, R.string.file_open_failure,Toast.LENGTH_SHORT).show();
                return;
            }

            parseIntent(data);

        }
    }

    private void parseIntent(Intent data){
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
            memeBackground = BitmapFactory.decodeStream(inputStream);
            App.getInstance().getDataManager().setBitmap(memeBackground);

            Intent intent = new Intent(this,MemeCreatorActivity.class);
            intent.putExtra(Constants.SOURCE_MODE,Constants.GALERRY);
            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(ImagePickedLocallyEvent event){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        },500);

        Intent intent = new Intent(this,MemeCreatorActivity.class);
        intent.putExtra(Constants.SOURCE_MODE,Constants.LOCALLY);
        intent.putExtra(Constants.LOCAL_IMG_ID,event.getId());
        startActivity(intent);
    }
}
