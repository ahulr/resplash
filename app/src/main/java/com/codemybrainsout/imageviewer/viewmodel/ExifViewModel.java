package com.codemybrainsout.imageviewer.viewmodel;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.codemybrainsout.imageviewer.model.Exif;

/**
 *
 * Viewmodel required for binding exif data to layout_exif.xml
 *
 * Created by ahulr on 11-06-2017.
 */

public class ExifViewModel extends BaseObservable {

    private Exif exif;

    public ExifViewModel(Exif exif) {
        this.exif = exif;
    }

    public String getMake() {
        return TextUtils.isEmpty(exif.getMake()) ? "NA" : exif.getMake();
    }

    public String getModel() {
        return TextUtils.isEmpty(exif.getModel()) ? "NA" : exif.getModel();
    }

    public String getFocal() {
        return TextUtils.isEmpty(exif.getFocalLength()) ? "NA" : exif.getFocalLength() + "mm";
    }

    public String getAperture() {
        return TextUtils.isEmpty(exif.getAperture()) ? "NA" : "f/" + exif.getAperture();
    }

    public String getExposure() {
        return TextUtils.isEmpty(exif.getExposureTime()) ? "NA" : exif.getExposureTime() + "s";
    }

    public String getIso() {
        return exif.getIso() == null ? "NA" : "ISO " + String.valueOf(exif.getIso());
    }

    public String getLikes() {
        return exif.getLikes() == null ? "NA" : String.valueOf(exif.getLikes());
    }

    public String getDownloads() {
        return exif.getDownloads() == null ? "NA" : String.valueOf(exif.getDownloads());
    }
}
