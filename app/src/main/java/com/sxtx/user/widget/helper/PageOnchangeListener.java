package com.sxtx.user.widget.helper;

/**
 * viewpager 里的页面滑动接口
 * @author 龙少
 *
 */
public interface PageOnchangeListener {
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
	public void onPageSelected(int position);
	public void onPageScrollStateChanged(int state);
}
