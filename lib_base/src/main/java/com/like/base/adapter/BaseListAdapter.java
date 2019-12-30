package com.like.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import com.like.utilslib.other.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView GridView 列表级适配器
 *
 * @author longshao
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private String TAG;
    public Context context;
    private LayoutInflater inflater;
    public List<T> mList = new ArrayList<>();

    public BaseListAdapter(Context context, List<T> list) {
        this.context = context;
        TAG = this.getClass().getSimpleName();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        inflater = LayoutInflater.from(context);
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void update(List<T> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    /**
     * 添加单个数据
     *
     * @param item
     */
    public void add(T item) {
        mList.add(item);
        this.notifyDataSetChanged();
    }

    /**
     * 添加多个数据
     *
     * @param list
     */
    public void addAll(List<T> list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    public View initConvertView(int resource) {
        return inflater.inflate(resource, null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void log(String log) {
        LogUtil.loge(log);
    }
}
