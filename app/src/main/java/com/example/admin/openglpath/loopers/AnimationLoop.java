package com.example.admin.openglpath.loopers;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.shapes.Drawable;
import com.example.admin.openglpath.util.Constants;

import java.util.Map;

/**
 * Created by Mark Stanford on 11/20/14.
 */
public class AnimationLoop {

    /**
     * TODO: We can have multiple threads here if we need to.
     */
    public static void doRun(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<Drawable, Animation> mAnimationMap = DataHolder.getInstance().getAnimationMap();
                while(true){
                    //Iterate through the animations.
                    Animation[] animations = new Animation[mAnimationMap.values().size()];
                    mAnimationMap.values().toArray(animations);
                    for(int i = 0; i < animations.length; i++){
                        Animation animation = animations[i];
                        //Do next animation frame and then check if it returned false
                        if(!animation.doNext()){
                            //Remove the finished animation
                            DataHolder.getInstance().getAnimationMap().remove(animation.getDrawable());
                        }
                    }

                    try {
                        Thread.sleep(Constants.WAIT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
