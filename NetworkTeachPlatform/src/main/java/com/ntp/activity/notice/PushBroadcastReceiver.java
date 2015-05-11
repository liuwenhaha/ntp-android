package com.ntp.activity.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.activity.MainActivity;
import com.ntp.activity.PathConstant;
import com.ntp.dao.PreferenceDao;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 接受透传消息
 */
public class PushBroadcastReceiver extends BroadcastReceiver {

    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();

    private static final String TAG = "PushBroadcastReceiver";
    private static final String HOMEWORK="homework";//推送消息类型为作业消息
    private static final String COMMENT="comment";//推送消息类型为回帖消息

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GeTuiSdk", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskId = bundle.getString("taskid");
                String messageId = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskId, messageId, 90001);
                Log.d(TAG,"第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Log.d("GeTuiSdk", "Got Payload:" + data);
                    PreferenceDao.setNoticeRed(context, true);

                    if (MainActivity.noticeRed!=null){
                        MainActivity.noticeRed.setVisibility(View.VISIBLE);
                    }else {
                        Log.d(TAG,"MainActivity.noticeRed is null");
                    }
                    if (data.equals(HOMEWORK)){//作业消息
                       PreferenceDao.setHomeworkRed(context,true);
                        if (NoticeFragment.homeworkRed!=null){
                            NoticeFragment.homeworkRed.setVisibility(View.VISIBLE);
                        }
                    }else if (data.equals(COMMENT)){//回帖消息
                        PreferenceDao.setCommentRed(context,true);
                        if (NoticeFragment.commentRed!=null){
                            NoticeFragment.commentRed.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
                // 有些情况下ClientID可能会发生变化，为保证获取最新的ClientID，请应用程序在每次获取ClientID广播后，都能进行一次关联绑定
                String cid = bundle.getString("clientid");
                Log.d(TAG, "clientId=" + cid);
                String name= PreferenceDao.getLoadName(context);
                Log.d(TAG,"name="+name);
                //用户没有登录，取消上传
                if (name.trim().equals("")){
                    break;
                }
                RequestParams params=new RequestParams();
                params.put("CID",cid);
                params.put("username",name);
                //上传用户名和clientID
                asyncHttpClient.post(PathConstant.PATH_UID_CID,params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i(TAG,response.toString());
                    }
                });
                break;
            default:
                break;
        }
    }
}
