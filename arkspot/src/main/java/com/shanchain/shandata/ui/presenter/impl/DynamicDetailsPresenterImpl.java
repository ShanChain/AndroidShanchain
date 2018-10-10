package com.shanchain.shandata.ui.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.net.SCHttpStringCallBack;
import com.shanchain.data.common.utils.SCJsonUtils;
import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.ui.model.BdCommentBean;
import com.shanchain.shandata.ui.model.CommentBean;
import com.shanchain.shandata.ui.model.CommentData;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.ResponseCommentInfo;
import com.shanchain.shandata.ui.model.ResponseContactInfo;
import com.shanchain.shandata.ui.presenter.DynamicDetailsPresenter;
import com.shanchain.shandata.ui.view.activity.story.stroyView.DynamicDetailView;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by zhoujian on 2017/11/14.
 */

public class DynamicDetailsPresenterImpl implements DynamicDetailsPresenter {
    private DynamicDetailView mView;

    public DynamicDetailsPresenterImpl(DynamicDetailView view) {
        mView = view;
    }

    @Override
    public void initData(int page, int size, String storyId) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.COMMENT_QUERY)
                .addParams("storyId", storyId)
                .addParams("page", page + "")
                .addParams("size", size + "")
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取评论列表失败");
                        e.printStackTrace();
                        error(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取评论列表成功 = " + response);
                            ResponseCommentInfo responseCommentInfo = parseObject(response, ResponseCommentInfo.class);
                            if (TextUtils.equals(responseCommentInfo.getCode(), NetErrCode.COMMON_SUC_CODE)) {
                                boolean isLast = responseCommentInfo.getData().isLast();
                                CommentData data = responseCommentInfo.getData();
                                List<CommentBean> commentBeanList = data.getContent();
                                List<Integer> characterIds = new ArrayList<>();
                                LogUtils.i("评论数量 = " + commentBeanList.size());
                                List<BdCommentBean> bdCommentBeanList = new ArrayList<>();
                                for (int i = 0; i < commentBeanList.size(); i++) {
                                    BdCommentBean bdCommentBean = new BdCommentBean();
                                    CommentBean commentBean = commentBeanList.get(i);
                                    int characterId = commentBean.getCharacterId();
                                    characterIds.add(characterId);
                                    bdCommentBean.setCharacterId(characterId);
                                    bdCommentBean.setCommentBean(commentBean);
                                    bdCommentBeanList.add(bdCommentBean);
                                }
                                obtainCharacterInfos(bdCommentBeanList, characterIds, isLast);
                            } else {
                                error(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            error(false);
                        }

                    }
                });
    }

    @Override
    public void addComment(String comment, String storyId) {
        String dataString = "{\"content\": \"" + comment + "\",\"isAnon\":0}";
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_COMMENT_ADD)
                .addParams("dataString", dataString)
                .addParams("storyId", storyId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("添加评论失败");
                        e.printStackTrace();
                        mView.addSuccess(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("添加评论成功 = " + response);
                        String code = parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            mView.addSuccess(true);
                        } else {
                            mView.addSuccess(false);
                        }
                    }
                });
    }

    /*
    * 删除评论
    *
    * */
    @Override
    public void deleteComment(String commentId, final int position) {
        SCHttpUtils.post()
                .addParams("commentId", commentId)
                .url(HttpApi.DELETE_MINE_COMMENT)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("删除失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String code = JSONObject.parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            mView.deleteSuccess(true,position);
                        } else {
                            mView.deleteSuccess(false,position);
                        }



                    }
                });
    }

    @Override
    public void support(String storyId) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_SUPPORT_ADD)
                .addParams("storyId", storyId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("点赞失败");
                        e.printStackTrace();
                        mView.supportSuc(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("点赞结果 = " + response);
                        String code = parseObject(response).getString("code");

                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            mView.supportSuc(true);
                        } else {
                            mView.supportSuc(false);
                        }
                    }
                });
    }

    @Override
    public void supportCancel(String storyId) {
        SCHttpUtils.postWithUidAndCharId()
                .url(HttpApi.STORY_SUPPORT_CANCEL)
                .addParams("storyId", storyId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("取消点赞失败");
                        e.printStackTrace();
                        mView.supportCancelSuc(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("取消点赞成功 = " + response);
                        String code = parseObject(response).getString("code");
                        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            mView.supportCancelSuc(true);
                        } else {
                            mView.supportCancelSuc(false);
                        }
                    }
                });
    }


    private void obtainCharacterInfos(final List<BdCommentBean> commentBeanList, List<Integer> characterIds, final boolean isLast) {

        String jArr = JSON.toJSONString(characterIds);
        SCHttpUtils.post()
                .url(HttpApi.CHARACTER_BRIEF)
                .addParams("dataArray", jArr)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取角色信息失败");
                        e.printStackTrace();
                        error(isLast);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取角色信息 = " + response);
                        try {
                            ResponseContactInfo responseContactInfo = parseObject(response, ResponseContactInfo.class);
                            String code = responseContactInfo.getCode();
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                List<ContactBean> data = responseContactInfo.getData();
                                for (int i = 0; i < commentBeanList.size(); i++) {
                                    BdCommentBean bdCommentBean = commentBeanList.get(i);

                                    for (ContactBean contactBean : data) {
                                        if (bdCommentBean.getCharacterId() == contactBean.getCharacterId()) {
                                            bdCommentBean.setContactBean(contactBean);
                                        }
                                    }
                                }
                                mView.commentSuccess(commentBeanList, isLast);
                            } else {
                                error(isLast);
                            }
                        } catch (Exception e) {
                            LogUtils.i("获取角色信息失败");
                            e.printStackTrace();
                            error(isLast);
                        }
                    }
                });

    }

    @Override
    public void initNovelInfo(String storyId) {
        SCHttpUtils.post()
                .url(HttpApi.STORY_GET_BY_ID)
                .addParams("storyId", storyId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("获取故事失败");
                        e.printStackTrace();
                        mView.initNovelSuc(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("获取到故事内容 = " + response);
                            String code = JSONObject.parseObject(response).getString("code");
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                String data = JSONObject.parseObject(response).getString("data");
                                StoryDetailInfo storyDetailInfo = JSONObject.parseObject(data, StoryDetailInfo.class);
                                if (storyDetailInfo != null) {
                                    mView.initNovelSuc(storyDetailInfo);
                                } else {
                                    mView.initNovelSuc(null);
                                }
                            } else {
                                mView.initNovelSuc(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.initNovelSuc(null);
                        }
                    }
                });
    }

    @Override
    public void supportCancelComment(int commentId, final int position) {
        SCHttpUtils.postWithUidAndCharId()
                .url(HttpApi.STORY_COMMENT_SUPPORT_CANCEL)
                .addParams("commentId", "" + commentId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("取消点赞点赞失败");
                        e.printStackTrace();
                        mView.commentSupportCancelSuc(false, position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("取消点赞评论结果 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mView.commentSupportCancelSuc(true, position);
                            } else {
                                mView.commentSupportCancelSuc(false, position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.commentSupportCancelSuc(false, position);
                        }
                    }
                });
    }

    @Override
    public void supportComment(int commentId, final int position) {
        SCHttpUtils.postWithChaId()
                .url(HttpApi.STORY_COMMENT_SUPPORT)
                .addParams("commentId", "" + commentId)
                .build()
                .execute(new SCHttpStringCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("点赞失败");
                        e.printStackTrace();
                        mView.commentSupportSuc(false, position);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtils.i("点赞评论结果 = " + response);
                            String code = SCJsonUtils.parseCode(response);
                            if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                                mView.commentSupportSuc(true, position);
                            } else {
                                mView.commentSupportSuc(false, position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mView.commentSupportSuc(false, position);
                        }
                    }
                });
    }

    private void error(boolean isLast) {
        mView.commentSuccess(null, isLast);
    }


}
