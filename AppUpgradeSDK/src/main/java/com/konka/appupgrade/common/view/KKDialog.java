package com.konka.appupgrade.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.konka.appupgrade.R;

/**
 * 该对话框代码从康佳控件库拷贝，去除一些列表项，只保留消息对话框
 */
public class KKDialog extends Dialog implements OnClickListener, View.OnFocusChangeListener{

    private TextView mTitle;
    private TextView mMessage;
    private Button mNegativeButton;
    private Button mPositiveButton;
    private ImageView mIcon;
    private View mSelectBoxView;

    private ViewGroup mContent;

    private OnClickListener mNegativeListener;
    private OnClickListener mPositiveListener;

    private static int moveYoffset = 0;

    protected static final int MODE_MESSAGE = 1;
    protected static final int MODE_ITEMS = 2;
    protected static int mode = MODE_MESSAGE;


    //add by qujiabin
    private int mReuseKeyOfOK =  0xffffffff;
    //add by qujiabin
    private View.OnKeyListener onKeyListener = new View.OnKeyListener(){

        /**
         * Called when a hardware key is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         * <p>Key presses in software keyboards will generally NOT trigger this method,
         * although some may elect to do so in some situations. Do not assume a
         * software input method has to be key-based; even if it is, it may use key presses
         * in a different way than you expect, so there is no way to reliably catch soft
         * input key presses.
         *
         * @param v       The view the key has been dispatched to.
         * @param keyCode The code for the physical key that was pressed
         * @param event   The KeyEvent object containing full information about
         *                the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
//            if(reuseKeyOfOK == 0xffffffff){
//                return false;
//            }
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == mReuseKeyOfOK) {
                    int i = v.getId();
                    if (i == R.id.button_positive) {
                        if (mPositiveListener != null) {
                            mPositiveListener.onClick(KKDialog.this, 0);
                        } else {
                            cancel();
                        }
                    } else if (i == R.id.button_negative) {
                        if (mNegativeListener != null) {
                            mNegativeListener.onClick(KKDialog.this, 0);
                        } else {
                            cancel();
                        }
                    }
                    return true;
                } else {

                }
            }
            return false;
        }
    };

    private void setReuseKeyOfOK(int reuseKeyOfOK){
        this.mReuseKeyOfOK = reuseKeyOfOK;
    }



    public KKDialog(Context context, int mode) {
        super(context, R.style.KKDialog);
        setupViews();
        if(mode != MODE_ITEMS) {
            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay();
            WindowManager.LayoutParams p = getWindow().getAttributes();
            p.width = (int) (d.getWidth());
            getWindow().setAttributes(p);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_positive) {
            if (mPositiveListener != null) {
                mPositiveListener.onClick(this, 0);
            } else {
                cancel();
            }

        } else if (i == R.id.button_negative) {
            if (mNegativeListener != null) {
                mNegativeListener.onClick(this, 1);
            } else {
                cancel();
            }

        } else {
        }
    }


    private void setupViews(){
        if(mode == MODE_ITEMS){
            //setContentView(R.layout.kkdialog_list);
        } else {
            setContentView(R.layout.kkdialog_message_sdk);
            mSelectBoxView = (View)findViewById(R.id.select_box);
            mSelectBoxView.setVisibility(View.INVISIBLE);
            if(0 == moveYoffset) {
                moveYoffset = (int)mSelectBoxView.getResources().getDimension(R.dimen.selectbox_x_offset);
            }
        }

        mTitle = (TextView) findViewById(R.id.title);
        mIcon = (ImageView)findViewById(R.id.icon);
        mMessage = (TextView)findViewById(R.id.msg_text);

        mNegativeButton = (Button)findViewById(R.id.button_negative);
        mPositiveButton = (Button)findViewById(R.id.button_positive);

        mContent = (ViewGroup)findViewById(R.id.layout_content);

        mNegativeButton.setOnClickListener(this);
        mPositiveButton.setOnClickListener(this);
        if(mode == MODE_MESSAGE){
	        mPositiveButton.setOnFocusChangeListener(this);
	        mNegativeButton.setOnFocusChangeListener(this);
        }
    }

    private void setTitle(String title){
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(title);
    }

    private void setTitleColor(int color){
    	mTitle.setTextColor(color);
    }

    private void setMessageColor(int color){
    	mMessage.setTextColor(color);
    }

    private void setIcon(Drawable resId){
        mIcon.setVisibility(View.VISIBLE);
        mIcon.setImageDrawable(resId);
    }

    private void setListWidth(int width){
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_content);
        LayoutParams layoutParams = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
    }

    private void setBackground(Drawable resId){
    	LinearLayout layout = (LinearLayout) findViewById(R.id.parent_layout);
    	layout.setBackground(resId);
    }

    private void setMessage(CharSequence message){
        mMessage.setVisibility(View.VISIBLE);
        mMessage.setText(message);
    }

    private void setView(View view) {
        if(view != null){
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mContent.addView(view, layoutParams);
        }
    }

    private void setNegativeButton(CharSequence text, OnClickListener listener) {
        mNegativeButton.setVisibility(View.VISIBLE);
        mNegativeButton.setText(text);
        mNegativeButton.setOnKeyListener(onKeyListener);    //add by qujiabin
        this.mNegativeListener = listener;
    }

    private void setPositiveButton(CharSequence text, OnClickListener listener){
        mPositiveButton.setVisibility(View.VISIBLE);
        mPositiveButton.setText(text);
        mPositiveButton.setOnKeyListener(onKeyListener);    //add by qujiabin
        this.mPositiveListener = listener;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    	if(mode == MODE_MESSAGE){
	    	if(v instanceof Button){
		        if(hasFocus) {
		            startFocusChangeAnimation(v, new MyAnimatorListener(v));
		        } else {
		        	if (v instanceof Button){
		            ((Button)v).setTextColor(0xff787878);
		            v.setBackgroundResource(R.drawable.kkdialog_btn_nor_bg);
		        	}
		        }
	    	}
    	}
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(mPositiveButton != null && mPositiveButton.getVisibility() == View.VISIBLE){
            mPositiveButton.requestFocus();
        }else if(mNegativeButton != null && mNegativeButton.getVisibility() == View.VISIBLE){
            mNegativeButton.requestFocus();
        }
        if(mode == MODE_MESSAGE)
        	startFocusChangeAnimation(getCurrentFocus(), new MyAnimatorListener(getCurrentFocus()));
    }


    private class MyAnimatorListener extends AnimatorListenerAdapter {
        private View curView;

        public MyAnimatorListener(View view) {
            curView = view;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (mSelectBoxView.getVisibility() != View.VISIBLE){
                mSelectBoxView.setVisibility(View.VISIBLE);
            }
            refershFocusState();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            refershFocusState();
        }

        private void refershFocusState() {
        	if(curView instanceof Button){
	            if(curView.hasFocus()) {
	                ((Button)curView).setTextColor(0xffffffff);
	                curView.setBackgroundResource(R.drawable.kkdialog_btn_sel_bg);
	            } else {
	                ((Button)curView).setTextColor(0xff787878);
	                curView.setBackgroundResource(R.drawable.kkdialog_btn_nor_bg);
	            }
        	}

        }
    }

    //按钮焦点移动效果
    /**modify by qujiabin at 2016.6.28
     * add:  371-373
     * solve question:not judge the focus view is null or not. NullPointerException at {focusView.getLocationOnScreen(int[])}
     * */
    private void startFocusChangeAnimation(View focusView, MyAnimatorListener myAnimatorListener) {
        if(focusView == null){
            return;
        }
        int[] fromLocation = new int[2];
        mSelectBoxView.getLocationOnScreen(fromLocation);

        float fromX = fromLocation[0];
        int[] toLocation = new int[2];
        focusView.getLocationOnScreen(toLocation);

        final float toX = toLocation[0];
        final float scale = focusView.getResources().getDisplayMetrics().density;
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(mSelectBoxView, "x", fromX-296*scale, toX - moveYoffset-296*scale);
        translateXAnimator.setDuration(150);
        if (myAnimatorListener != null) {
            translateXAnimator.addListener(myAnimatorListener);
        }
        translateXAnimator.start();
    }


    public static class Builder{
        private KKDialog mKKDialogNew;

        private Context mContext;
        private CharSequence mTitle;
        private Drawable mIconDrawable;
        private Drawable mBackground;
        private CharSequence mMessage;
        private int mTitleColor = R.color.dialog_title_color;
        private int mMessageColor = R.color.dialog_message_color;

        private CharSequence mNegative;
        private CharSequence mPositive;
        private int mListWidth = 0;

        private OnClickListener mNegavtiveListener;
        private OnClickListener mPositiveListener;

        private View mView;

        private CharSequence[] mItems;

        private OnClickListener mSingleChoiceClickListener;
        private OnMultiChoiceClickListener mMultiChoiceClickListener;
        private int mCheckedItem;
        private boolean[] mCheckedItems;
        private boolean mIsSingleChoice = false;
        private boolean mIsMultiChoice = false;

        //add by qujiabin
        private int mReuseKeyOfOK = 0xffffffff;

        public Builder(Context context){
            mContext = context;
        }

        public Builder setTitle(CharSequence title){
            mTitle = title;
            return this;
        }
        
        public Builder setTitleColor(int titleColor){
        	mTitleColor = titleColor;
        	return this;
        }
        
        public Builder setMessageColor(int messageColor){
        	mMessageColor = messageColor;
        	return this;
        }

        public Builder setTitle(int titleId) {
            setTitle(mContext.getString(titleId));
            return this;
        }

        public Builder setMessage(CharSequence message){
            mode = MODE_MESSAGE;
            mMessage = message;
            return this;
        }

        public Builder setMessage(int messageId) {
            setMessage(mContext.getString(messageId));
            return this;
        }

        public Builder setIcon(Drawable icon){
            this.mIconDrawable = icon;
            return this;
        }

        public Builder setIcon(int iconId){
            setIcon(mContext.getResources().getDrawable(iconId));
            return this;
        }
        
        public Builder setBackground(Drawable bg){
        	this.mBackground = bg;
        	return this;
        }
        
        public Builder setBackground(int bgId){
        	setBackground(mContext.getResources().getDrawable(bgId));
        	return this;
        }

        public Builder setListWidth(int width){
            mListWidth = width;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener){
            mode = MODE_ITEMS;
            mItems = items;
            mSingleChoiceClickListener = listener;
            mCheckedItem = checkedItem;
            mIsSingleChoice = true;
            return this;
        }

        public void setView(View view){
            mView = view;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, OnMultiChoiceClickListener
                                           listener){
            mode = MODE_ITEMS;
            mItems = items;
            mMultiChoiceClickListener = listener;
            mCheckedItems = checkedItems;
            mIsMultiChoice = true;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener){
            this.mNegative = text;
            this.mNegavtiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, OnClickListener listener){
            setNegativeButton(mContext.getString(textId), listener);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener){
            this.mPositive = text;
            this.mPositiveListener = listener;
            return this;
        }

        public Builder setPosiviteButton(int textId, OnClickListener listener){
            setPositiveButton(mContext.getString(textId), listener);
            return this;
        }

        //add by qujiabin
        /**
         * define a key to reuse as {@link KeyEvent#KEYCODE_DPAD_CENTER}
         * @param reuseKeyOfOK use like {@link KeyEvent#KEYCODE_DPAD_CENTER}
         * @return
         */
        public Builder setReuseKeyOfOK(int reuseKeyOfOK) {
            this.mReuseKeyOfOK = reuseKeyOfOK;
            return this;
        }

        public KKDialog create(){
            final KKDialog kkDialogNew = new KKDialog(mContext, mode);

            if(mTitle != null){
                kkDialogNew.setTitle(mTitle.toString());
            }

            if(mMessage != null){
                kkDialogNew.setMessage(mMessage);
            }
            
            if(mTitleColor != R.color.dialog_title_color){
            	kkDialogNew.setTitleColor(mTitleColor);
            }
            
            if(mMessageColor != R.color.dialog_message_color){
            	kkDialogNew.setMessageColor(mMessageColor);
            }

            if(mIconDrawable != null){
                kkDialogNew.setIcon(mIconDrawable);
            }
            
            if(mBackground != null){
            	kkDialogNew.setBackground(mBackground);
            }

            if(mNegative != null){
                kkDialogNew.setNegativeButton(mNegative, mNegavtiveListener);
            }

            if(mPositive != null){
                kkDialogNew.setPositiveButton(mPositive, mPositiveListener);
            }

            if(mListWidth != 0){
                kkDialogNew.setListWidth(mListWidth);
            }


            if(mView != null){
                kkDialogNew.setView(mView);
            }

            //add by qujiabin
            if (mReuseKeyOfOK != 0xffffffff){
                kkDialogNew.setReuseKeyOfOK(mReuseKeyOfOK);
            }
            //end

            mKKDialogNew = kkDialogNew;
            return kkDialogNew;


        }

        public KKDialog show() {
            create();
            mKKDialogNew.show();
            return mKKDialogNew;
        }


    }

    /**
     * add by qujiabin at 2016.07.20
     * make the developer can modify dialog content when dialog show.
     * Now you can only modify this following content:
     * title, title color, message, message color, icon, background, negative button, positive button<br>
     * Tips: now you could not make the content invisible if the content had showed in the dialog
     */
    public static class Editor{
        private KKDialog dialog;
        private Editor(KKDialog dialog){
            this.dialog = dialog;
        }
        public Editor setTitle(CharSequence title){
            if(title != null){
                dialog.setTitle(title);
            }
            return this;
        }

        public Editor setTitleColor(int titleColor){
            dialog.setTitleColor(titleColor);

            return this;
        }

        public Editor setMessageColor(int messageColor){
            dialog.setMessageColor(messageColor);

            return this;
        }

        public Editor setTitle(int titleId) {
            setTitle(dialog.getContext().getString(titleId));
            return this;
        }

        public Editor setMessage(CharSequence message){
            if(message != null){
                dialog.setMessage(message);
            }
            return this;
        }

        public Editor setMessage(int messageId) {
            setMessage(dialog.getContext().getString(messageId));
            return this;
        }

        public Editor setIcon(Drawable icon){
            if(icon != null){
                dialog.setIcon(icon);
            }
            return this;
        }

        public Editor setIcon(int iconId){
            setIcon(dialog.getContext().getResources().getDrawable(iconId));
            return this;
        }

        public Editor setBackground(Drawable bg){
            if(bg != null){
                dialog.setBackground(bg);
            }
            return this;
        }

        public Editor setBackground(int bgId){
            setBackground(dialog.getContext().getResources().getDrawable(bgId));
            return this;
        }

        public Editor setNegativeButton(CharSequence text, OnClickListener listener){
            if(text != null){
                dialog.setNegativeButton(text, listener);
            }
            return this;
        }

        public Editor setNegativeButton(int textId, OnClickListener listener){
            setNegativeButton(dialog.getContext().getString(textId), listener);
            return this;
        }

        public Editor setPositiveButton(CharSequence text, OnClickListener listener){
            if(text != null){
                dialog.setPositiveButton(text, listener);
            }
            return this;
        }

        public Editor setPosiviteButton(int textId, OnClickListener listener){
            setPositiveButton(dialog.getContext().getString(textId), listener);
            return this;
        }
    }
}






























