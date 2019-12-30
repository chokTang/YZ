package com.like.base.dialog.adapter;

import com.contrarywind.adapter.WheelAdapter;

import java.util.List;

public class ArrayWheelAdapter implements WheelAdapter<String> {

    private List<String> mList;

    public ArrayWheelAdapter(List<String> list){
        mList=list;
    }

    @Override
    public int getItemsCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public String getItem(int index) {
        return mList.get(index);
    }

    @Override
    public int indexOf(String o) {
        return -1;
    }
}
