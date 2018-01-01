package com.codemybrainsout.imageviewer.custom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.databinding.LayoutExifBinding;
import com.codemybrainsout.imageviewer.model.Exif;
import com.codemybrainsout.imageviewer.viewmodel.ExifViewModel;

import butterknife.BindView;

/**
 * Created by ahulr on 11-06-2017.
 */

public class ExifDialog extends AppCompatDialog {

    private Context context;
    private Exif exif;

    public ExifDialog(Context context, Exif exif) {
        super(context, R.style.DialogTheme);
        this.exif = exif;
        this.context = context;
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Data binding to set exif data directly to the UI
        LayoutExifBinding exifBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_exif, null, false);
        setContentView(exifBinding.getRoot());
        ExifViewModel exifViewModel = new ExifViewModel(exif);
        exifBinding.setExif(exifViewModel);

        exifBinding.layoutExifLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
