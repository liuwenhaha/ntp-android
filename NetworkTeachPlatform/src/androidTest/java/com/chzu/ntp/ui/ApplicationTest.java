package com.chzu.ntp.ui;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.chzu.ntp.dao.CourseDao;
import com.chzu.ntp.util.SDCardUtil;

import java.io.File;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void testSDDir(){
        File file= SDCardUtil.creatSDDir("ntp");
    }




}