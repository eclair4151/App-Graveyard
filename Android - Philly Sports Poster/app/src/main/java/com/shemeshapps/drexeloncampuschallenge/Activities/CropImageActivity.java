package com.shemeshapps.drexeloncampuschallenge.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.isseiaoki.simplecropview.CropImageView;
import com.shemeshapps.drexeloncampuschallenge.Helpers.CustomCropView;
import com.shemeshapps.drexeloncampuschallenge.Helpers.PictureTypeSelect;
import com.shemeshapps.drexeloncampuschallenge.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        final CustomCropView cropImageView = (CustomCropView)findViewById(R.id.cropImageView);
        cropImageView.setMinFrameSizeInDp(200);
        cropImageView.setHandleSizeInDp(6);
        cropImageView.setTouchPaddingInDp(16);
        cropImageView.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);
        File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pre_crop.jpg");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        cropImageView.setImageBitmap(PictureTypeSelect.rotateBitmap(BitmapFactory.decodeFile(f.getAbsolutePath(), options),orientation));
        final Button crop = (Button)findViewById(R.id.crop_button);
        Button cancel = (Button)findViewById(R.id.cancel_button);
        final ProgressBar p = (ProgressBar)findViewById(R.id.cropping_progress);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                p.setVisibility(View.VISIBLE);
                cropImageView.setEnabled(false);
                crop.setEnabled(false);
                final Bitmap b = cropImageView.getCroppedBitmap();

                new AsyncTask<Void,Void,Void>()
                {
                    @Override
                    protected Void doInBackground(Void... params) {

                        FileOutputStream out = null;
                        File f_out = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "post_crop.jpg");
                        if (f_out.exists()) f_out.delete();

                        try {
                            out = new FileOutputStream(f_out.getAbsolutePath());
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

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void params) {
                        super.onPostExecute(params);
                        setResult(RESULT_OK);
                        finish();
                    }
                }.execute();

            }
        });

    }

}
