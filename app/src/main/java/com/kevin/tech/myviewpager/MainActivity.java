package com.kevin.tech.myviewpager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kevin.tech.myviewpager.adapter.MyViewPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mIndicator;
    private ViewPager mViewPager;
    private ImageView[] mImageView;
    private int[] mImageArr = new int[]{R.drawable.slideimage1, R.drawable.slideimage2, R.drawable.slideimage3};
    private MyViewPagerAdapter mAdapter;
    private Timer mTimer;
    private static final int UPDATE_VIEWPAGER = 100;
    private int autoCurrIndex;
    private View mSelectedPoint;

//    private ExecutorService mExecutorService = Executors.newCachedThreadPool();//创建一个可缓存线程池
//    private int count = 0;//每次页面跳转时计数+1
//    private final ThreadLocal<Integer> selectcount = new ThreadLocal<Integer>() {
//        @Override
//        protected Integer initialValue() {
//            return 0;
//        }
//    };

    private boolean isLoop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = new Timer();
        mIndicator = (LinearLayout) findViewById(R.id.indicator);
        mSelectedPoint = findViewById(R.id.selected_point);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        mAdapter = new MyViewPagerAdapter(this, mImageArr);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(5000 * (mImageArr.length));
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(MainActivity.this, "Down", Toast.LENGTH_SHORT).show();
                        isLoop = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainActivity.this, "Up", Toast.LENGTH_SHORT).show();
                        isLoop = true;
                        break;
                }
                return false;
            }
        });

//        //viewPager间隔5秒自动滑动
//        final Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                //跳转到下一页
//                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//            }
//        };
//        Field mScroller = null;
//        try {
//            mScroller = ViewPager.class.getDeclaredField("mScroller");
//            mScroller.setAccessible(true);
//            ChangeSpeedScroller scroller = new ChangeSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
//            scroller.setDuration(500);
//            mScroller.set(mViewPager, scroller);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }


        initIndicator();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_VIEWPAGER;
                if(isLoop){//如果isLoop = true 才进行轮播
                handler.sendMessage(message);
                }
            }
        }, 1000, 1000);//这里定义了轮播图切换的间隔时间
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mSelectedPoint.getLayoutParams();
                params.leftMargin = (int)(positionOffset*20+0.5f) + position * 20;
                mSelectedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                Log.i("kevin", position + "");
//                count++;
//                //开启新线程
//                mExecutorService.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        //启动线程时将当前的count赋值给selectcount
//                        selectcount.set(count);
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        //线程沉睡5秒之后如果selectcount==count，即count值未发生改变,即viewpager未发生跳转
//                        if (selectcount.get() == count) {
//                            //线程沉睡期间页面未发生跳转则发送消息，否则不发送此条消息
//                            mHandler.sendEmptyMessage(0);
//                        }
//                    }
//                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        //开启一个线程，用于循环
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isLooper = true;
//                while (isLooper) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //这里是设置当前页的下一页
//                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//                        }
//                    });
//                }
//            }
//        }).start();
    }

    private void initIndicator() {
        mImageView = new ImageView[mImageArr.length];
        for (int i = 0; i < mImageArr.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.indicator_image, null);
            view.findViewById(R.id.indicator_iamge).setBackgroundResource(R.drawable.shape_origin_point_pink);
            mImageView[i] = new ImageView(this);
            if (i == 0) {
                mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_pink);
            } else {
                mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_white);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 0, 0, 0);
                mImageView[i].setLayoutParams(layoutParams);
            }
            mIndicator.addView(mImageView[i]);
        }
    }

    private void setIndicator(int position) {
        position %= mImageArr.length;
        for (int i = 0; i < mImageArr.length; i++) {
            mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_pink);
            if (position != i) {
                mImageView[i].setBackgroundResource(R.drawable.shape_origin_point_white);
            }

        }
    }


    private boolean isLooper;
    private static final int SCROLL_WHAT = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_VIEWPAGER:
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                    break;
            }
        }
    };

}
