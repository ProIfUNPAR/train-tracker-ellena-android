package com.example.user.myapplication;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import static org.junit.Assert.*;

/**
 * Created by GeneralDevil X on 4/25/2018.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp()throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(),getActivity());
    }

    public void testMasukinStasiun(){
        //implementasi untuk automated testing
    }



    public void tearDown() throws Exception{
        try{
            solo.finalize();
        }
        catch(Throwable t){
            t.printStackTrace();
        }
        getActivity().finish();
        super.tearDown();
    }
}