package com.like.base.base.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.like.base.R;
import com.like.base.base.BaseFragment;
import com.like.base.base.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 基础的主页模板
 * Created by longshao on 2017/8/16.
 */

public abstract class BaseBottomFragment<P extends BasePresenter> extends BaseFragment<P> implements View.OnClickListener {

    protected abstract LinkedHashMap<BottomTabBean, BottomItemFragment> setItems(ItemBuilder builder);

    protected abstract int setIndexFragment();//设置显示那个Fragment

    @ColorInt
    protected abstract int setClickColor();

    @ColorInt
    protected abstract int setNormalColor();

    private LinearLayoutCompat bottom_bar;
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemFragment> ITEM_FRAGMENTS = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean, BottomItemFragment> ITEMS = new LinkedHashMap<>();
    private int mCurrentFragment = 0;
    protected int mIndexFragment = 0;
    private int mClickedColor = Color.RED;
    private int mNormalColor = Color.BLACK;

    @Override
    public Object getResId() {
        return R.layout.fragment_basebottom_layout;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        bottom_bar = (LinearLayoutCompat) $(R.id.bottom_bar);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        final int size = ITEMS.size();
        for (int i = 0; i < size; i++) {
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_item_icon_text_layout, bottom_bar);
            final RelativeLayout item = (RelativeLayout) bottom_bar.getChildAt(i);
            item.setTag(i);
            item.setOnClickListener(this);
            final ImageView itemIcon = (ImageView) item.findViewById(R.id.bottom_item_icon_img);
            final AppCompatTextView itemText = (AppCompatTextView) item.findViewById(R.id.bottom_item_title_tv);
            final BottomTabBean bean = TAB_BEANS.get(i);
            //初始化数据
            itemIcon.setImageResource(bean.getNomalDrawbelres());
            itemText.setText(bean.getTitle());
            itemText.setTextColor(mNormalColor);
            if (i == mIndexFragment) {
                itemIcon.setImageResource(bean.getSelectDrawbelres());
                itemText.setTextColor(mClickedColor);
            }
        }

        final ISupportFragment[] fragmentArray = ITEM_FRAGMENTS.toArray(new ISupportFragment[size]);
        loadMultipleRootFragment(R.id.bottom_bar_fragment_container, mIndexFragment, fragmentArray);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexFragment = setIndexFragment();
        mCurrentFragment = mIndexFragment;
        if (setClickColor() != 0) {
            mClickedColor = setClickColor();
        }
        if (setNormalColor() != 0) {
            mNormalColor = setNormalColor();
        }
        final ItemBuilder builder = ItemBuilder.builder();
        final LinkedHashMap<BottomTabBean, BottomItemFragment> items = setItems(builder);
        ITEMS.putAll(items);
        for (Map.Entry<BottomTabBean, BottomItemFragment> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemFragment value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_FRAGMENTS.add(value);
        }
    }

    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;
        final ImageView itemIcon = (ImageView) item.findViewById(R.id.bottom_item_icon_img);
        itemIcon.setImageResource(TAB_BEANS.get(tag).getSelectDrawbelres());
        final AppCompatTextView itemTitle = (AppCompatTextView) item.findViewById(R.id.bottom_item_title_tv);
        itemTitle.setTextColor(mClickedColor);
        showHideFragment(ITEM_FRAGMENTS.get(tag), ITEM_FRAGMENTS.get(mCurrentFragment));
        //注意先后顺序
        mCurrentFragment = tag;
    }

    /**
     * 设置显示谁
     *
     * @param index
     */
    public void setShowFragment(int index) {
        resetColor();
        RelativeLayout item = (RelativeLayout) bottom_bar.getChildAt(index);
        final ImageView itemIcon = (ImageView) item.findViewById(R.id.bottom_item_icon_img);
        itemIcon.setImageResource(TAB_BEANS.get(index).getSelectDrawbelres());
        final AppCompatTextView itemTitle = (AppCompatTextView) item.findViewById(R.id.bottom_item_title_tv);
        itemTitle.setTextColor(mClickedColor);
        showHideFragment(ITEM_FRAGMENTS.get(index), ITEM_FRAGMENTS.get(mCurrentFragment));
        mCurrentFragment = index;
    }

    /**
     * 显示第几个number
     */
    public void setShowNumber(int index, int number) {
        RelativeLayout item = (RelativeLayout) bottom_bar.getChildAt(index);
        TextView bottom_item_number_tv = item.findViewById(R.id.bottom_item_number_tv);
        bottom_item_number_tv.setText(String.valueOf(number));
        if (number <= 0) {
            bottom_item_number_tv.setVisibility(View.GONE);
        } else {
            bottom_item_number_tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 复原
     */
    private void resetColor() {
        final int count = bottom_bar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) bottom_bar.getChildAt(i);
            final ImageView itemIcon = (ImageView) item.findViewById(R.id.bottom_item_icon_img);
            final AppCompatTextView itemText = (AppCompatTextView) item.findViewById(R.id.bottom_item_title_tv);
            itemIcon.setImageResource(TAB_BEANS.get(i).getNomalDrawbelres());
            itemText.setTextColor(mNormalColor);
        }
    }

    /**
     * 获取当前选中的Fragment
     * @return
     */
    protected BottomItemFragment getSelectFragment(){
        return ITEM_FRAGMENTS.get(mCurrentFragment);
    }
}
