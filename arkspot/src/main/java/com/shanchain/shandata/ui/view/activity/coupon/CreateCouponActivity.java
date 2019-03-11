package com.shanchain.shandata.ui.view.activity.coupon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.EventBusObject;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpPostBodyCallBack;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.ui.widgets.CustomDialog;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.ui.widgets.timepicker.SCTimePickerView;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.SCUploadImgHelper;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.event.EventMessage;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.widgets.photochoose.PhotoUtils;
import com.shanchain.shandata.widgets.pickerimage.PickImageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.view.CircleImageView;
import okhttp3.Call;

public class CreateCouponActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.tb_coupon)
    ArthurToolBar tbCoupon;
    @Bind(R.id.image_head)
    CircleImageView imageHead;
    @Bind(R.id.tv_edit_head)
    TextView tvEditHead;
    @Bind(R.id.linear_head)
    LinearLayout linearHead;
    @Bind(R.id.tv_coupon_name)
    TextView tvCouponName;
    @Bind(R.id.edit_coupon_name)
    EditText editCouponName;
    @Bind(R.id.tv_coupon_code)
    TextView tvCouponCode;
    @Bind(R.id.edit_coupon_code)
    EditText editCouponCode;
    @Bind(R.id.tv_coupon_des)
    TextView tvCouponDes;
    @Bind(R.id.tv_coupon_price)
    TextView tvCouponPrice;
    @Bind(R.id.edit_coupon_price)
    EditText editCouponPrice;
    @Bind(R.id.tv_coupon_num)
    TextView tvCouponNum;
    @Bind(R.id.edit_coupon_num)
    EditText editCouponNum;
    @Bind(R.id.tv_coupon_expiration)
    TextView tvCouponExpiration;
    @Bind(R.id.edit_coupon_expiration)
    TextView editCouponExpiration;
    @Bind(R.id.tv_coupon_example)
    TextView tvCouponExample;
    @Bind(R.id.tv_coupon_describe)
    TextView tvCouponDescribe;
    @Bind(R.id.edit_coupon_describe)
    EditText editCouponDescribe;
    @Bind(R.id.relative_top)
    RelativeLayout relativeTop;
    @Bind(R.id.tv_coupon_mortgage)
    TextView tvCouponMortgage;
    @Bind(R.id.tv_coupon_mortgage_num)
    TextView tvCouponMortgageNum;
    @Bind(R.id.tv_coupon_currency)
    TextView tvCouponCurrency;
    @Bind(R.id.tv_coupon_currency_num)
    TextView tvCouponCurrencyNum;
    @Bind(R.id.tv_coupon_wallet)
    TextView tvCouponWallet;
    @Bind(R.id.tv_coupon_wallet_balance)
    TextView tvCouponWalletBalance;
    @Bind(R.id.tv_coupon_seat)
    TextView tvCouponSeat;
    @Bind(R.id.tv_coupon_seat_num)
    TextView tvCouponSeatNum;
    @Bind(R.id.button_create_coupon)
    Button buttonCreateCoupon;
    @Bind(R.id.fb_window)
    RelativeLayout relativeWindow;
    @Bind(R.id.relative_bottom)
    RelativeLayout relativeBottom;
    @Bind(R.id.relative_coupon_example)
    RelativeLayout relativeCouponExample;
    @Bind(R.id.tv_name_des)
    TextView tvNameDes;
    @Bind(R.id.tv_price_des)
    TextView tvPriceDes;
    @Bind(R.id.tv_wallet_recharge)
    TextView tvWalletRecharge;

    private boolean isShow = true;
    private SCTimePickerView scTimePickerView;
    private SCTimePickerView.OnTimeSelectListener onTimeSelectListener;

    private String amount, deadline, detail, name, photoUrl, price, roomId, subuserId, tokenSymbol, userId;
    private String rate = "10.0";//汇率
    private long timeStamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_create;
    }

    @Override
    protected void initViewsAndEvents() {
        roomId = getIntent().getStringExtra("roomId");
        // 标题栏
        tbCoupon.setTitleText("创建马甲劵");
        tbCoupon.setOnLeftClickListener(this);
        subuserId = SCCacheUtils.getCacheCharacterId();//角色ID
        userId = SCCacheUtils.getCacheUserId();
        //钱包充值
        tvWalletRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walletIntent = new Intent(CreateCouponActivity.this, com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
                JSONObject obj = new JSONObject();
                obj.put("url", HttpApi.SEAT_WALLET);
                obj.put("title", getResources().getString(R.string.nav_my_wallet));
                String webParams = obj.toJSONString();
                walletIntent.putExtra("webParams", webParams);
                startActivity(walletIntent);
            }
        });
        //加载头像
        String avatar = SCCacheUtils.getCacheHeadImg();
        photoUrl = avatar != null ? SCCacheUtils.getCacheHeadImg() : "";
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.aurora_headicon_default);
        Glide.with(CreateCouponActivity.this).load(avatar).apply(options).into(imageHead);
        //更换头像
        tvEditHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //初始化数据显示钱包余额
        initData();
        //显示示例图片
        tvCouponExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                relativeCouponExample.setVisibility(View.VISIBLE);
                CustomDialog customDialog = new CustomDialog(CreateCouponActivity.this, R.layout.dialog_image_bg, new int[]{R.id.iv_dialog_bg});
                customDialog.show();
            }
        });
        //点击隐藏示例图片
//        relativeCouponExample.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                relativeCouponExample.setVisibility(View.GONE);
//            }
//        });
        //监听名称文本
        editCouponName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String couponName = s.toString();
                if (couponName.length() > 1) {
                    tvNameDes.setVisibility(View.GONE);
                }
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(couponName);
                if (couponName.length() > 16 || matcher.find()) {
                    tvNameDes.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });
        //只能输入大写字母
        editCouponCode.setTransformationMethod(new ReplacementTransformationMethod() {
                                                   @Override
                                                   protected char[] getOriginal() {
                                                       char[] originalCharArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
                                                       return originalCharArr;
                                                   }

                                                   @Override
                                                   protected char[] getReplacement() {
                                                       char[] replacementCharArr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                                                       return replacementCharArr;
                                                   }
                                               }
        );
        editCouponPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    if (!TextUtils.isEmpty(s.toString())) {
                        if (s.toString().getBytes().length > 0) {
                            if (s.toString().substring(0, 1).equals("0")) {
                                editCouponPrice.setText("");
                                ToastUtils.showToast(CreateCouponActivity.this, "单价不能为0");
                                return;
                            }
                            tvPriceDes.setVisibility(View.GONE);
                            price = s.toString();
                            if (TextUtils.isEmpty(amount)) {
                                Integer unitPrice = Integer.valueOf(s.toString());
                                tvCouponCurrencyNum.setText("" + unitPrice * 0.01);
                                tvCouponSeatNum.setText("" + unitPrice / Double.valueOf(rate) * 0.01 + "");
                            } else {
                                int unitPrice = Integer.valueOf(s.toString());
                                int totalCount = TextUtils.isEmpty(amount) ? 0 : Integer.valueOf(amount);
                                tvCouponCurrencyNum.setText("" + unitPrice * totalCount * 0.01);
                                if (TextUtils.isEmpty(rate)) {
                                    Double totalNum = Double.valueOf(unitPrice * totalCount * 0.01);
                                    tvCouponSeatNum.setText(totalNum / 10.0 + "");
                                } else {
                                    Double totalNum = Double.valueOf(unitPrice * totalCount * 0.01);
                                    tvCouponSeatNum.setText(totalNum / Double.valueOf(rate) + "");
                                }
                            }
                        } else {
                            tvCouponCurrencyNum.setText("" + 0);

                        }
                    }
                } catch (NumberFormatException e) {
                    tvPriceDes.setVisibility(View.VISIBLE);
                }
            }
        });
        //监听填写数量的输入框
        editCouponNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.toString().getBytes().length > 0) {
                        if (s.toString().substring(0, 1).equals("0")) {
                            editCouponNum.setText("");
                            ToastUtils.showToast(CreateCouponActivity.this, "数量不能为0");
                            return;
                        }
                        amount = s.toString();
                        if (!TextUtils.isEmpty(price)) {
                            Integer unitPrice = Integer.valueOf(price);
                            Integer totalCount = Integer.valueOf(s.toString());
                            tvCouponCurrencyNum.setText("" + unitPrice * totalCount * 0.01);

                            if (TextUtils.isEmpty(rate)) {
                                Double totalNum = Double.valueOf(unitPrice * totalCount * 0.01);
                                tvCouponSeatNum.setText("" + totalNum / 10.0 + "");
                            } else {
                                Double totalNum = Double.valueOf(unitPrice * totalCount * 0.01);
                                tvCouponSeatNum.setText("" + totalNum / Double.valueOf(rate) + "");
                            }
                        } else {
                            int totalCount = Integer.valueOf(s.toString());
                            tvCouponCurrencyNum.setText("" + totalCount * 0.01);
                            tvCouponSeatNum.setText("" + totalCount / Double.valueOf(rate) * 0.01 + "");
                        }
                    } else {
                        tvCouponCurrencyNum.setText("" + 0.0);
                    }
                } catch (NumberFormatException e) {
//                    ToastUtils.showToast(CreateCouponActivity.this, "请输入合法数字");
                }


            }
        });
        //显示抵押费率说明
        tvCouponMortgageNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StandardDialog standardDialog = new StandardDialog(CreateCouponActivity.this);
                standardDialog.setStandardMsg(getResources().getString(R.string.coupon_show_window));
                standardDialog.setStandardTitle("抵押费");
                standardDialog.show();
//                if (isShow) {
////                    relativeWindow.setVisibility(View.VISIBLE);
//                    isShow = false;
//                } else {
////                    relativeWindow.setVisibility(View.GONE);
//                    isShow = true;
//                }

            }
        });
        //初始化时间选择器
        initPickerView();
        editCouponExpiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scTimePickerView.show(editCouponExpiration);

            }
        });
        //创建马甲劵
        buttonCreateCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editCouponName.getText().toString();//名称
//                tokenSymbol = editCouponCode.getText().toString();//卡劵代码
                price = editCouponPrice.getText().toString();//单价
                amount = editCouponNum.getText().toString();//数量
                detail = editCouponDescribe.getText().toString();//说明
                subuserId = SCCacheUtils.getCacheCharacterId();//角色ID
                userId = SCCacheUtils.getCacheUserId();
                createCoupon();
            }
        });
    }

    private void initData() {
        getUseName();
        CustomDialog showPasswordDialog = new CustomDialog(CreateCouponActivity.this, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
        SCHttpUtils.getAndToken()
                .url(HttpApi.WALLET_INFO)
                .addParams("characterId", subuserId)
                .addParams("userId", userId)
                .build()
                .execute(new SCHttpStringCallBack(CreateCouponActivity.this, showPasswordDialog) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        String msg = JSONObject.parseObject(response).getString("msg");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            final String amount = JSONObject.parseObject(data).getString("amount");
                            final String price = JSONObject.parseObject(data).getString("price");
                            rate = JSONObject.parseObject(data).getString("rate");
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvCouponWalletBalance.setText("￥ " + price);
                                }
                            });

                        }
                    }
                });
    }

    private void createCoupon() {
        if (TextUtils.isEmpty(amount) || Integer.valueOf(amount) < 1 || TextUtils.isEmpty(deadline) || TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || Integer.valueOf(price) < 1) {
            ToastUtils.showToast(CreateCouponActivity.this, "请输入完整信息");
            return;
        }
        detail = TextUtils.isEmpty(detail) ? "empty" : detail;
        Map requestBody = new HashMap();
        requestBody.put("authCode", SCCacheUtils.getCacheAuthCode() + "");
        requestBody.put("deviceToken", registrationId + "");
//        requestBody.put("token", SCCacheUtils.getCacheToken() + "");
        requestBody.put("amount", amount + "");
        requestBody.put("deadline", deadline + "");
        requestBody.put("detail", detail + "");
        requestBody.put("name", name + "");
        requestBody.put("photoUrl", photoUrl + "");
        requestBody.put("price", price + "");
        requestBody.put("roomId", roomId + "");
        requestBody.put("subuserId", subuserId + "");
        requestBody.put("tokenSymbol", "empty");
        requestBody.put("userId", userId + "");
        CustomDialog showPasswordDialog = new CustomDialog(CreateCouponActivity.this, true, 1.0,
                R.layout.dialog_bottom_wallet_password,
                new int[]{R.id.iv_dialog_add_picture, R.id.tv_dialog_sure});
//        showLoadingDialog();
        SCHttpUtils.postByBody(HttpApi.COUPONS_CREATE, JSONObject.toJSONString(requestBody), new SCHttpPostBodyCallBack(CreateCouponActivity.this, showPasswordDialog) {
            @Override
            public void responseDoParse(String string) {
                closeLoadingDialog();
                final String result = string;
                String code = JSONObject.parseObject(result).getString("code");
                final String msg = JSONObject.parseObject(result).getString("msg");
                if (code.equals(NetErrCode.SUC_CODE)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(CreateCouponActivity.this, "" + msg);
                            String data = JSONObject.parseObject(result).getString("data");
                            EventMessage eventMessage = new EventMessage(0);
                            EventBus.getDefault().post(eventMessage);
                            finish();
                        }
                    });
                } else if (NetErrCode.BALANCE_NOT_ENOUGH.equals(code)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(CreateCouponActivity.this, "您的余额不足");
                        }
                    });
                    closeLoadingDialog();
                } else {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(CreateCouponActivity.this, "创建失败");
                        }
                    });
                    closeLoadingDialog();
                }
            }

        });

    }

    //选择头像
    private void selectImage() {
        int from = PickImageActivity.FROM_LOCAL;
        int requestCode = PhotoUtils.INTENT_SELECT;
        if (ContextCompat.checkSelfPermission(CreateCouponActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
//            PickImageActivity.start(MessageListActivity.this, requestCode, from, tempFile(), true, 1,
//                    true, false, 0, 0);
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, requestCode);
        }
    }

    //初始化时间选择弹窗
    private void initPickerView() {
        SCTimePickerView.Builder builder = new SCTimePickerView.Builder(CreateCouponActivity.this);
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        int year = startDate.get(Calendar.YEAR);
        Date date = new Date(System.currentTimeMillis());
        int hour = date.getHours();
//        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE));//设置起始年份
        startDate.set(year, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE) + 1, selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));//设置起始年份
        Calendar endDate = Calendar.getInstance();
        endDate.set(year + 50, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE), selectedDate.get(Calendar.HOUR), selectedDate.get(Calendar.MINUTE));//设置结束年份
        builder.setType(new boolean[]{true, true, true, false, false, false})//设置显示年、月、日、时、分、秒
                .setDecorView((ViewGroup) findViewById(android.R.id.content).getRootView())
//                .setDecorView((ViewGroup) dialog.getWindow().getDecorView().getRootView())
                .isCenterLabel(true)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("清除")
                .setCancelColor(CreateCouponActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .setSubmitText("完成")
                .setRangDate(startDate, endDate)
                .setSubCalSize(18)
                .setTitleSize(18)
                .setContentSize(18)
                .setTitleBgColor(CreateCouponActivity.this.getResources().getColor(com.shanchain.common.R.color.colorWhite))
                .setSubmitColor(CreateCouponActivity.this.getResources().getColor(com.shanchain.common.R.color.colorDialogBtn))
                .isDialog(true)
                .build();

        scTimePickerView = new SCTimePickerView(builder);
        scTimePickerView.setDate(Calendar.getInstance());

        onTimeSelectListener = new SCTimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formatDate = simpleDateFormat.format(date);
                timeStamp = date.getTime();
                deadline = "" + (timeStamp + 24 * 60 * 60 * 1000 - 60 * 1000);
                TextView clickView = (TextView) v;
                clickView.setText(formatDate);
            }
        };
        scTimePickerView.setOnTimeSelectListener(onTimeSelectListener);
        scTimePickerView.setOnCancelClickListener(new SCTimePickerView.OnCancelClickListener() {
            @Override
            public void onCancelClick(View v) {
                scTimePickerView.dismiss();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(Object event) {
        super.onEventMainThread(event);
        try {
            EventBusObject busObject = (EventBusObject) event;
            if (EventConstant.EVENT_RELEASE == busObject.getCode()) {
                if (ActivityStackManager.getInstance().getTopActivity() == CreateCouponActivity.this) {
                    createCoupon();
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            switch (requestCode) {
                case PhotoUtils.INTENT_SELECT:
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String photoPath = cursor.getString(columnIndex);  //获取照片路径
                    cursor.close();
                    final Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    showLoadingDialog(true);
                    SCUploadImgHelper helper = new SCUploadImgHelper();
                    helper.setUploadListener(new SCUploadImgHelper.UploadListener() {
                        @Override
                        public void onUploadSuc(final List<String> urls) {

                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    photoUrl = urls.get(0);
                                    RequestOptions options = new RequestOptions();
                                    options.placeholder(R.mipmap.aurora_headicon_default);
                                    Glide.with(CreateCouponActivity.this).load(photoUrl).apply(options).into(imageHead);
                                    closeLoadingDialog();
                                }
                            });
                        }

                        @Override
                        public void error() {
                            LogUtils.i(TAG, "oss上传失败");
                        }
                    });
                    List list = new ArrayList();
                    list.add(photoPath);
                    helper.upLoadImg(mContext, list);
                    break;
            }
        }
    }

    private void getUseName() {
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_GET_CURRENT)
                .addParams("userId", "" + SCCacheUtils.getCacheUserId())
                .build()
                .execute(new SCHttpStringCallBack(mContext, commonDialog) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "网络异常");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (NetErrCode.COMMON_SUC_CODE.equals(code)) {
                            String data = JSONObject.parseObject(response).getString("data");
                            if (data == null) {
                                return;
                            }
                            String character = JSONObject.parseObject(data).getString("characterInfo");
                            final CharacterInfo characterInfo = JSONObject.parseObject(character, CharacterInfo.class);
                            final String headImg = characterInfo.getHeadImg();
                            ThreadUtils.runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    RequestOptions options = new RequestOptions();
                                    options.placeholder(R.mipmap.aurora_headicon_default);
                                    Glide.with(CreateCouponActivity.this).load(headImg).apply(options).into(imageHead);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
