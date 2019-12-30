package com.like.base.adapter.rvhelper;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.like.utilslib.image.LoadImageUtil;

import java.util.LinkedHashSet;

/**
 * 对RecycleView的封装
 * Created by longshao on 2016/10/24.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private Context _context;
    private View _view;
    private final SparseArray<View> _views;
    private final LinkedHashSet<Integer> _childClickViewIds;
    private final LinkedHashSet<Integer> _childLongClickViewIds;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        _context = context;
        _view = itemView;
        _views = new SparseArray<View>();
        _childClickViewIds = new LinkedHashSet<Integer>();
        _childLongClickViewIds = new LinkedHashSet<Integer>();
    }

    public <T extends View> T cdFindViewById(int viewId) {
        View view = _views.get(viewId);
        if (view == null) {
            view = _view.findViewById(viewId);
            _views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置文本框内容
     *
     * @param viewId
     * @param value
     * @return
     */
    public BaseViewHolder setText(int viewId, CharSequence value) {
        TextView view = cdFindViewById(viewId);
        view.setText(value);
        return this;
    }

    /**
     * 设置进度条
     * @param viewId
     * @param value
     * @return
     */
    public BaseViewHolder setProgress(int viewId, int value) {
        ProgressBar view = cdFindViewById(viewId);
        view.setProgress(value);
        return this;
    }

    /**
     * 设置文本框选中状态
     *
     * @param viewId
     * @param select
     * @return
     */
    public BaseViewHolder setTextSelect(int viewId, boolean select) {
        TextView view = cdFindViewById(viewId);
        view.setSelected(select);
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param colores
     * @return
     */
    public BaseViewHolder setTextColor(int viewId, @ColorRes Integer colores) {
        TextView view = cdFindViewById(viewId);
        view.setTextColor(ContextCompat.getColor(_context, colores));
        return this;
    }

    /**
     * 默认的动画是淡入动画
     * 设置图片
     *
     * @param viewId
     * @param imageUrl
     * @return
     */
    public BaseViewHolder setImageUrl(int viewId, Object imageUrl) {
        ImageView view = cdFindViewById(viewId);
        LoadImageUtil.loadImage(view, imageUrl);
        return this;
    }

    public BaseViewHolder setBgColor(int viewId, @ColorRes int colorRes) {
        View view = cdFindViewById(viewId);
        view.setBackgroundColor(ContextCompat.getColor(_context,colorRes));
        return this;
    }

    /**
     * 设置图片
     *
     * @param viewId
     * @param imageUrl
     * @param width    图片的宽度 像素
     * @param height   图片的高度 像素
     * @return
     */
    public BaseViewHolder setImageUrl(int viewId, Object imageUrl, int width, int height) {
        ImageView view = cdFindViewById(viewId);
        LoadImageUtil.loadSizeImage(view, imageUrl, width, height);
        return this;
    }

    /**
     * 设置圆形图片
     *
     * @param viewId
     * @param imageUrl
     * @param widthorheight 表示图片的宽高
     * @return
     */
    public BaseViewHolder setRoundImageUrl(Integer viewId, String imageUrl, int widthorheight) {
        ImageView view = cdFindViewById(viewId);
        LoadImageUtil.loadRoundSizeImage(view, imageUrl, widthorheight);
        return this;
    }

    /**
     * 设置圆角图片
     *
     * @param viewId
     * @param imageUrl
     * @param radios   表示圆角度数
     * @return
     */
    public BaseViewHolder setRadiusImageUrl(Integer viewId, String imageUrl, int radios) {
        ImageView view = cdFindViewById(viewId);
        LoadImageUtil.loadRadiusImage(view, imageUrl, radios);
        return this;
    }

    /**
     * Sets the childView click listener of the view
     *
     * @param viewId The view id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder addItemClickListener(int viewId) {
        _childClickViewIds.add(viewId);
        return this;
    }

    /**
     * add long click view id
     *
     * @param viewId
     * @return
     */
    public BaseViewHolder addItemLongClickListener(int viewId) {
        _childLongClickViewIds.add(viewId);
        return this;
    }

    /**
     * 设置是否隐藏
     *
     * @param viewId  The view id.
     * @param visible boolean:isshow
     * @return
     */
    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = cdFindViewById(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置 选中与否（CompoundButton与CheckedTextView）
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setChecked(int viewId, boolean checked) {
        View view = cdFindViewById(viewId);
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    /**
     * 设置 tag
     *
     * @param viewId view Id
     * @param key    the key of tag
     * @param tag    the tag value
     * @return
     */
    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = cdFindViewById(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 设置 tag
     *
     * @param viewId view Id
     * @param tag    the tag value
     * @return
     */
    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = cdFindViewById(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 设置view是否为超链接
     *
     * @param viewId
     * @return
     */
    public BaseViewHolder linkify(int viewId) {
        TextView view = cdFindViewById(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public View get_view() {
        return _view;
    }

    public LinkedHashSet<Integer> getChildClickViewIds() {
        return _childClickViewIds;
    }

    public LinkedHashSet<Integer> getChildLongClickViewIds() {
        return _childLongClickViewIds;
    }
}
