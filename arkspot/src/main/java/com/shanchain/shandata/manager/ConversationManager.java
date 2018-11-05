//package com.shanchain.shandata.manager;
//
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.hyphenate.chat.EMClient;
//import com.hyphenate.chat.EMConversation;
//import com.hyphenate.chat.EMMessage;
//import com.shanchain.data.common.cache.SCCacheUtils;
//import com.shanchain.data.common.net.HttpApi;
//import com.shanchain.data.common.net.NetErrCode;
//import com.shanchain.data.common.net.SCHttpStringCallBack;
//import com.shanchain.data.common.net.SCHttpUtils;
//import com.shanchain.data.common.utils.LogUtils;
//import com.shanchain.data.common.utils.SCJsonUtils;
//import com.shanchain.shandata.ui.model.CharacterConversationBean;
//import com.shanchain.shandata.ui.model.ConversationInfo;
//import com.shanchain.shandata.ui.model.GroupConversationBean;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.Call;
//
///**
// * Created by zhoujian on 2017/12/6.
// */
//
//public class ConversationManager {
//
//    private static ConversationManager instance;
//    private static List<GroupConversationBean> groupBeanList;
//    private static List<CharacterConversationBean> characterBeanList;
//
//    public synchronized static ConversationManager getInstance() {
//        if (null == instance) {
//            instance = new ConversationManager();
//        }
//        return instance;
//    }
//
//
//
//    /**
//     * 描述：缓存会话信息
//     *
//     * @param key   存缓存的key值  一般为当前登录的环信id
//     * @param value 缓存的value值  一般为当前会话列表信息的json集合
//     */
//    public void setConversationCache(String key, String value) {
//        String cacheUserId = SCCacheUtils.getCacheUserId();
//        SCCacheUtils.setCache(cacheUserId, "hx_" + key, value);
//
//        LogUtils.i("缓存了会话信息 = " + SCCacheUtils.getCache(cacheUserId,"hx_" + key));
//
//    }
//
//    /**
//     *  描述：判断缓存中是否有当前环信账号信息
//     *  @param hxUser 待判断的环信用户账号
//     */
//    public boolean hasHxUser(String hxUser){
//        boolean has = false;
//        List<ConversationInfo> conversationCache = getConversationCache(EMClient.getInstance().getCurrentUser());
//        if (conversationCache == null){
//                has =false;
//        }else {
//            for (int i = 0; i < conversationCache.size(); i ++) {
//                if (TextUtils.equals(conversationCache.get(i).getHxUser(),hxUser)){
//                    has = true;
//                }
//            }
//        }
//
//        return has;
//    }
//
//    public void insertConversationCache(ConversationInfo info){
//        List<ConversationInfo> conversationCache = getConversationCache(EMClient.getInstance().getCurrentUser());
//        if (conversationCache == null){
//            conversationCache = new ArrayList<>();
//        }
//        conversationCache.add(info);
//        String conversationJson = JSONObject.toJSONString(conversationCache);
//        setConversationCache(EMClient.getInstance().getCurrentUser(),conversationJson);
//    }
//
//    /**
//     * 描述：取出缓存的会话信息
//     *
//     * @param key 取缓存的key值     一般为当前登录的环信id
//     */
//    public List<ConversationInfo> getConversationCache(String key) {
//        try {
//            List<ConversationInfo> conversationInfos;
//            String cacheUserId = SCCacheUtils.getCacheUserId();
//            String conversationJson = SCCacheUtils.getCache(cacheUserId, "hx_" + key);
//            conversationInfos = JSONObject.parseArray(conversationJson, ConversationInfo.class);
//            return conversationInfos;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private boolean hxGroupSuc;
//    private boolean hxUserSuc;
//
//    public void obtainConversationInfoFromServer() {
//
//        EMClient.getInstance().chatManager().loadAllConversations();
//
//        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
//
//        List<EMConversation> conversations = new ArrayList<>();
//        conversations.addAll(allConversations.values());
//
//        List<String> hxUser = new ArrayList<>();
//        List<String> hxGroup = new ArrayList<>();
//
//        for (int i = 0; i < conversations.size(); i++) {
//            EMConversation emConversation = conversations.get(i);
//            String userName = "";
//            EMMessage.ChatType chatType = emConversation.getLastMessage().getChatType();
//            if (chatType == EMMessage.ChatType.GroupChat){
//                userName = emConversation.getLastMessage().getTo();
//            }else {
//                userName =  emConversation.getLastMessage().getUserName();
//            }
//
//
//            boolean isGroup = emConversation.isGroup();
//            if (isGroup) {
//                hxGroup.add(userName);
//            } else {
//                hxUser.add(userName);
//            }
//        }
//            if (hxGroup.size() > 0) {
//                SCHttpUtils.post()
//                        .url(HttpApi.HX_GROUP_LIST)
//                        .addParams("jArray", JSONObject.toJSONString(hxGroup))
//                        .build()
//                        .execute(new SCHttpStringCallBack() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                LogUtils.e("获取环信群信息失败");
//                                e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                try {
//                                    LogUtils.i("获取到环信群信息 = " + response);
//                                    String code = SCJsonUtils.parseCode(response);
//                                    if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
//                                        String data = SCJsonUtils.parseData(response);
//                                        groupBeanList = JSONObject.parseArray(data, GroupConversationBean.class);
//                                        hxGroupSuc = true;
//                                        if (hxUserSuc){
//                                            saveCache();
//                                        }
//                                    }else{
//
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            } else {
//                hxGroupSuc = true;
//            }
//
//            if (hxUser.size() > 0) {
//                SCHttpUtils.post()
//                        .url(HttpApi.HX_USER_USERNAME_LIST)
//                        .addParams("jArray", JSONObject.toJSONString(hxUser))
//                        .build()
//                        .execute(new SCHttpStringCallBack() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                LogUtils.e("获取环信用户信息失败");
//                                e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                try {
//                                    LogUtils.i("获取到环信用户信息 = " + response);
//                                    String code = SCJsonUtils.parseCode(response);
//                                    if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)){
//                                        String data = SCJsonUtils.parseData(response);
//                                        characterBeanList = JSONObject.parseArray(data, CharacterConversationBean.class);
//                                        hxUserSuc = true;
//                                        if (hxGroupSuc){
//                                            saveCache();
//                                        }
//                                    }else{
//
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//
//                                }
//                            }
//                        });
//            } else {
//                hxUserSuc = true;
//            }
//
//
//
//
//    }
//
//    private void saveCache() {
//        List<ConversationInfo> conversationInfos = new ArrayList<>();
//
//        if (groupBeanList != null && groupBeanList.size()>0){
//            for (int i = 0; i < groupBeanList.size(); i ++) {
//                ConversationInfo info = new ConversationInfo();
//                GroupConversationBean groupConversationBean = groupBeanList.get(i);
//                info.setGroup(true);
//                info.setHxUser(groupConversationBean.getGroupId());
//                info.setHeadImg(groupConversationBean.getIconUrl());
//                info.setName(groupConversationBean.getGroupName());
//                conversationInfos.add(info);
//            }
//        }
//
//        if (characterBeanList != null && characterBeanList.size()>0){
//            for (int i = 0; i < characterBeanList.size(); i ++) {
//                ConversationInfo info = new ConversationInfo();
//                CharacterConversationBean characterConversationBean = characterBeanList.get(i);
//                info.setGroup(false);
//                info.setHxUser(characterConversationBean.getHxUserName());
//                info.setName(characterConversationBean.getName());
//                info.setHeadImg(characterConversationBean.getHeadImg());
//                conversationInfos.add(info);
//            }
//        }
//
//
//        String conversionJson = JSONObject.toJSONString(conversationInfos);
//
//        setConversationCache(EMClient.getInstance().getCurrentUser(),conversionJson);
//    }
//
//}
