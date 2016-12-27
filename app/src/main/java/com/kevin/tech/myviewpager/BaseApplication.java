package com.kevin.tech.myviewpager;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
public class BaseApplication extends Application {

	private static Context mContext;
	private static Handler mHandler;
	private static long mMainThreadId;
	private static Thread mMainThread;

	public static Context getContext() {
		return mContext;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static long getMainThreadId() {
		return mMainThreadId;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	@Override
	public void onCreate() {// 程序入口方法
		// 创建一些常见的变量

		// 1.上下文
		mContext = getApplicationContext();

		// 2.创建一个handler
		mHandler = new Handler();

		// 3.得到一个主线程id
		mMainThreadId = android.os.Process.myTid();

		// 4.得到主线程
		mMainThread = Thread.currentThread();

//		 uncaughtException();

		super.onCreate();
	}

	private void uncaughtException() {
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
//				System.out.println("异常捕获");

				// 获取的手机的型号，配置
				// 动态获取手机的配置
				StringWriter out = new StringWriter();
				Field[] declaredFields = Build.class.getDeclaredFields();
				for (Field field : declaredFields) {
					try {

						// 写入手机配置信息
						out.write(field.getName() + ":" + field.get(null));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				PrintWriter pw = new PrintWriter(out);
				ex.printStackTrace(pw);
				try {

					File dir = new File("/sdcard/bamy");
					if (!dir.exists()) {
						dir.mkdir();
					}
					FileOutputStream fos = new FileOutputStream(new File("/sdcard/bamy/error.txt"), true);
					fos.write(out.toString().getBytes());
//					LogUtils.i(out.toString());
					fos.close();
				} catch (Exception e) {

					e.printStackTrace();
				}

				// 复活
//				PackageManager pm = getPackageManager();
//				Intent launchIntentForPackage = pm.getLaunchIntentForPackage(getPackageName());
//				startActivity(launchIntentForPackage);

				// 杀死
//				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}

}
