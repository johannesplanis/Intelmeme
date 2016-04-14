package com.planis.johannes.intelmeme.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planis.johannes.intelmeme.App;
import com.planis.johannes.intelmeme.Constants;
import com.planis.johannes.intelmeme.R;
import com.planis.johannes.intelmeme.utils.L;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemeCreatorActivity extends AppCompatActivity {


    @Bind(R.id.ivCreatorImageContainer)
    ImageView ivCreatorImageContainer;

    @Bind(R.id.tvCreatorTextFieldTop)
    TextView tvCreatorTextFieldTop;
    @Bind(R.id.tvCreatorTextFieldBottom)
    TextView tvCreatorTextFieldBottom;
    @Bind(R.id.rlCreatorSnapshotContainer)
    RelativeLayout rlCreatorSnapshotContainer;
    @Bind(R.id.tvCreatorCopyright)
    TextView tvCreatorCopyright;

    private Bitmap bitmap;

    private Uri bitmapUri;


    private Bitmap backgroundImage;
    private Integer[] colors = {R.color.white, R.color.red, R.color.green, R.color.yellow,R.color.blue};
    private Float[] textSizes = {24f,26f,28f,30f,32f};
    private Integer[] memeTypes = {TWO_LINES, TOP_LINE, BOTTOM_LINE};

    private static final int TWO_LINES = 1114;
    private static final int TOP_LINE = 1115;
    private static final int BOTTOM_LINE = 1116;
    private static final int REQUEST_WRITE_STORAGE = 112;

    private int memeType = 0;

    private int currentColor = 0;

    private int currentTextSize = 0;

    private int imageSourceMode = Constants.ERROR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_creator);
        ButterKnife.bind(this);


        setupView();
    }

    private void setupView() {



        Intent intent = getIntent();

        String action = intent.getAction();
        String type = intent.getType();

        imageSourceMode = intent.getIntExtra(Constants.SOURCE_MODE, Constants.ERROR);


        if (Intent.ACTION_SEND.equals(action) && null !=type) {
            if (type.startsWith("image/")){
                imageSourceMode = Constants.SERVICE;
            }
        }

        switch (imageSourceMode) {

            case Constants.LOCALLY:
                int id = intent.getIntExtra(Constants.LOCAL_IMG_ID, R.drawable.its_something);
                ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, id));
                break;

            case Constants.GALERRY:
                backgroundImage = App.getInstance().getDataManager().getBitmap();
                if (null != backgroundImage) {
                    ivCreatorImageContainer.setImageBitmap(backgroundImage);
                } else {
                    ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.its_something));
                }
                break;

            case Constants.SERVICE:
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                L.d(imageUri.getPath());
                loadImageFromUri(imageUri);
                break;

            default:
                ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.its_something));
                break;
        }
    }

    private void loadImageFromUri(Uri imageUri) {

        if (null != imageUri) {
            //ivCreatorImageContainer.setImageURI(null);
            ivCreatorImageContainer.setImageURI(imageUri);
        }
    }


    private void changeColor() {
        currentColor++;
        if (currentColor == colors.length) {
            currentColor = 0;
        }

        int colorId = colors[currentColor];
        int color = ContextCompat.getColor(this, colorId);

        tvCreatorTextFieldTop.setTextColor(color);
        tvCreatorTextFieldBottom.setTextColor(color);
    }

    private void changeStyle() {
        memeType++;
        if (memeType == memeTypes.length) {
            memeType = 0;
        }
        setupMemeType();
    }


    @OnClick({R.id.tvCreatorChangeStyle, R.id.tvCreatorTextFieldTop, R.id.tvCreatorTextFieldBottom, R.id.btnCreatorChangeColor, R.id.btnCreatorSave,R.id.btnCreatorChangeTextSize})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCreatorChangeStyle:
                changeStyle();
                break;
            case R.id.tvCreatorTextFieldTop:
                editMemeText(tvCreatorTextFieldTop);
                break;
            case R.id.tvCreatorTextFieldBottom:
                editMemeText(tvCreatorTextFieldBottom);
                break;
            case R.id.btnCreatorChangeColor:
                changeColor();
                break;
            case R.id.btnCreatorSave:
                saveMeme();
                break;
            case R.id.btnCreatorChangeTextSize:
                changeSize();
                break;
        }
    }

    private void changeSize() {
        currentTextSize++;
        if (currentTextSize==textSizes.length){
            currentTextSize = 0;
        }
        tvCreatorTextFieldTop.setTextSize(textSizes[currentTextSize]);
        tvCreatorTextFieldBottom.setTextSize(textSizes[currentTextSize]);

    }



    private void editMemeText(final TextView tv) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout dialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_creator_edit_meme_text, null, false);
        final EditText et = (EditText) dialogLayout.findViewById(R.id.etCreatorTextEditor);
        if (!getString(R.string.kliknij_eby_edytowac).equals(tv.getText())){
        et.setText(tv.getText());
        }else{
            et.setHint(tv.getText());
        }
        builder.setView(dialogLayout).setMessage(getString(R.string.insert_meme_text)).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv.setText(et.getText());
            }
        }).setNegativeButton(getString(R.string.cancel), null).create().show();
    }


    private void setupMemeType() {
        switch (memeTypes[memeType]) {
            case TWO_LINES:
                tvCreatorTextFieldTop.setVisibility(View.VISIBLE);
                tvCreatorTextFieldBottom.setVisibility(View.VISIBLE);
                break;
            case TOP_LINE:
                tvCreatorTextFieldTop.setVisibility(View.VISIBLE);
                tvCreatorTextFieldBottom.setVisibility(View.GONE);
                break;
            case BOTTOM_LINE:
                tvCreatorTextFieldTop.setVisibility(View.GONE);
                tvCreatorTextFieldBottom.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void saveMeme() {

        tvCreatorCopyright.setVisibility(View.VISIBLE);
        View v = rlCreatorSnapshotContainer;
        v.setDrawingCacheEnabled(true);

        final Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        this.bitmap = bitmap;


        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else{
            bitmapUri = storeImage(bitmap);
            setupAlertDialog();
        }

        tvCreatorCopyright.setVisibility(View.INVISIBLE);

        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
    }


    private void attemptShare(Uri uri) {
        shareBitmap(uri);


    }

    private void shareBitmap(Uri uri) {

        if (uri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");

            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            Toast.makeText(MemeCreatorActivity.this,"Wystąpił błąd!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    bitmapUri = storeImage(bitmap);
                    setupAlertDialog();
                } else
                {

                    Toast.makeText(this, "Wystąpił błąd, przyznaj uprawnienie zapisu na karcie żeby kontynuować", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void setupAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        RelativeLayout dialogLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_meme_preview, null, false);
        ImageView iv = (ImageView) dialogLayout.findViewById(R.id.imageView);
        RelativeLayout rl = (RelativeLayout) dialogLayout.findViewById(R.id.rlPreviewShareContainer);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptShare(bitmapUri);
            }
        });
        iv.setImageBitmap(bitmap);
        builder.setView(dialogLayout).create().show();
    }


    private Uri storeImage(Bitmap b) {

        FileOutputStream fileOutputStream = null;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "Intelmeme_" + Calendar.getInstance().getTimeInMillis()+ ".jpg");
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            bos.flush();
            bos.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.parse("file://" + file.getAbsolutePath());
    }



}
