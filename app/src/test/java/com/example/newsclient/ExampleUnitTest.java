package com.example.newsclient;

import com.example.newsclient.Model.model.MainViewModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }



    @Test
    public void textDB2() {
        new MainViewModel().getVideoTabsFromNet(Configuration.VIDEO_TYPE_URL);
    }
}