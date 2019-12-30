package com.like.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.like.base.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AutoWidthTabLayout extends FrameLayout {

    private TabLayout mTabLayout;
    private List<String> mTabList;
    private List<View> mCustomViewList;
    private List<View> mMessageViewList;
    private int mSelectIndicatorColor;
    private int mSelectTextColor;
    private int mUnSelectTextColor;
    private int mIndicatorHeight;
    private int mIndicatorWidth;
    private int mItemWidth;
    private int mTabMode;
    private int mTabTextSize;
    private int mTabSelectTextSize;
    private boolean tabSelectMode;//是否加粗
    private boolean tabIsShow;//是否显示底部的横线 默认不显示

    public AutoWidthTabLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AutoWidthTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoWidthTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoWidthTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoWidthTabLayout);
        mSelectIndicatorColor = typedArray.getColor(R.styleable.AutoWidthTabLayout_tabIndicatorColor, context.getResources().getColor(R.color.fontcColor1));
        mUnSelectTextColor = typedArray.getColor(R.styleable.AutoWidthTabLayout_tabTextColor, Color.parseColor("#666666"));
        mSelectTextColor = typedArray.getColor(R.styleable.AutoWidthTabLayout_tabSelectTextColor, context.getResources().getColor(R.color.fontcColor1));
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.AutoWidthTabLayout_tabIndicatorHeight, 1);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.AutoWidthTabLayout_tabIndicatorWidth, 0);
        mItemWidth = typedArray.getDimensionPixelSize(R.styleable.AutoWidthTabLayout_tabWidth, 0);
        mTabTextSize = typedArray.getInteger(R.styleable.AutoWidthTabLayout_tabTextSize, 14);
        mTabSelectTextSize = typedArray.getInteger(R.styleable.AutoWidthTabLayout_tabSelectTextSize, 14);
        mTabMode = typedArray.getInt(R.styleable.AutoWidthTabLayout_tab_Mode, 2);
        tabSelectMode = typedArray.getBoolean(R.styleable.AutoWidthTabLayout_tabSelectMode, false);
        tabIsShow = typedArray.getBoolean(R.styleable.AutoWidthTabLayout_tabIndicatorShow, false);
        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        readAttr(context, attrs);
        mTabList = new ArrayList<>();
        mCustomViewList = new ArrayList<>();
        mMessageViewList = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_tab_view, this, true);
        mTabLayout = view.findViewById(R.id.enhance_tab_view);

        // 添加属性
        mTabLayout.setTabMode(mTabMode == 1 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        addOnTabSelectedListeners();
    }

    public void addOnTabSelectedListeners() {
        if (mTabLayout.getTabAt(0) != null) {
            View view = mTabLayout.getTabAt(0).getCustomView();
            if (view == null) {
                return;
            }
            TextView text = (TextView) view.findViewById(R.id.tab_item_text);
            View indicator = view.findViewById(R.id.tab_item_indicator);
            text.setTextColor(mSelectTextColor);
//            indicator.setBackgroundColor(mSelectIndicatorColor);
            indicator.setVisibility(tabIsShow ? View.VISIBLE : GONE);
            text.getPaint().setFakeBoldText(tabSelectMode);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabSelectTextSize);
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    if (view == null) {
                        return;
                    }
                    TextView text = (TextView) view.findViewById(R.id.tab_item_text);
                    View indicator = view.findViewById(R.id.tab_item_indicator);
                    if (i == tab.getPosition()) { // 选中状态
                        text.setTextColor(mSelectTextColor);
//                        indicator.setBackgroundColor(mSelectIndicatorColor);
                        indicator.setVisibility(tabIsShow ? View.VISIBLE : View.GONE);
                        text.getPaint().setFakeBoldText(tabSelectMode);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabSelectTextSize);
                    } else {// 未选中状态
                        text.setTextColor(mUnSelectTextColor);
                        indicator.setVisibility(tabIsShow ? View.INVISIBLE : View.GONE);
                        text.getPaint().setFakeBoldText(false);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabTextSize);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public List<View> getCustomViewList() {
        return mCustomViewList;
    }

    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    /**
     * 与TabLayout 联动
     *
     * @param viewPager
     */
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        mTabLayout.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager, this));
    }


    /**
     * retrive TabLayout Instance
     *
     * @return
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * 添加tab
     *
     * @param tab
     */
    public void addTab(String tab) {
        addTab(tab, false);
    }

    public void clearTab() {
        mTabLayout.removeAllTabs();
        mTabLayout.clearOnTabSelectedListeners();
        mTabList.clear();
        mMessageViewList.clear();
        mCustomViewList.clear();
    }

    public void setTabTextValue(String value, final int position) {
        ((TextView) mCustomViewList.get(position).findViewById(R.id.tab_item_text)).setText(value);
    }

    /**
     * 添加tab
     *
     * @param tab
     */
    public void addTab(String tab, boolean isshowMessage) {
        mTabList.add(tab);
        View customView = getTabView(getContext(), tab, mIndicatorWidth, mIndicatorHeight, mTabTextSize);

        View messageView = customView.findViewById(R.id.view_message);
        if (isshowMessage) {
            messageView.setVisibility(VISIBLE);
        }
        mMessageViewList.add(messageView);
        mCustomViewList.add(customView);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(customView));
    }

    /**
     * 隐藏某个tab中的message点
     *
     * @param index
     */
    public void showMessage(int index, boolean isShow) {
        mMessageViewList.get(index).setVisibility(isShow ? VISIBLE : GONE);
    }

    public static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private final ViewPager mViewPager;
        private final WeakReference<AutoWidthTabLayout> mTabLayoutRef;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager, AutoWidthTabLayout enhanceTabLayout) {
            mViewPager = viewPager;
            mTabLayoutRef = new WeakReference<AutoWidthTabLayout>(enhanceTabLayout);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
            AutoWidthTabLayout mTabLayout = mTabLayoutRef.get();
            if (mTabLayoutRef != null) {
                List<View> customViewList = mTabLayout.getCustomViewList();
                if (customViewList == null || customViewList.size() == 0) {
                    return;
                }
                for (int i = 0; i < customViewList.size(); i++) {
                    View view = customViewList.get(i);
                    if (view == null) {
                        return;
                    }
                    TextView text = (TextView) view.findViewById(R.id.tab_item_text);
                    View indicator = view.findViewById(R.id.tab_item_indicator);
                    if (i == tab.getPosition()) { // 选中状态
                        text.setTextColor(mTabLayout.mSelectTextColor);
//                        indicator.setBackgroundColor(mTabLayout.mSelectIndicatorColor);
                        indicator.setVisibility(mTabLayout.tabIsShow ? View.VISIBLE : View.GONE);
                        text.getPaint().setFakeBoldText(mTabLayout.tabSelectMode);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabLayout.mTabSelectTextSize);
                    } else {// 未选中状态
                        text.setTextColor(mTabLayout.mUnSelectTextColor);
                        indicator.setVisibility(mTabLayout.tabIsShow ? View.INVISIBLE : View.GONE);
                        text.getPaint().setFakeBoldText(false);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabLayout.mTabTextSize);
                    }
                }
            }

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
        }
    }

    /**
     * 获取Tab 显示的内容
     *
     * @param context
     * @param
     * @return
     */
    private View getTabView(Context context, String text, int indicatorWidth, int indicatorHeight, int textSize) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null);
        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTabLayout.getLayoutParams().height);
        view.setLayoutParams(viewParams);
        TextView tabText = (TextView) view.findViewById(R.id.tab_item_text);
        if (indicatorWidth > 0) {
            View indicator = view.findViewById(R.id.tab_item_indicator);
            ViewGroup.LayoutParams layoutParams = indicator.getLayoutParams();
            layoutParams.width = indicatorWidth;
            layoutParams.height = indicatorHeight;
            indicator.setLayoutParams(layoutParams);
        }
        tabText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        tabText.setTextColor(mUnSelectTextColor);
        tabText.setText(text);
        return view;
    }
}
