package com.ntp.activity.course;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ntp.activity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 某个帖子详情界面
 * @author yanxing
 */
public class CourseForumReplyActivity extends Activity {

    private TextView forum;
    private ListView replyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_forum_reply);
        forum= (TextView) findViewById(R.id.forum);
        forum.setText("C语言难学吗？");
        replyList= (ListView) findViewById(R.id.replyList);
        String str[] = new String[]{};
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), getData(),
                R.layout.listview_item_courseforum_reply, new String[]{"content", "username", "time"},
                new int[]{R.id.content, R.id.username, R.id.time});
        replyList.setAdapter(adapter);

    }

    /**
     * 回复
     */
    public void reply(View view) {


    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 1; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("content", "不难");
            map.put("username", "time");
            map.put("time", "2015-05-01");
            list.add(map);
        }
        return list;
    }

}
