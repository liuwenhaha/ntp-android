package com.chzu.ntp.ui;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.chzu.ntp.dao.CourseDao;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private CourseDao courseDao=new CourseDao(getContext());
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * 清空缓存
     */
    public void testDeleteAllCourse(){
          courseDao.delete();
    }

}