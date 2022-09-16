package com.sadiwala.shivam.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.ShivamApplication;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by ankit on 29/03/16.
 */
public class UiUtil {
    private static final int TRANSPARENT = 0;
    private static final int OPAQUE = 200;
    private static String TAG = "UiUtil";

    public static EventBus getVymoEventBus() {
        // TODO: 16/09/16 we may make it skelton object.
        return EventBus.builder().sendNoSubscriberEvent(false).build();
    }


    @Deprecated
    public static boolean isListEmpty(List list) {
        if (list == null) {
            return true;
        } else if (list.size() <= 0) {
            return true;
        }
        return false;
    }

    public static void addBrandingColorToRefreshSpinner(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(UiUtil.getBrandedPrimaryColorWithDefault());
    }

    /**
     * Applys given color on given text if color is null then apply #999999
     */
    public static void applyColorOnTextView(Context context, TextView figure, String color) {
        if (figure != null) {
            if (color != null) {
                figure.setTextColor(Color.parseColor(color));
            } else {
                figure.setTextColor(context.getResources().getColor(R.color.vymo_text_color_4));
            }
        }
    }

    /**
     * Apply given color as tint on given drawable and returns the same. Supported on pre-lollipop devices also
     */
    public static Drawable paintImageDrawable(Drawable drawable, int color) {
        drawable.mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    /**
     * Apply branded color as tint on given drawable and returns the same. Supported on pre-lollipop devices also
     */
    public static Drawable paintImageInBrandedColor(Drawable drawable) {
        return paintImageInBrandedColor(ShivamApplication.getAppContext(), drawable);
    }

    public static Drawable paintImageInBrandedColor(Context context, Drawable drawable) {
        return paintImageDrawable(drawable, getBrandedPrimaryColorWithDefault());
    }

    /**
     * Converts given string into spannable string and apply branded color on that
     */
    public static SpannableString getBrandedColoredText(Context context, String text) {
        SpannableString spannableString = new SpannableString(text);
        int color = getBrandedPrimaryColorWithDefault();
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static int getBrandedPrimaryColorWithDefault() {
        int backgroundColor = UiUtil.getColor(R.color.red);
        return backgroundColor;
    }

    /**
     * returns color state if branding is available. selected color will be background color and
     * non selected colors is black.
     */
    public static ColorStateList getBrandedBackgroundColorState() {
        return getBrandedBackgroundColorState(ShivamApplication.getAppContext());
    }

    public static ColorStateList getBrandedBackgroundColorState(Context context) {
        int[][] states = new int[][]{new int[]{android.R.attr.state_checked}, new int[]{
                -android.R.attr.state_checked}};
        int[] colors = new int[]{getBrandedPrimaryColorWithDefault(), Color.BLACK};
        return new ColorStateList(states, colors);
    }



    public static int getPercentageChangeColor(Context context, float change) {
        if (change > 0) {
            //positive and show upArrow
            return context.getResources().getColor(R.color.metrics_figure_positive_text);
        } else if (change < 0) {
            //negative and show down arrow
            return context.getResources().getColor(R.color.vymo_red);
        } else {
            //neutral and no arrow
            return context.getResources().getColor(R.color.metrics_figure_neutral_text);

        }
    }

    public static void paintProgressBarWithColorByPercentage(ProgressBar progressBar, int percentageProgress) {
        progressBar.setProgress(percentageProgress);
        int percentageColor = UiUtil.getColorByRange(percentageProgress);

        LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        RotateDrawable progressDrawable = (RotateDrawable) (layerDrawable)
                .findDrawableByLayerId(android.R.id.progress);
        UiUtil.paintImageDrawable(progressDrawable, percentageColor);
        progressBar.setProgressDrawable(layerDrawable);
    }


    public static void paintHorizontalProgressBar(ProgressBar progressBar, int percentage, int color) {
        LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
        Drawable progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
        progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        progressBar.setProgress(percentage);
    }

    public static int getColorByRange(double value) {
        Context context = ShivamApplication.getAppContext();
        int returnedColor = ContextCompat.getColor(context, R.color.vymo_red);
        if (value >= 0.0 && value < 33.0) {
            returnedColor = ContextCompat.getColor(context, R.color.metrics_figure_25_text);
        } else if (value >= 33.0 && value < 66.0) {
            returnedColor = ContextCompat.getColor(context, R.color.yellow_f4b922);
        } else if (value > 66.0) {
            returnedColor = ContextCompat.getColor(context, R.color.metrics_figure_100_text);
        }
        return returnedColor;
    }

    public static boolean isJson(String value) {
        return (value.length() > 0) && value.startsWith("{");
    }


    public static void paintFabMenu(Context context, FloatingActionMenu mFabMenu) {
        mFabMenu.setMenuButtonColorNormal(UiUtil.getBrandedPrimaryColorWithDefault());
        mFabMenu.setMenuButtonColorPressed(UiUtil.getBrandedPrimaryColorWithDefault());
        UiUtil.paintImageDrawable(mFabMenu.getMenuIconView().getDrawable(), context.getResources().getColor(android.R.color.white));
    }

    /**
     * Will return a rotated drawable based on the angle specified.
     */
    private static Drawable getRotateDrawable(final Drawable d, final float angle) {
        final Drawable[] arD = {d};
        return new LayerDrawable(arD) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, d.getBounds().width() / 2, d.getBounds().height() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
    }


    private static void showFABOptions(ViewGroup group) {
        if (group == null || group.getBackground() == null) {
            return;
        }
        group.getBackground().setAlpha(OPAQUE);
    }

    public static void hideFABOptions(ViewGroup group, FloatingActionMenu menu) {
        if (menu != null) {
            menu.close(false);
        }
        if (group == null || group.getBackground() == null) {
            return;
        }

        group.getBackground().setAlpha(TRANSPARENT);
        group.setOnTouchListener(null);
    }

    /**
     * Ex: RotateDrawable rotateDrawable = (RotateDrawable) ((LayerDrawable) ContextCompat.getDrawable(getActivity(),
     * R.drawable.circular_progress_bar)).findDrawableByLayerId(android.R.id.progress);
     * pBar.setProgressDrawable(UiUtil.setBrandedColorOnProgressbar(rotateDrawable));
     */
    public static Drawable setBrandedColorOnProgressbar(RotateDrawable drawable) {
        paintImageDrawable(drawable, getBrandedPrimaryColorWithDefault());
        return drawable;
    }


    public static void paintAndUnderlineText(TextView textView, boolean useBrandedColor) {
        if (useBrandedColor) {
            textView.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
        }
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static void removeUnderline(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
    }

    public static void reduceAlpha(Drawable drawable, int alpha) {
        drawable.setAlpha(alpha);
    }

    /**
     * Returns a color between Red and Green based on value passed to it. Value must be between
     * 0(Red) to 1(green)
     */
    public static int getColorByPercentage(double value) {
        return Color.HSVToColor(new float[]{(float) value * 120f, 1f, 1f});
    }


    /**
     * This method returns a single relatively sized spanned string builder,
     * by taking two strings and span size as inputs
     *
     * @param span        float value of relative span size
     * @param stringPart1 Result string part one
     * @param stringPart2 Result string part two (This will be relatively sized)
     * @return SpannableStringBuilder
     */
    public static SpannableStringBuilder getRelativeSizeSpanString(float span, String stringPart1, String stringPart2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(stringPart1);

        SpannableString styledString = new SpannableString(stringPart2);
        styledString.setSpan(new RelativeSizeSpan(span), 0, stringPart2.length(), 0);

        spannableStringBuilder.append(" ");
        spannableStringBuilder.append(styledString);

        return spannableStringBuilder;
    }

    public static Drawable getBackgroundDrawableAfterApplyingBrandedColor(Drawable background) {
        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background.mutate();
            gradientDrawable.setColor(UiUtil.getBrandedPrimaryColorWithDefault());
        }
        return background;
    }

    public static Drawable setRadius(Drawable background, int radius) {
        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setCornerRadius(radius);
        }
        return background;
    }

    public static Drawable getBackgroundDrawableAfterApplyingColor(Drawable background, int color) {
        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background.mutate();
            gradientDrawable.setColor(color);
        }
        return background;
    }

    public static Drawable getBackgroundDrawableAfterApplyingColorBorder(Drawable background, int color) {
        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background.mutate();
            gradientDrawable.setStroke(1, color);
        }
        return background;
    }

    public static Drawable setCornerRadius(Drawable background, int topLeftRadius, int topRightRadius, int bottomRightRadius, int bottomLeftRadius) {
        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background.mutate();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            float[] radius = {topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius,
                    bottomRightRadius, bottomLeftRadius, bottomLeftRadius};
            gradientDrawable.setCornerRadii(radius);
        }

        return background;
    }

    public static Drawable getBackgroundDrawableAfterApplyingBrandedColorBorder(Drawable background) {
        return getBackgroundDrawableAfterApplyingColorBorder(background, getBrandedPrimaryColorWithDefault());
    }

    public static void addUnderlineWithBrandedColor(TextView textView, boolean brandedColor) {
        if (brandedColor) {
            textView.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
        }
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static int getViewHeight(View view) {
        if (view != null) {
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int height = view.getMeasuredHeight();
            return height;
        }
        return 0;
    }

    public static int getViewWidth(View view) {
        if (view != null) {
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int width = view.getMeasuredWidth();
            return width;
        }
        return 0;
    }

    /**
     * This will show or hide keyboard, this can be used on button click or inside onCreate also.
     */
    public static void showHideKeyboard(Context context, View view, boolean show) {
        if (show) {
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setDrawable(View view, Drawable drawable) {
        view.setBackground(drawable);
    }

    public static int getColor(int color) {
        return ContextCompat.getColor(ShivamApplication.getAppContext(), color);
    }

    /**
     * convert sp to px, this can be used to give font size programmatically in spannable strings.
     */
    public static float getSpToPixel(int sp) {
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    public static int getDpToPixel(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    public static void paintMenuItemIcon(MenuItem menuItem) {
        Drawable drawable = menuItem.getIcon();
        drawable.setColorFilter(ContextCompat.getColor(ShivamApplication.getAppContext(), R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    public static Drawable getDrawable(int drawableId) {
        return ContextCompat.getDrawable(ShivamApplication.getAppContext(), drawableId);
    }




    public static int getDimension(int resourceId) {
        int valueInPixels = (int) ShivamApplication.getAppContext().getResources().getDimension(resourceId);
        return valueInPixels;
    }

    public static boolean isActivityAlive(Activity activity) {
        if (activity == null
                || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {

            return false;
        }

        return true;
    }

    // Checks if a fragment is destroyed or detached or removed from context
    public static boolean isFragmentAlive(Fragment fragment) {
        return !(fragment.isRemoving() || fragment.getActivity() == null || fragment.isDetached() || !fragment.isAdded() || fragment.getView() == null);
    }

    /**
     * This method will disable all child views click.
     *
     * @param view
     * @param enabled
     */
    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    /**
     * Converts given string into spannable string and apply color to the "*" in the required field
     */
    public static SpannableString getHintWithRequiredColor(String hint, boolean isRequired) {
        SpannableString hintWithRequiredColor = new SpannableString(hint);
        if (isRequired && hint.contains("*")) {
            int index = hint.lastIndexOf("*");
            hintWithRequiredColor.setSpan(new ForegroundColorSpan(getColor(R.color.vymo_red)), index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return hintWithRequiredColor;
        }
        return hintWithRequiredColor;
    }

    /**
     * Append star(*) to the given string based on minRequiredCount parameter
     */
    public static String getHintText(String hintText, int minRequiredCount) {
        String requiredSuffix = minRequiredCount > 0 ? "*" : "";
        return hintText + requiredSuffix;
    }

    public static int getScreenWidth(Activity mActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


}
