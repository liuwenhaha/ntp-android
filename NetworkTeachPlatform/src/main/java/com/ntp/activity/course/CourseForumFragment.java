package com.ntp.activity.course;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.activity.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程跟帖
 */
public class CourseForumFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static CourseForumFragment mCourseForumFragment;
    private static PullToRefreshListView pullToRefreshListView;

    /**
     * 创建对象
     */
    public static CourseForumFragment getInstance() {
        mCourseForumFragment = new CourseForumFragment();
        return mCourseForumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_forum, container, false);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        String str[] = new String[]{};
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), getData(),
                R.layout.listview_item_courseforum, new String[]{"content", "name", "time", "reply"},
                new int[]{R.id.content, R.id.name, R.id.time, R.id.reply});
        pullToRefreshListView.setOnItemClickListener(this);
        pullToRefreshListView.setAdapter(adapter);
        return view;
    }

    /**
     * 模拟数据
     */
    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 1; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("content", "C语言难学吗?");
            map.put("name", "yanxing");
            map.put("time", "2015-05-01");
            map.put("reply", "1人回复");
            list.add(map);
        }
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity().getApplicationContext(),CourseForumReplyActivity.class));
    }
}
