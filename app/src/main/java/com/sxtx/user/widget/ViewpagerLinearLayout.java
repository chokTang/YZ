package com.sxtx.user.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sxtx.user.R;
import com.sxtx.user.widget.helper.PageOnchangeListener;

import java.util.List;

public class ViewpagerLinearLayout extends LinearLayout {

	private Context context;

	private Paint paint;
	private Path path;
	private int width, height;// 三角行的宽高
	private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;
	private final int MAX_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGLE_WIDTH);// 三角形的最大宽度
	private int initTranslationX;// 默认的偏移量
	private int translationx;// 偏移量

	private int mTabVisibleCont;
	private static final int COUNT_DEAFULT_TAB = 4;// 默认数量

	private List<String> titles;
	private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;// 字体默认的颜色
	private static final int COLOR_TEXT_SELECT = 0xFFFFFFFF;// 字体默认的颜色
	private static final int COLOR_TRANSLATION_BG=0xFFFFFFFF;//三角形默认的背景颜色

	private ViewPager viewPager;
	private PageOnchangeListener listener;

	public ViewpagerLinearLayout(Context context) {
		this(context, null);
	}

	public ViewpagerLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewpagerLinearLayout(Context context, AttributeSet attrs,
								 int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		inint(attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = (int) (w / mTabVisibleCont * RADIO_TRIANGLE_WIDTH);
		width = Math.min(width, MAX_WIDTH);// 取最小值

		initTranslationX = w / mTabVisibleCont / 2 - width / 2;
		initTriangle();
	}

	/**
	 * 初始化三角形
	 */
	private void initTriangle() {
		height = width / 2;

		path = new Path();
		path.moveTo(0, 0);
		path.lineTo(width, 0);
		path.lineTo(width / 2, -height + 2);
		path.close();

	}

	/**
	 * 初始化变量
	 */
	private void inint(AttributeSet attrs) {
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.viewpageIndicator);
		mTabVisibleCont = array.getInt(
				R.styleable.viewpageIndicator_visible_tab_count,
				COUNT_DEAFULT_TAB);
		if (mTabVisibleCont < 0) {
			mTabVisibleCont = COUNT_DEAFULT_TAB;
		}

		paint = new Paint();
		paint.setAntiAlias(true);
		// 画直线
		// paint.setColor(Color.parseColor("#FF0000"));
		paint.setColor(COLOR_TRANSLATION_BG);
		paint.setStyle(Style.FILL);
		paint.setPathEffect(new CornerPathEffect(2));
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		//画三角
		canvas.translate(initTranslationX + translationx, getHeight());
		canvas.drawPath(path, paint);
		// 画直线
		// canvas.translate(translationx, getHeight());
		// canvas.drawRect(0, 0, getScreenWidth()/mTabVisibleCont, -3, paint);
		canvas.restore();
		super.dispatchDraw(canvas);
	}

	/**
	 * 指示器跟随手指进行滚动
	 *
	 * @param postion
	 * @param offset
	 */
	public void scroll(int postion, float offset) {
		int tabWidth = getWidth() / mTabVisibleCont;
		translationx = (int) (tabWidth * (offset + postion));
		Log.d("+++", postion + "---" + getWidth() + "--"
				+ getChildAt(postion).getWidth());

		// 容器进行移动 当tab处于移动最后一个的时候
		if (postion!=(titles.size() - 2)&&postion >= (mTabVisibleCont - 2) && offset > 0
				&& getChildCount() > mTabVisibleCont) {
			if (mTabVisibleCont != 1) {
				this.scrollTo((postion - (mTabVisibleCont - 2)) * tabWidth
						+ (int) (tabWidth * offset), 0);
			} else {
				this.scrollTo(postion * tabWidth + (int) (tabWidth * offset), 0);
			}
		}

		invalidate();
	}

	/**
	 * 当xml加载完成之后
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int count = getChildCount();
		if (count == 0) {
			return;
		}
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			LayoutParams params = (LayoutParams) view
					.getLayoutParams();
			params.weight = 0;
			params.width = getScreenWidth() / mTabVisibleCont;
			view.setLayoutParams(params);
		}
		setItemClickEvent();
	}

	/**
	 * 获取屏幕的宽度
	 * @return
	 */
	private int getScreenWidth() {
		if (context == null) {
			context = getContext();
		}
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 动态添加
	 *
	 * @param titles
	 */
	public void setTabItemTitles(List<String> titles) {
		if (titles != null && titles.size() > 0) {
			this.removeAllViews();
			this.titles = titles;
			for (String title : this.titles) {
				addView(getItemView(title));
			}
			setItemClickEvent();
		}
	}

	/**
	 * 根据title 创建Tab
	 *
	 * @param title
	 * @return
	 */
	private View getItemView(String title) {
		TextView tv = new TextView(getContext());
		LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.width = getScreenWidth() / mTabVisibleCont;
		tv.setText(title);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setLayoutParams(params);
		return tv;
	}

	/**
	 * 设置关联的Viewpager
	 *
	 * @param viewPager
	 * @param postion
	 */
	public void setViewPager(ViewPager viewPager, int postion) {
		this.viewPager = viewPager;
		this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (listener != null) {
					listener.onPageSelected(arg0);
				}
				setSelectTextView(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				scroll(arg0, arg1);
				if (listener != null) {
					listener.onPageScrolled(arg0, arg1, arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (listener != null) {
					listener.onPageScrollStateChanged(arg0);
				}
			}
		});
		this.viewPager.setCurrentItem(postion);
		setSelectTextView(postion);
	}

	/**
	 * 提供回调方法
	 *
	 * @param listener
	 */
	public void setPageOnchangeListener(PageOnchangeListener listener) {
		this.listener = listener;
	}

	/**
	 * 取消选中的TextView的颜色
	 *
	 */
	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
			}
		}
	}

	/**
	 * 设置选中的TextView的颜色
	 *
	 * @param position
	 */
	private void setSelectTextView(int position) {
		resetTextViewColor();
		View view = getChildAt(position);
		if (view instanceof TextView) {
			((TextView) view).setTextColor(COLOR_TEXT_SELECT);
		}
	}

	/**
	 * 设置TAB的点击事件
	 */
	private void setItemClickEvent() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewPager.setCurrentItem(j);
				}
			});
		}
	}
}
