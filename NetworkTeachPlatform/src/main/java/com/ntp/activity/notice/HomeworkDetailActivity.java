package com.ntp.activity.notice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.activity.R;
import com.ntp.util.PathConstant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作业消息详情
 *
 * @author yanxing
 */
public class HomeworkDetailActivity extends Activity {

    private TextView homeworkTitle, readOver, score;

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static final String TAG = "HomeworkDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        homeworkTitle = (TextView) findViewById(R.id.homeworkTitle);
        readOver = (TextView) findViewById(R.id.readOver);
        score = (TextView) findViewById(R.id.score);
        String scoreId = getIntent().getStringExtra("scoreId");
        Log.d(TAG, scoreId);
        loadData(scoreId);

    }

    /**
     * 获取数据
     *
     * @param scoreId
     */
    public void loadData(String scoreId) {
        RequestParams params = new RequestParams();
        params.put("id", scoreId);
        asyncHttpClient.post(PathConstant.PATH_COURSE_SCORE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String scoreStr = response.getJSONObject("score").getString("score");
                        String suggest = response.getJSONObject("score").getString("suggest");
                        if (!scoreStr.equals("null")) {
                            score.setText("成绩：" + scoreStr+"’");

                        }
                        if (!suggest.equals("null")) {
                            readOver.setText("批语：" + suggest);
                        }
                        if (scoreStr.equals("null") && suggest.equals("null")) {
                            homeworkTitle.setText("该作业尚未批改，没有成绩和批语");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
