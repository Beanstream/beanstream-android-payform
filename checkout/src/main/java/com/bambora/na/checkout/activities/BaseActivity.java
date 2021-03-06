/*
 * Copyright (c) 2017 Bambora
 */

package com.bambora.na.checkout.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bambora.na.checkout.models.Options;
import com.bambora.na.checkout.models.Purchase;
import com.bambora.na.checkout.R;

import java.util.Currency;

public abstract class BaseActivity extends AppCompatActivity {

    public final static String EXTRA_OPTIONS = "com.bambora.na.checkout.models.options";
    public final static String EXTRA_PURCHASE = "com.bambora.na.checkout.models.purchase";

    Options options;
    Purchase purchase;

    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static int getThemeAccentColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (view != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    public static void showKeyboardWhenEmpty(Activity activity, EditText editText) {
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            editText.requestFocus();
            BaseActivity.showKeyboard(activity, editText);
        } else {
            BaseActivity.hideKeyboard(activity);
        }
    }

    public static void showKeyboard(Activity activity, View view) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (view != null) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        options = intent.getParcelableExtra(EXTRA_OPTIONS);
        if (options == null) {
            options = new Options();
        }
        purchase = intent.getParcelableExtra(EXTRA_PURCHASE);
        if (purchase == null) {
            purchase = new Purchase(0.0, Currency.getInstance(Purchase.CURRENCY_CODE_CANADA));
        }

        if (options.getThemeResourceId() == 0) {
            super.setTheme(R.style.Theme_Checkout);
        } else {
            super.setTheme(options.getThemeResourceId());
        }
    }

    void disableHeaderBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    void updatePurchaseHeader(Options options, Purchase purchase) {
        ImageView imageView = ((ImageView) findViewById(R.id.header_company_logo));
        if (options.getCompanyLogoResourceId() == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(options.getCompanyLogoResourceId());
        }

        TextView amount = ((TextView) findViewById(R.id.header_amount));
        amount.setText(purchase.getFormattedAmount());

        TextView company = ((TextView) findViewById(R.id.header_company_name));
        company.setText(options.getCompanyName());

        TextView description = ((TextView) findViewById(R.id.header_description));
        description.setText(purchase.getDescription());

        Toolbar cardHeader = (Toolbar) findViewById(R.id.toolbar_card_header);
        if (cardHeader != null) {
            cardHeader.setBackgroundColor(Color.WHITE);
            amount.setTextColor(android.graphics.Color.BLACK);
            company.setTextColor(android.graphics.Color.BLACK);
            description.setTextColor(android.graphics.Color.BLACK);
        }

        int primaryColor = getThemePrimaryColor(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_with_controls);
        toolbar.setBackgroundColor(primaryColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getDarkerShade(primaryColor));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public int getDarkerShade(int color) {
        float SHADE_FACTOR_DARKER = 0.6f;
        return getShade(color, SHADE_FACTOR_DARKER);
    }

    public int getLighterShade(int color) {
        int alpha = 200;
        return getAlphaColor(color, alpha);
    }

    private int getAlphaColor(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    private int getShade(int color, float shadeFactor) {
        return Color.rgb((int) (shadeFactor * Color.red(color)),
                (int) (shadeFactor * Color.green(color)),
                (int) (shadeFactor * Color.blue(color)));
    }
}
