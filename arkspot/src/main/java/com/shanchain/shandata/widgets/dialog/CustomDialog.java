package com.shanchain.shandata.widgets.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;

import java.util.List;

/**
 * Created by 周建 on 2017/5/30.
 */

public class CustomDialog extends AlertDialog implements View.OnClickListener {
    private Context context;      // 上下文
    private boolean isBottom = false;     //是否在底部
    private boolean isAnimator = false;   //是否有动画效果
    private double ratio = 0.8;     //屏幕宽度占比
    private int layoutResID;      // 布局文件id
    private int[] listenedItems;  // 要监听的控件id
    private Boolean isShow;  // 是否显示删除控件控件view
    private String dialogMsgs = null;  // 显示的消息

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

    public CustomDialog(Context context, boolean isBottom, double ratio, int layoutResID, int[] listenedItems, boolean isShow) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.isBottom = isBottom;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
        this.isShow = isShow;
    }

    public CustomDialog(Context context, boolean isBottom, double ratio, int layoutResID, int[] listenedItems, String dialogMsgs) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.isBottom = isBottom;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
        this.dialogMsgs = dialogMsgs;
    }


    public CustomDialog(Context context, double ratio, int layoutResID, int[] listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式

        this.context = context;
        this.ratio = ratio;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
    }


    public CustomDialog(Context context, int layoutResID, int listenedItems) {
        super(context, R.style.dialog_custom); //dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
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

    private OnItemClickListener listener;

    private OnAddTextChangedListener textChangedListener;

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
                switch (id) {
                    case R.id.tv_report_dialog_report:
                        if (!isShow) {
                            TextView textView = (TextView) this.findViewById(id);
                            textView.setBackgroundResource(R.drawable.shape_bg_dialog_item);
                            findViewById(R.id.report_dialog_view).setVisibility(View.GONE);
                        }
                        break;
                    case R.id.dialog_msg:
                        TextView textView = (TextView) this.findViewById(id);
                        textView.setText(this.dialogMsgs);
                        break;
                    case R.id.et_input_dialog_bounty:
                        final EditText editText = (EditText) this.findViewById(id);
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (textChangedListener != null) {
                                    textChangedListener.TextChanged(CustomDialog.this, editText,s.toString(),start,before,count);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        break;
                }
            }

        }
        if (isShow == null) {
            return;
        }
        if (isShow) {
            findViewById(R.id.tv_report_dialog_delete).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tv_report_dialog_delete).setVisibility(View.GONE);
        }

        //将传入的dialogMsg赋值个dialog_msg
//        if (dialogMsgs!=null){
//            TextView dialogMsgView = (TextView) findViewById(R.id.dialog_msg);
//            dialogMsgView.setText("删除动态");
//        }


    }

    public View getByIdView(@IdRes int Id) {
        View view = findViewById(Id);
        return view;
    }

    //    @Override
//    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {
//
//    }
    public interface OnAddTextChangedListener {
        void TextChanged(CustomDialog dialog, EditText editText,String s,int start, int before, int count);
    }

    public interface OnItemClickListener {
        void OnItemClick(CustomDialog dialog, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setAddTextChangedListener(OnAddTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    @Override
    public void onClick(View view) {
        //dismiss();//只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnItemClick(this, view);
    }

}
