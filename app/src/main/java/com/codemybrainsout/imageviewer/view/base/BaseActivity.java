package com.codemybrainsout.imageviewer.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemybrainsout.imageviewer.R;
import com.codemybrainsout.imageviewer.app.ResplashApplication;
import com.codemybrainsout.imageviewer.dagger.component.AppComponent;
import com.codemybrainsout.imageviewer.utility.NetworkUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ahulr on 06-06-2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void showErrorSnackbar(View view, @StringRes int strId) {
        Snackbar snackbar = Snackbar.make(view, strId, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.cardBackground));
        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    protected void showErrorSnackbar(View view, String str) {

        String s = resolveError(str);

        final Snackbar snackbar = Snackbar.make(view, s, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.cardBackground));
        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private String resolveError(String str) {

        if (str.contains("HTTP 403 ")) {
            return "API Limit Exhausted.";
        } else if (str.contains("Unable to resolve host")) {
            return "No Internet Connection.";
        } else {
            return str;
        }

    }

    public void addProgressLayout(Context context, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.view_progress, null);
        parent.addView(v, parent.getChildCount(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void retryClicked() {

    }

    public void removeProgressLayout(ViewGroup parent) {
        if (parent.findViewById(R.id.view_progress) != null) {
            parent.removeView(parent.findViewById(R.id.view_progress));
        }
    }

    public AppComponent getAppComponent() {
        return ((ResplashApplication) getApplication()).getComponent();
    }


    public abstract void setToolbar();
}
