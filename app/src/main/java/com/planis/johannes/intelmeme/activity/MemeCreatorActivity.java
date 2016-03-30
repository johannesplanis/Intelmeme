package com.planis.johannes.intelmeme.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planis.johannes.intelmeme.App;
import com.planis.johannes.intelmeme.Constants;
import com.planis.johannes.intelmeme.R;

import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemeCreatorActivity extends AppCompatActivity {


    @Bind(R.id.ivCreatorImageContainer)
    ImageView ivCreatorImageContainer;
    @Bind(R.id.rlCreatorContainer)
    RelativeLayout rlCreatorContainer;


    @Bind(R.id.vCreatorColorPreview)
    View vCreatorColorPreview;
    @Bind(R.id.btnCreatorChangeStyle)
    Button btnCreatorChangeStyle;
    @Bind(R.id.tvCreatorTextFieldTop)
    TextView tvCreatorTextFieldTop;
    @Bind(R.id.tvCreatorTextFieldBottom)
    TextView tvCreatorTextFieldBottom;

    Bitmap backgroundImage;
    Integer[] colors = {R.color.black, R.color.red, R.color.green,R.color.white};

    private static final int TWO_LINES = 1114;
    private static final int TOP_LINE = 1115;
    private static final int BOTTOM_LINE = 1116;

    Integer[] memeTypes = {TWO_LINES,TOP_LINE,BOTTOM_LINE};

    int memeType = 0;

    int currentColor = 0;

    int imageSourceMode = Constants.ERROR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_creator);
        ButterKnife.bind(this);


        setupView();
    }

    private void setupView() {

        Intent intent = getIntent();
        imageSourceMode = intent.getIntExtra(Constants.SOURCE_MODE,Constants.ERROR);
        backgroundImage = App.getInstance().getDataManager().getBitmap();

        switch(imageSourceMode){

            case Constants.LOCALLY:
                int id = intent.getIntExtra(Constants.LOCAL_IMG_ID,R.drawable.its_something);
                ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this,id));
                break;
            case Constants.GALERRY:
                if (null != backgroundImage) {
                    ivCreatorImageContainer.setImageBitmap(backgroundImage);
                } else {
                    ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.its_something));
                }
                break;
            case Constants.SERVICE:
                String url = intent.getStringExtra(Constants.SERVICE_URL);
                if (null!=url&&!"".equals(url)){
                    // TODO: 3/31/2016 load from Picasso
                    break;
                }
            default:
                ivCreatorImageContainer.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.its_something));
                break;

        }

    }


    public void changeColor() {
        currentColor++;
        if (currentColor == colors.length) {
            currentColor = 0;
        }

        int colorId = colors[currentColor];
        int color = ContextCompat.getColor(this, colorId);

        vCreatorColorPreview.setBackgroundColor(color);

        tvCreatorTextFieldTop.setTextColor(color);
        tvCreatorTextFieldBottom.setTextColor(color);
    }

    private void changeStyle() {
        memeType++;
        if (memeType == memeTypes.length){
            memeType = 0;
        }
        setupMemeType();
    }



    @OnClick({R.id.btnCreatorChangeStyle, R.id.tvCreatorTextFieldTop, R.id.tvCreatorTextFieldBottom,R.id.btnCreatorChangeColor,R.id.btnCreatorSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreatorChangeStyle:
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
        }
    }




    private void editMemeText(final TextView tv) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout dialogLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_creator_edit_meme_text,null,false);
        final EditText et = (EditText) dialogLayout.findViewById(R.id.etCreatorTextEditor);
        et.setText(tv.getText());
        builder.setView(dialogLayout).setMessage(getString(R.string.insert_meme_text)).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tv.setText(et.getText());
            }
        }).setNegativeButton(getString(R.string.cancel),null).create().show();
    }


    private void setupMemeType(){
        switch (memeTypes[memeType]){
            case TWO_LINES:
                tvCreatorTextFieldTop.setVisibility(View.VISIBLE);
                tvCreatorTextFieldBottom.setVisibility(View.VISIBLE);
                btnCreatorChangeStyle.setText(R.string.two_lines);
                break;
            case TOP_LINE:
                tvCreatorTextFieldTop.setVisibility(View.VISIBLE);
                tvCreatorTextFieldBottom.setVisibility(View.GONE);
                btnCreatorChangeStyle.setText(R.string.top);
                break;
            case BOTTOM_LINE:
                tvCreatorTextFieldTop.setVisibility(View.GONE);
                tvCreatorTextFieldBottom.setVisibility(View.VISIBLE);
                btnCreatorChangeStyle.setText(R.string.bottom);
                break;
        }
    }

    public void saveMeme() {

        Bitmap b = loadBitmapFromImageView();


        FileOutputStream out = null;
        try {
            out = new FileOutputStream("memesix");
            b.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Bitmap loadBitmapFromImageView(){
        Bitmap b = Bitmap.createBitmap(ivCreatorImageContainer.getWidth(),ivCreatorImageContainer.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        int ivPosX = 0;
        int ivPosY = 0;
        // TODO: 3/29/2016 calculate coordinates of iv in rl
        rlCreatorContainer.layout(ivPosX,ivPosY,ivCreatorImageContainer.getWidth(),ivCreatorImageContainer.getHeight());
        rlCreatorContainer.draw(c);

        return b;
    }
}
