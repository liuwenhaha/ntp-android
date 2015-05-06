package com.ntp.activity.course;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.activity.R;
import com.ntp.dao.PathConstant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 课程讨论区发帖参加讨论
 * @author yanxing
 */
public class CourseForumCommentActivity extends Activity implements View.OnClickListener{

    ImageView back;
    EditText comment;
    private String name,code;
    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_forum_comment);
        back= (ImageView) findViewById(R.id.back);
        comment= (EditText) findViewById(R.id.comment);
        back.setOnClickListener(this);
        name=getIntent().getStringExtra("name");
        code=getIntent().getStringExtra("code");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back://返回
                finish();
                break;
        }
    }

    /**
     * 发评论
     * @param view
     */
    public void comment(View view) {
         String commentStr=comment.getText().toString().trim();
         if (commentStr.equals("")){
             Toast.makeText(getApplicationContext(),"不能为空",Toast.LENGTH_SHORT).show();
             return;
         }
        RequestParams params=new RequestParams();
        params.put("code",code);
        params.put("name",name);
        params.put("comment",commentStr);
        asyncHttpClient.post(PathConstant.PATH_COURSE_FORUM_COMMENT,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    try {
                        String result = response.getString("result");
                        //评论成功
                        if (result.equals("success")){
                            setResult(RESULT_OK);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
