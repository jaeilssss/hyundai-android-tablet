/*
 * Copyright (C) 2011 - Riccardo Ciovati
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.obigo.hkmotors.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.obigo.hkmotors.R;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Class for custom keyboard
 */
public class CustomKeyboardView extends KeyboardView {

    private Context mContext;
    private Keyboard mKeyBoard;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mKeyBoard = this.getKeyboard();
        List<Keyboard.Key> keys = null;
        if (mKeyBoard != null) {
            keys = mKeyBoard.getKeys();
        }

        // when keyboard is pressed or selected, keyboard image should be changed
        if (keys != null) {
            for (Keyboard.Key key : keys) {
                if (key.pressed && key.codes[0] == 8) {
                    drawKeyBackground(R.drawable.keypad_1_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 9) {
                    drawKeyBackground(R.drawable.keypad_2_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 10) {
                    drawKeyBackground(R.drawable.keypad_3_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 11) {
                    drawKeyBackground(R.drawable.keypad_4_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 12) {
                    drawKeyBackground(R.drawable.keypad_5_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 13) {
                    drawKeyBackground(R.drawable.keypad_6_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 14) {
                    drawKeyBackground(R.drawable.keypad_7_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 15) {
                    drawKeyBackground(R.drawable.keypad_8_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 16) {
                    drawKeyBackground(R.drawable.keypad_9_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 7) {
                    drawKeyBackground(R.drawable.keypad_0_pre, canvas, key);
                } else if (key.pressed && key.codes[0] == 67) {
                    drawKeyBackground(R.drawable.keypad_delete_pre, canvas, key);
                }
            }
        }
    }

    /**
     * Draw keyboard background
     *
     * @param drawableId - drawable resource id
     * @param canvas - canvas
     * @param key - key
     */
    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {

        Drawable npd = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            npd = mContext.getResources().getDrawable(drawableId, mContext.getTheme());
        } else {
            npd = mContext.getResources().getDrawable(drawableId);
        }
//        Drawable npd = mContext.getResources().getDrawable(drawableId);

        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }

        // 태블릿으로하니 정확하게 이미지 나오지 않아 고정값 적용 ...
        npd.setBounds(key.x+200, key.y+10, key.x + key.width-200, key.y + key.height-10);
        npd.draw(canvas);
    }

    /**
     * Draw text
     * Currently, it isn't used
     *
     * @param canvas - canvas
     * @param key - key
     */
    private void drawText(Canvas canvas, Keyboard.Key key) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);


        paint.setAntiAlias(true);

        paint.setColor(Color.WHITE);
        if (key.label != null) {
            String label = key.label.toString();

            Field field;

            if (label.length() > 1 && key.codes.length < 2) {
                int labelTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    labelTextSize = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(labelTextSize);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                int keyTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    keyTextSize = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(keyTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            }

            paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                    .length(), bounds);
            canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                    (key.y + key.height / 2) + bounds.height() / 2, paint);
        } else if (key.icon != null) {
            key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2, key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
                    key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(), key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
            key.icon.draw(canvas);
        }
    }

    /**
     * Show the keyboard with animation
     *
     * @param animation - animation
     */
    public void showWithAnimation(Animation animation) {
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
            }
        });

        setAnimation(animation);
    }
}