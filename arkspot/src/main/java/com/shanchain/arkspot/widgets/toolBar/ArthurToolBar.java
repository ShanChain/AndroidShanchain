package com.shanchain.arkspot.widgets.toolBar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.utils.SystemUtils;


/**
 * 描述: 自定义标题栏
 */
public class ArthurToolBar extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "ArthurToolBar";

    // 必要属性 ---------------------------------------------------

    /**
     * 描述：全局上下文对象
     */
    private Context mContext = null;

    /**
     * 描述：全局资源对象
     */
    private Resources mResources = null;

    /**
     * 描述：ToolBar高度
     */
    private int toolHeight = 0;

    /**
     * 描述：屏幕宽度
     */
    private int mScreenWidth = 0;

    /**
     * 描述：是否关闭动画
     */
    private boolean isOpenAnim = false; // 默认关闭

    /**
     * 描述：活跃(选中)时Text大小
     */
    private float activeTextSize = 0;

    /**
     * 描述：闲置(非选中)时Text大小
     */
    private float inactiveTextSize = 0;

    // 子View ---------------------------------------------------

    /**
     * 描述：沉浸式View 默认关闭
     */
    private View mImmersiveView;

    /**
     * 描述：Toolbar View
     */
    private View mTitleLayoutView;

    /**
     * 描述：左侧按钮
     */
    private DrawableCenterTextView mLeftText;


    /**
     * 描述：中间标题
     */
    private TextView mTitleText;

    /**
     * 描述：右侧按钮
     */
    private DrawableCenterTextView mRightText;

    // 回调接口 ---------------------------------------------------

    /**
     * 描述：左侧按钮点击回调接口
     */
    private OnLeftClickListener mLeftListener;

    /**
     * 描述：右侧按钮点击回调接口
     */
    private OnRightClickListener mRightListener;

    // 构造方法 ---------------------------------------------------

    /**
     *  描述：标题点击回调接口
     *
     */
    private OnTitleClickListener mTitleListener;

    /**
     * 描述：构造方法
     */
    public ArthurToolBar(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 描述：构造方法
     */
    public ArthurToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 描述：构造方法
     */
    public ArthurToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 日期: 2017/1/10 10:47
     * 描述: 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        // 初始化上下文对象
        this.mContext = context;
        // 初始化资源对象
        this.mResources = mContext.getResources();
        // 获取屏幕宽度
        this.mScreenWidth = mResources.getDisplayMetrics().widthPixels;
        // 获取Toolbar高度
        this.toolHeight = (int) mResources.getDimension(R.dimen.arthur_navigation_height);
        // 获取动画范围
        this.activeTextSize = mResources.getDimensionPixelSize(R.dimen.sizeToolbarActive);
        this.inactiveTextSize = mResources.getDimensionPixelSize(R.dimen.sizeToolbarInactive);
        // 初始化View
        this.initView();
        // 获取自定义属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArthurToolbar, 0, 0);
            try {
                // 初始化资源
                initAttrs(typedArray);
            } finally {
                // 回收TypedArray
                typedArray.recycle();
            }
        }
        // 阴影效果
//        ViewCompat.setElevation(this, mResources.getDimension(R.dimen.arthur_navigation_elevation));
        // 控件绘制区域不在padding里面
        this.setClipToPadding(false);
        // 设置垂直方向
        this.setOrientation(VERTICAL);
        // 设置居中
        this.setGravity(Gravity.CENTER);
    }

    /**
     * 时间：15:44
     * 描述: 初始化属性
     */
    protected void initAttrs(TypedArray typedArray) {
        // 标题
        String titleText = typedArray.getString(R.styleable.ArthurToolbar_titleText);
        int titleTextSize = typedArray.getInteger(R.styleable.ArthurToolbar_titleTextSize, 18);
        int titleTextColor = typedArray.getColor(R.styleable.ArthurToolbar_titleColor, 0xff000000);
        int titleImage = typedArray.getResourceId(R.styleable.ArthurToolbar_titleImage, 0xff000000);

        if (null != titleText) setTitleText(titleText);
        if (18 != titleTextSize) setTitleTextSize(titleTextSize);
        if (0xff000000 != titleTextColor) setTitleTextColor(titleTextColor);
        if (0xff000000 != titleImage) setTitleImage(titleImage);

        // 左侧按钮
        String leftText = typedArray.getString(R.styleable.ArthurToolbar_leftText);
        int leftTextSize = typedArray.getInteger(R.styleable.ArthurToolbar_leftTextSize, 16);
        int leftTextColor = typedArray.getColor(R.styleable.ArthurToolbar_leftTextColor, 0xff000000);
        int leftImage = typedArray.getResourceId(R.styleable.ArthurToolbar_leftImage, 0xff000000);
        boolean leftEnabled = typedArray.getBoolean(R.styleable.ArthurToolbar_leftEnabled, true);
        boolean leftVisibility = typedArray.getBoolean(R.styleable.ArthurToolbar_leftVisibility, true);

        if (null != leftText) setLeftText(leftText);
        if (16 != leftTextSize) setLeftTextSize(leftTextSize);
        if (0xff000000 != leftTextColor) setLeftTextColor(leftTextColor);
        if (0xff000000 != leftImage) setLeftImage(leftImage);

        // 右侧按钮
        String rightText = typedArray.getString(R.styleable.ArthurToolbar_rightText);
        int rightTextSize = typedArray.getInteger(R.styleable.ArthurToolbar_rightTextSize, 16);
        int rightTextColor = typedArray.getColor(R.styleable.ArthurToolbar_rightTextColor, 0xff000000);
        int rightImage = typedArray.getResourceId(R.styleable.ArthurToolbar_rightImage, 0xff000000);
        boolean rightEnabled = typedArray.getBoolean(R.styleable.ArthurToolbar_rightEnabled, true);
        boolean rightVisibility = typedArray.getBoolean(R.styleable.ArthurToolbar_rightVisibility, true);

        if (null != rightText) setRightText(rightText);
        if (16 != rightTextSize) setRightTextSize(rightTextSize);
        if (0xff000000 != rightTextColor) setRightTextColor(rightTextColor);
        if (0xff000000 != rightImage) setRightImage(rightImage);

        //al 公共属性 是否禁用：默认为fse不禁用
        boolean disable = typedArray.getBoolean(R.styleable.ArthurToolbar_btnDisable, false);

        if (!disable) {
            setBtnEnabled(leftEnabled, rightEnabled);
            setBtnVisibility(leftVisibility, rightVisibility);
        } else {
            setBtnEnabled(!disable);
            setBtnVisibility(!disable);
        }
    }

    /**
     * 日期: 2017/1/10 11:15
     * 描述: 初始化View
     */
    private void initView() {
        // 初始化子View
        mImmersiveView = new View(mContext);
        mImmersiveView.setVisibility(GONE);
        //获取系统状态栏高度
        int statusBarHeight = SystemUtils.getStatusBarHeight(mContext);
        mImmersiveView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, statusBarHeight));
        mTitleLayoutView = LayoutInflater.from(mContext).inflate(R.layout.atthur_toolbar_view, this, false);
        mLeftText = (DrawableCenterTextView) mTitleLayoutView.findViewById(R.id.mLeftText);
        mTitleText = (TextView) mTitleLayoutView.findViewById(R.id.mTitleText);
        mRightText = (DrawableCenterTextView) mTitleLayoutView.findViewById(R.id.mRightText);
        mLeftText.setOnClickListener(this);
        mRightText.setOnClickListener(this);
        mTitleText.setOnClickListener(this);
        // 载入子VIew
        addView(mImmersiveView);
        addView(mTitleLayoutView);
    }

    /**
     * 日期: 2017/1/10 14:28
     * 描述: 按钮点击事件
     */
    @Override
    public void onClick(View v) {
        try {
            // 获取View ID
            int id = v.getId();
            // 根据ID处理不同的回调事件
            if (id == R.id.mLeftText) { // 左侧按钮事件
                // 执行点击事件回调
                if (mLeftListener != null) {
                    mLeftListener.onLeftClick(v);
                }
                // 执行动画
                if (isOpenAnim) {
                    if (!TextUtils.isEmpty(mLeftText.getText().toString().trim())) {
                        ArthurNavigationHelper.updateTextSize(mLeftText, inactiveTextSize, activeTextSize);
                        ArthurNavigationHelper.updateTextSize(mLeftText, activeTextSize, inactiveTextSize);
                    } else {
                        ArthurNavigationHelper.updateAlpha(mLeftText, 1, 0.60f);
                        ArthurNavigationHelper.updateAlpha(mLeftText, 0.60f, 1);
                    }
                }
            } else if (id == R.id.mRightText) { // 右侧按钮事件
                // 执行点击事件回调
                if (mRightListener != null) {
                    mRightListener.onRightClick(v);
                }
                // 执行动画
                if (isOpenAnim) {
                    if (!TextUtils.isEmpty(mRightText.getText().toString().trim())) {
                        ArthurNavigationHelper.updateTextSize(mRightText, inactiveTextSize, activeTextSize);
                        ArthurNavigationHelper.updateTextSize(mRightText, activeTextSize, inactiveTextSize);
                    } else {
                        ArthurNavigationHelper.updateAlpha(mRightText, 1, 0.60f);
                        ArthurNavigationHelper.updateAlpha(mRightText, 0.60f, 1);
                    }
                }
            } else if (id == R.id.mTitleText) {
                if (mTitleListener != null) {
                    mTitleListener.onTitleClick(v);
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "button == null !!! 按钮执行动画异常");
        }
    }

    /* ===================================================================
     *                              公开方法
     * ===================================================================
     */

    // 标题 ------------------------------------------------------------

    /**
     * 时间：15:13
     * 描述: 设置标题文本
     */
    public void setTitleText(String text) { // 文字
        if (null == mTitleText || null == text) return;
        mTitleText.setText(text);
    }

    /**
     * 时间：15:13
     * 描述: 设置标题文本颜色
     */
    public void setTitleTextColor(int color) { // 颜色
        if (null == mTitleText) return;
        mTitleText.setTextColor(color);
    }

    /**
     * 时间：15:13
     * 描述: 设置标题文本大小
     */
    public void setTitleTextSize(int size) { // 字体大小
        if (null == mTitleText) return;
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 时间：15:13
     * 描述: 设置标题图标
     */
    public void setTitleImage(int image) { // 图标
        if (null == mTitleText) return;
        mTitleText.setText("");
        Drawable drawable = ContextCompat.getDrawable(mContext, image);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTitleText.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 时间：15:20
     * 描述: 获取标题控件
     */
    public TextView getTitleView() { // 获取标题View
        return this.mTitleText;
    }

    // 左侧按钮 ------------------------------------------------------------

    /**
     * 时间：15:20
     * 描述: 设置左侧按钮文本
     */
    public void setLeftText(String text) {  // 文字
        if (null == mLeftText || null == text) return;
        mLeftText.setText(text);
    }

    /**
     * 时间：15:20
     * 描述: 设置左侧按钮文本颜色
     */
    public void setLeftTextColor(int color) { // 颜色
        if (null == mLeftText) return;
        mLeftText.setTextColor(color);
    }

    /**
     * 时间：15:20
     * 描述: 设置左侧按钮文本大小
     */
    public void setLeftTextSize(int size) { // 字体大小
        if (null == mLeftText) return;
        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 时间：15:20
     * 描述: 设置左侧按钮图标
     */
    public void setLeftImage(int image) { // 图标
        if (null == mLeftText) return;
        mLeftText.setText("");
        Drawable drawable = ContextCompat.getDrawable(mContext, image);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mLeftText.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 时间：15:20
     * 描述: 获取左侧按钮
     */
    public TextView getLeftView() { // 获取左侧View
        return this.mLeftText;
    }

    // 右侧按钮 ------------------------------------------------------------

    /**
     * 时间：15:20
     * 描述: 设置右侧按钮文字
     */
    public void setRightText(String text) { // 文字
        if (null == mRightText || null == text) return;
        mRightText.setText(text);
    }

    /**
     * 时间：15:20
     * 描述: 设置右侧按钮文字颜色
     */
    public void setRightTextColor(int color) { // 颜色
        if (null == mRightText) return;
        mRightText.setTextColor(color);

    }

    /**
     * 时间：15:20
     * 描述: 设置右侧按钮文字大小
     */
    public void setRightTextSize(int size) { // 字体大小
        if (null == mRightText) return;
        mRightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * 时间：15:20
     * 描述: 设置右侧按钮图标
     */
    public void setRightImage(int image) { // 图标
        if (null == mRightText) return;
        mRightText.setText("");
        Drawable drawable = ContextCompat.getDrawable(mContext, image);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mRightText.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 时间：15:20
     * 描述: 获取右侧按钮
     */
    public TextView getRightView() { // 获取右侧View
        return this.mRightText;
    }

    /**
     * 日期: 2017/3/17 9:13
     * 描述: 是否打开沉浸式标题栏
     */
    public void setImmersive(Activity activity, boolean isOpen) {
        // 设置透明
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 显示
            mImmersiveView.setVisibility(VISIBLE);
            // 设置背景色
            if(!isOpen) {
                mImmersiveView.setBackgroundColor(Color.parseColor("#333333"));
            }
        }*/

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            // 显示
            mImmersiveView.setVisibility(VISIBLE);
            // 设置背景色
            if (!isOpen) {
                mImmersiveView.setBackgroundColor(Color.parseColor("#333333"));
            }
        } else {
            // 隐藏
            mImmersiveView.setVisibility(GONE);
        }
    }

    /**
     * 日期: 2017/3/17 9:45
     * 描述: 重写setBackgroundColor设置状态栏颜色
     */
    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        mImmersiveView.setBackgroundColor(color);
    }


    /**
     * 时间：17:03
     * 描述: 设置禁止点击
     */
    public void setBtnEnabled(boolean isLeft, boolean isRight) {
        if (null != mLeftText) mLeftText.setEnabled(isLeft);
        if (null != mRightText) mRightText.setEnabled(isRight);
    }

    public void setBtnEnabled(boolean isEnabled) {
        if (null != mLeftText) mLeftText.setEnabled(isEnabled);
        if (null != mRightText) mRightText.setEnabled(isEnabled);
    }

    /**
     * 时间：18:52
     * 描述: 设置显示与隐藏
     */
    public void setBtnVisibility(boolean isLeft, boolean isRight) {
        if (null != mLeftText) mLeftText.setVisibility(isLeft ? VISIBLE : GONE);
        if (null != mRightText) mRightText.setVisibility(isRight ? VISIBLE : GONE);
    }

    public void setBtnVisibility(boolean isVisibility) {
        if (null != mLeftText) mLeftText.setVisibility(isVisibility ? VISIBLE : GONE);
        if (null != mRightText) mRightText.setVisibility(isVisibility ? VISIBLE : GONE);
    }

    /**
     * 日期: 2017/2/16 17:37
     * 描述: 是否开启动画
     */
    public void openAnim(boolean open) {
        this.isOpenAnim = open;
    }





    /* ===================================================================
     *                             回调接口
     * ===================================================================
     */

    /**
     * 时间：15:07
     * 描述: 左侧按钮点击事件回调接口
     */
    public interface OnLeftClickListener {
        void onLeftClick(View v);
    }

    /**
     *  描述：中间标题的点击事件回调接口
     *
     */
    public interface OnTitleClickListener{
        void onTitleClick(View v);
    }

    /**
     * 时间：15:07
     * 描述: 右侧按钮点击事件回调接口
     */
    public interface OnRightClickListener {
        void onRightClick(View v);
    }

    /**
     * 时间：15:12
     * 描述: 设置左侧接口
     */
    public void setOnLeftClickListener(OnLeftClickListener listener) {
        this.mLeftListener = listener;
    }

    public void setOnTitleClickListener(OnTitleClickListener listener) {
        this.mTitleListener = listener;
    }

    /**
     * 时间：15:12
     * 描述: 设置右侧接口
     */
    public void setOnRightClickListener(OnRightClickListener listener) {
        this.mRightListener = listener;
    }
}
