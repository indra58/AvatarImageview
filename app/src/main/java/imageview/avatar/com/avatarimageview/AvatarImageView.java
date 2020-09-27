package imageview.avatar.com.avatarimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class AvatarImageView extends FrameLayout {

    private static final int DEFAULT_SHAPE = 0;
    private static final int DEFAULT_RADIUS = 40;
    private static final int DEFAULT_MARGIN = 0;

    private static final int DEFAULT_TEXT_COLOR = R.color.default_text_color;
    private static final float DEFAULT_TEXT_SIZE = 20f;
    private static final int DEFAULT_FONT_STYLE = -1;
    private static final int DEFAULT_STROKE_WIDTH = 0;
    private static final int DEFAULT_STROKE_COLOR = R.color.default_stroke_color;

    private static final boolean DEFAULT_GRADIENT_ENABLED = false;
    private static final int DEFAULT_BG_COLOR = -1;
    private static final int DEFAULT_START_COLOR = -1;
    private static final int DEFAULT_CENTER_COLOR = -1;
    private static final int DEFAULT_END_COLOR = -1;
    private static final int DEFAULT_GRADIENT_ANGLE = 0;

    private int mShape;
    private int mRadius;
    private int mMargin;
    private int mTextColor;
    private float mTextSize;
    private int mFontStyle;
    private int mStrokeWidth;
    private int mStrokeColor;

    private boolean mGradientEnabled;
    private int mBgColor;
    private int mStartColor;
    private int mCenterColor;
    private int mEndColor;
    private int mGradientAngle;

    private AppCompatImageView mAppCompatImageView;
    private AppCompatTextView mAppCompatTextView;
    private Context mContext;

    private UserAvatar mUserAvatar;

    public AvatarImageView(@NonNull Context context) {
        this(context, null);
    }

    public AvatarImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, defStyleAttr, 0);

        mShape = typedArray.getInteger(R.styleable.AvatarImageView_aiv_shape, DEFAULT_SHAPE);
        mMargin = typedArray.getInteger(R.styleable.AvatarImageView_aiv_margin, DEFAULT_MARGIN);
        mRadius = typedArray.getInteger(R.styleable.AvatarImageView_aiv_radius, DEFAULT_RADIUS);
        mTextColor = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_text_color, DEFAULT_TEXT_COLOR);
        mTextSize = typedArray.getFloat(R.styleable.AvatarImageView_aiv_text_size, DEFAULT_TEXT_SIZE);
        mStrokeWidth = typedArray.getInt(R.styleable.AvatarImageView_aiv_stroke_width, DEFAULT_STROKE_WIDTH);
        mStrokeColor = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_stroke_color, DEFAULT_STROKE_COLOR);

        mFontStyle = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_font_family, DEFAULT_FONT_STYLE);

        mGradientEnabled = typedArray.getBoolean(R.styleable.AvatarImageView_aiv_gradient_enabled, DEFAULT_GRADIENT_ENABLED);
        mBgColor = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_bg_color, DEFAULT_BG_COLOR);
        mStartColor = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_start_color, DEFAULT_START_COLOR);
        mCenterColor = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_center_color, DEFAULT_CENTER_COLOR);
        mEndColor = typedArray.getResourceId(R.styleable.AvatarImageView_aiv_end_color, DEFAULT_END_COLOR);
        mGradientAngle = typedArray.getInteger(R.styleable.AvatarImageView_aiv_gradient_angle, DEFAULT_GRADIENT_ANGLE);

        typedArray.recycle();

        init();
    }

    private void init() {
        mAppCompatImageView = new AppCompatImageView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mAppCompatImageView.setLayoutParams(params);

        mAppCompatTextView = new AppCompatTextView(mContext);
        mAppCompatTextView.setLayoutParams(params);
        mAppCompatTextView.setTextColor(ContextCompat.getColor(mContext, mTextColor));
        mAppCompatTextView.setTextSize(mTextSize);
        mAppCompatTextView.setGravity(Gravity.CENTER);
        if (mFontStyle != DEFAULT_FONT_STYLE) {
            mAppCompatTextView.setTypeface(ResourcesCompat.getFont(mContext, mFontStyle));
        }
        GradientDrawable shortNameDrawable;

        if (mGradientEnabled) {
            if (mStartColor == DEFAULT_START_COLOR && mCenterColor == DEFAULT_CENTER_COLOR && mEndColor == DEFAULT_END_COLOR) {
                shortNameDrawable = new GradientDrawable(getGradientOrientation(),
                        new int[]{ColorUtils.getInstance(mContext).getRandomColor(),
                                ColorUtils.getInstance(mContext).getRandomColor(),
                                ColorUtils.getInstance(mContext).getRandomColor()});
            } else {
                shortNameDrawable = new GradientDrawable(getGradientOrientation(),
                        new int[]{mStartColor != DEFAULT_START_COLOR ? ContextCompat.getColor(mContext, mStartColor) :
                                ColorUtils.getInstance(mContext).getRandomColor(),
                                mCenterColor != DEFAULT_CENTER_COLOR ? ContextCompat.getColor(mContext, mCenterColor) :
                                        ColorUtils.getInstance(mContext).getRandomColor(),
                                mEndColor != DEFAULT_END_COLOR ? ContextCompat.getColor(mContext, mEndColor) :
                                        ColorUtils.getInstance(mContext).getRandomColor()});
            }
        } else {
            shortNameDrawable = new GradientDrawable();
            shortNameDrawable.setColor(mBgColor != DEFAULT_BG_COLOR ?
                    ContextCompat.getColor(mContext, mBgColor) :
                    ColorUtils.getInstance(mContext).getRandomColor());
        }
        //Curved
        if (mShape == 2) {
            shortNameDrawable.setShape(GradientDrawable.RECTANGLE);
            shortNameDrawable.setCornerRadius(mRadius);
            shortNameDrawable.setStroke(mStrokeWidth, ContextCompat.getColor(mContext, mStrokeColor));
        } else {
            shortNameDrawable.setShape(GradientDrawable.OVAL);
        }
        shortNameDrawable.setSize(mAppCompatTextView.getWidth(), mAppCompatTextView.getHeight());
        mAppCompatTextView.setBackgroundDrawable(shortNameDrawable);

        addView(mAppCompatImageView);
        addView(mAppCompatTextView);

        hideTextView();
    }

    private GradientDrawable.Orientation getGradientOrientation() {
        switch (mGradientAngle) {
            case 45:
                return GradientDrawable.Orientation.BL_TR;
            case 90:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 135:
                return GradientDrawable.Orientation.BR_TL;
            case 180:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 225:
                return GradientDrawable.Orientation.TR_BL;
            case 270:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 315:
                return GradientDrawable.Orientation.TL_BR;
            default:
                return GradientDrawable.Orientation.LEFT_RIGHT;
        }
    }

    public void setAvatar(UserAvatar userAvatar) {
        mAppCompatImageView.setImageResource(0);

        mUserAvatar = userAvatar;

        switch (mShape) {
            case 1:
                setRoundImage();
                break;
            case 2:
                setCurvedImage();
                break;
            default:
                setNormalImage();
                break;
        }
    }

    private void setNormalImage() {
        GlideApp.with(mAppCompatImageView)
                .load(mUserAvatar.getImageFile() != null ? mUserAvatar.getImageFile() : mUserAvatar.getAvatarImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        setAvatarText();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaded();
                        return false;
                    }
                })
                .into(mAppCompatImageView);
    }

    private void setCurvedImage() {
        GlideApp.with(mAppCompatImageView)
                .load(mUserAvatar.getImageFile() != null ? mUserAvatar.getImageFile() : mUserAvatar.getAvatarImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transform(new RoundedCornersTransformation(mContext, mRadius, mMargin))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        setAvatarText();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaded();
                        return false;
                    }
                })
                .into(mAppCompatImageView);
    }

    private void setRoundImage() {
        GlideApp.with(mAppCompatImageView)
                .load(mUserAvatar.getImageFile() != null ? mUserAvatar.getImageFile() : mUserAvatar.getAvatarImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .apply(RequestOptions.circleCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        setAvatarText();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageLoaded();
                        return false;
                    }
                })
                .into(mAppCompatImageView);
    }

    private void hideImageView() {
        mAppCompatImageView.setVisibility(GONE);
    }

    private void hideTextView() {
        mAppCompatTextView.setVisibility(GONE);
    }

    private void showImageView() {
        mAppCompatImageView.setVisibility(VISIBLE);
    }

    private void showTextView() {
        mAppCompatTextView.setVisibility(VISIBLE);
    }

    private void imageLoaded() {
        showImageView();
        hideTextView();
    }

    private void setAvatarText() {
        hideImageView();
        showTextView();

        mAppCompatTextView.setText(mUserAvatar.getAvatarName().isEmpty() ? "" : NameUtils.getShortName(mUserAvatar.getAvatarName()));
    }
}
