package com.ntp.activity.course;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CourseForumFragment extends Fragment {
    private static CourseForumFragment mCourseForumFragment;
    private static PullToRefreshListView pullToRefreshListView;

    /**
     * 创建对象
     */
    public static CourseForumFragment getInstance() {
        if (mCourseForumFragment == null) {
            mCourseForumFragment = new CourseForumFragment();
        }
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
        pullToRefreshListView.setAdapter(adapter);
        return view;
    }

    /**
     * 模拟数据
     */
    private List<Map<String, String>> getData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("content", "创业团队做手机客户端，应该先做 Android 还是 iOS？为什么？");
            map.put("name", "yanxing");
            map.put("time", "2015-02-02");
            map.put("reply", "回复数");
            list.add(map);
        }
        return list;
    }


}
