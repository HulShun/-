package com.example.newsclient;

import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.dao.NewsDao;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    public void testDB() {
        List<NewsBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NewsBean bean = new NewsBean();
            bean.setTitle("1111");
            bean.setAbstractX("11111");
            bean.setDatetime("111111");
            bean.setImg_url("111");
            bean.setImg_url("1111");
            list.add(bean);
        }

        NewsDao dao = NewsDao.getInstance();
        //dao.insert(list);
    }
}