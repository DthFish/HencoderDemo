package com.dthfish.hencoderdemo.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.dthfish.hencoderdemo.R;
import com.dthfish.hencoderdemo.Utils;

/**
 * Description
 * Author DthFish
 * Date  2018/7/20.
 */
public class MaterialEditText extends AppCompatEditText {
    public static final float LABEL_SIZE = Utils.dpToPx(12);
    public static final float LABEL_OFFSET_Y = Utils.dpToPx(8);
    public static final float LABEL_OFFSET = Utils.dpToPx(4);
    public static final float LABEL_TRANSITION_Y = Utils.dpToPx(14);

    public static final float ICON_PADDING = Utils.dpToPx(8);


    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float percent = 0;
    private ObjectAnimator animator;
    private boolean labelShow = false;
    private boolean useFloatingLabel = true;
    private int accentColor;
    private int leftIcon;
    private Bitmap leftBitmap;

    public MaterialEditText(Context context) {
        super(context);
        init(context, null);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
        useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, true);
        leftIcon = typedArray.getResourceId(R.styleable.MaterialEditText_leftIcon, 0);
        typedArray.recycle();

        TypedArray typedArrayAccent = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.colorAccent});
        accentColor = typedArrayAccent.getColor(0, Color.BLACK);
        typedArrayAccent.recycle();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (useFloatingLabel) {
                    if (s.length() > 0) {
                        if (!labelShow) {
                            labelShow = true;
                            getAnimator().start();
                        }
                    } else if (labelShow) {
                        labelShow = false;
                        getAnimator().reverse();
                    }
                }
            }
        });

        if (useFloatingLabel) {
            setPadding((int) (getPaddingLeft() + getIconOffset()), (int) (getPaddingTop() + LABEL_SIZE + LABEL_OFFSET_Y),
                    getPaddingRight(), getPaddingBottom());

        } else if (leftIcon != 0) {
            setPadding((int) (getPaddingLeft() + getIconOffset()), getPaddingTop(),
                    getPaddingRight(), getPaddingBottom());
        }
        if (leftIcon != 0)
            leftBitmap = Utils.getAvatar(getResources(), leftIcon, getTextSize() * 1.5f);
    }

    {
        setBackground(null);
        paint.setTextSize(LABEL_SIZE);

    }


    public int getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(@DrawableRes int leftIcon) {
        float prePaddingLeftOffset = (int) getIconOffset();
        this.leftIcon = leftIcon;
        if (leftIcon == 0) {
            if (leftBitmap != null)
                leftBitmap.recycle();
            leftBitmap = null;
        } else {
            leftBitmap = Utils.getAvatar(getResources(), leftIcon, getTextSize() * 1.5f);
        }

        setPadding((int) (getPaddingLeft() - prePaddingLeftOffset + getIconOffset()), getPaddingTop(),
                getPaddingRight(), getPaddingBottom());

    }

    public ObjectAnimator getAnimator() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(
                    MaterialEditText.this, "percent", 0, 1);
        }
        return animator;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }

    public boolean isUseFloatingLabel() {
        return useFloatingLabel;
    }

    public void setUseFloatingLabel(boolean useFloatingLabel) {
        float prePaddingTopOffset = this.useFloatingLabel ? LABEL_SIZE + LABEL_OFFSET_Y : 0f;
        float paddingTopOffset = useFloatingLabel ? LABEL_SIZE + LABEL_OFFSET_Y : 0f;
        this.useFloatingLabel = useFloatingLabel;

        setPadding(getPaddingLeft(), (int) (getPaddingTop() - prePaddingTopOffset + paddingTopOffset),
                getPaddingRight(), getPaddingBottom());
    }

    private float getIconOffset() {

        return leftIcon == 0 ? 0 : ICON_PADDING * 2 + getTextSize() * 1.5f + LABEL_OFFSET;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (useFloatingLabel) {
            paint.setColor(accentColor);
            CharSequence hint = getHint();
            paint.setAlpha((int) (percent * 0xFF));
            canvas.drawText(hint, 0, hint.length(), LABEL_OFFSET + getIconOffset(),
                    LABEL_SIZE + LABEL_OFFSET_Y + LABEL_TRANSITION_Y * (1 - percent), paint);
            paint.setAlpha(0xFF);
        }

        if (hasFocus()) {
            paint.setColor(accentColor);
            paint.setStrokeWidth(Utils.dpToPx(2));

        } else {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(Utils.dpToPx(0.75f));
        }
        canvas.drawLine(LABEL_OFFSET + getIconOffset(), getBottom() - Utils.dpToPx(8),
                getWidth() - LABEL_OFFSET, getBottom() - Utils.dpToPx(8), paint);

        if (leftBitmap != null) {
            canvas.drawBitmap(leftBitmap, LABEL_OFFSET + ICON_PADDING,
                    getBottom() - Utils.dpToPx(10) - getTextSize() * 1.5f, paint);
        }

    }
}
