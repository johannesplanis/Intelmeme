package com.planis.johannes.intelmeme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.ButterKnife;

public class PhotoPickerActivity extends AppCompatActivity {


    private static final int PICK_IMAGE = 1111;

    private static final int INIT = 11112;
    private static final int LOCALLY = 33245;
    private static final int SERVICE = 54233;

    private int fragmentType = INIT;


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
            case INIT:
                return new PickerOptionsFragment();
            case LOCALLY:
                return new PickerLocalListFragment();
            case SERVICE:
                return new PickerFromServiceFragment();
            default:
                return new PickerOptionsFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if (fragmentType!=INIT){
            fragmentType = INIT;
            getSupportFragmentManager().beginTransaction().replace(R.id.flPickerContainer,getFragment()).commit();
        }else{
            finish();
        }
    }

    public void chooseService() {
        /*fragmentType = SERVICE;
        getSupportFragmentManager().beginTransaction().replace(R.id.flPickerContainer,getFragment()).commit();*/
        startMemeCreator();
    }

    public void chooseLocally() {
        /*fragmentType = LOCALLY;
        getSupportFragmentManager().beginTransaction().replace(R.id.flPickerContainer,getFragment()).commit();*/
        startMemeCreator();
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
                Toast.makeText(this,"Nie udało się otworzyć pliku",Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this,"Sukces!",Toast.LENGTH_SHORT).show();

            parseIntent(data);

        }
    }

    private void parseIntent(Intent data){
        try {
            InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
            memeBackground = BitmapFactory.decodeStream(inputStream);
            App.getInstance().getDataManager().setBitmap(memeBackground);
            startMemeCreator();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startMemeCreator(){
        startActivity(new Intent(this,MemeCreatorActivity.class));
    }

}
