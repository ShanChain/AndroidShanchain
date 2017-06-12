package com.shanchain.widgets.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shanchain.R;

import java.util.List;

/**
 * Created by 周建 on 2017/5/30.
 */

public class CustomDialog extends AlertDialog implements View.OnClickListener {
    private Context context;      // 上下文
    private boolean isBottom;     //是否在底部
    private boolean isAnimator;   //是否有动画效果
    private double ratio = 1;     //屏幕宽度占比
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id

    public CustomDialog(Context context, boolean isBottom , int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.isBottom = isBottom;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }

    public CustomDialog(Context context, boolean isBottom , double ratio, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式

        this.context = context;
        this.isBottom = isBottom;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }


    public CustomDialog(Context context, boolean isBottom , boolean isAnimator,double ratio, int layoutResID, int[] listenedItems) {
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
        if (isBottom){
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置为底部
        }else {
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        }
        if (isAnimator){
            window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        }

        setContentView(layoutResID);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.width = (int) (display.getWidth()*ratio); // 设置dialog宽度为屏幕的多少

        getWindow().setAttributes(lp);
        //点击Dialog外部消失
        setCanceledOnTouchOutside(true);
        //遍历控件id,添加点击事件
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
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
        dismiss();//只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnItemClick(this, view);
    }
}
