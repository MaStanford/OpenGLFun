package com.example.admin.openglpath.loopers;

import com.example.admin.openglpath.data.DataHolder;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public class FlingLoop {

    /**
     * We need to spawn a couple threads and then hand tasks to them from here.
     */
    public static void doRun(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(DataHolder.getInstance().getFlingQueue().peek() != null) {
                        DataHolder.getInstance().getFlingQueue().poll().run();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
