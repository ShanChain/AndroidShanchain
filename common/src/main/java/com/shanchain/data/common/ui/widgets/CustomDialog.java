package com.shanchain.data.common.ui.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.common.R;

import java.util.List;


public class CustomDialog extends AlertDialog implements View.OnClickListener {
    private Context context;      // 上下文
    private boolean isBottom = false;     //是否在底部
    private boolean isAnimator = false;   //是否有动画效果
    private double ratio = 0.8;     //屏幕宽度占比
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id
    private ImageView shareView, passwordView;
    private int drawableId, viewId;
    private String messageContent;
    private String title;
    private String sureText;
    private int messageContentSize;
    private String percentage;
    private Bitmap shareBitmap, passwordBitmap;
    private boolean isShow = false;
    private RelativeLayout relativeLayout, relativeAllText;

    public CustomDialog(Context context, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    public CustomDialog(Context context, boolean isBottom, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.isBottom = isBottom;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }


    public CustomDialog(Context context, boolean isBottom, double ratio, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式

        this.context = context;
        this.isBottom = isBottom;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    public CustomDialog(Context context, double ratio, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式

        this.context = context;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }


    public CustomDialog(Context context, boolean isBottom, boolean isAnimator, double ratio, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.isBottom = isBottom;
        this.isAnimator = isAnimator;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (isBottom) {
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置为底部
        } else {
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        }
        if (isAnimator) {
            window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        }
        setContentView(layoutResID);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.width = (int) (display.getWidth() * ratio); // 设置dialog宽度为屏幕的多少

        getWindow().setAttributes(lp);
        //点击Dialog外部消失
        setCanceledOnTouchOutside(true);
        //遍历控件id,添加点击事件
        if (listenedItems != null) {
            for (int id : listenedItems) {
                findViewById(id).setOnClickListener(this);
            }
        }
        if (messageContent != null) {
            TextView textView = (TextView) findViewById(R.id.even_message_content);
            textView.setText(messageContent);
        }
        if (messageContentSize != 0) {
            TextView textView = (TextView) findViewById(R.id.even_message_content);
            textView.setTextSize(messageContentSize);
        }
        if (title != null) {
            TextView textView = findViewById(R.id.tv_input_dialog_title);
            textView.setText(title);
        }
        if (sureText != null) {
            TextView sureTextView = findViewById(R.id.btn_dialog_task_detail_sure);
            sureTextView.setText(sureText);
        }
        if (drawableId != 0) {
            relativeLayout = findViewById(R.id.share_window);
            relativeLayout.setBackground(getContext().getResources().getDrawable(drawableId));
            relativeAllText = findViewById(R.id.allText);
            relativeAllText.setVisibility(View.VISIBLE);
        }
        if (percentage != null) {
            TextView tvPercentage = findViewById(R.id.percentage);
            tvPercentage.setText(percentage);
        }
        if (shareBitmap != null) {
            shareView = findViewById(R.id.share_image);
            shareView.setImageBitmap(shareBitmap);
            relativeLayout = findViewById(R.id.share_window);
            relativeLayout.setBackground(null);
            relativeAllText = findViewById(R.id.allText);
            relativeAllText.setVisibility(View.GONE);
            if (isShow == true) {
                relativeAllText = findViewById(R.id.allText);
                relativeAllText.setVisibility(View.VISIBLE);
            }
        }
        if (passwordBitmap != null) {
            passwordView = findViewById(R.id.iv_dialog_add_picture);
            TextView dialogSure = findViewById(R.id.tv_dialog_sure);
            passwordView.setImageBitmap(passwordBitmap);
            passwordView.setClickable(true);
            dialogSure.setBackground(this.getContext().getResources().getDrawable(R.drawable.common_shape_bg_btn_login));
        }
    }


    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
        if (shareView != null) {
            shareView.setBackground(null);
            shareView.setVisibility(View.GONE);
        }
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    public void setSureText(String sureText) {
        this.sureText = sureText;
    }

    public void setMessageContentSize(int size) {
        this.messageContentSize = size;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setShareBitmap(Bitmap shareBitmap) {
        this.shareBitmap = shareBitmap;
    }

    public void setShareBitmap(Bitmap shareBitmap, Boolean isShow) {
        this.shareBitmap = shareBitmap;
        this.isShow = isShow;
    }

    public void setPasswordBitmap(Bitmap passwordBitmap) {
        this.passwordBitmap = passwordBitmap;
    }


    public View getView(Context context, @IdRes int idRes) {
//        context = getContext();
        View layoutView = LayoutInflater.from(context).inflate(layoutResID, null);
        shareView = layoutView.findViewById(idRes);
        return shareView;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    private void setDialogTitle() {
        if (listenedItems != null) {
            for (int id : listenedItems) {
                findViewById(id);
            }
        }
    }

    private OnItemClickListener listener;

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    public interface OnItemClickListener {
        void OnItemClick(CustomDialog dialog, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.OnItemClick(this, view);
        } else {
            dismiss();//只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        }

    }
}
