package com.kevin.tech.myviewpager;


public class DensityUtils {
	/**
	 * dp-->px
	 * 
	 * @param dp
	 * @return
	 */
	public static int dip2Px(int dp) {
		// px/dp = density;
		float density = BaseApplication.getContext().getResources().getDisplayMetrics().density;
		// System.out.println("density:" + density);
		int px = (int) (dp * density + .5f);
		return px;
	}

	/**
	 * px-->dp
	 * 
	 * @param dp
	 * @return
	 */
	public static int px2Dp(int px) {
		// px/dp = density;
		float density = BaseApplication.getContext().getResources().getDisplayMetrics().density;
		// System.out.println("density:" + density);
		int dp = (int) (px / density + .5f);
		return px;
	}
}
