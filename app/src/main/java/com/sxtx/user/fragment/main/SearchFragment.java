package com.sxtx.user.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cy.cyflowlayoutlibrary.FlowLayoutAdapter;
import com.cy.cyflowlayoutlibrary.FlowLayoutScrollView;
import com.google.protobuf.ProtocolStringList;
import com.gyf.immersionbar.ImmersionBar;
import com.like.base.base.BaseFragment;
import com.sxtx.user.R;
import com.sxtx.user.mvp.presenter.main.SearchPresenter;
import com.sxtx.user.mvp.view.main.ISearchView;
import com.sxtx.user.util.KeybordUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment<SearchPresenter> implements View.OnClickListener, ISearchView {

    ImageView im_back;
    TextView tv_search;
    LinearLayout li_back, li_top_one;
    Button btn_delete_search;
    EditText edt_search_keywords;
    FlowLayoutScrollView fl_hot_search, fl_history_search;
    FlowLayoutAdapter<String> flowhotAdapter, flowhistoryAdapter;

    @Override
    public Object getResId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        initView();
        initListense();
        initLy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).statusBarDarkFont(true).init();
        }
    }


    private void initView() {
        li_top_one = $(R.id.li_top_one);
        ImmersionBar.setTitleBar(getActivity(), ImmersionBar.getStatusBarHeight(getActivity()), li_top_one);
        im_back = $(R.id.im_back);
        im_back.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.toolbar_back));
        tv_search = $(R.id.tv_search);
        btn_delete_search = $(R.id.btn_delete_search);
        li_back = $(R.id.li_back);
        edt_search_keywords = $(R.id.edt_search_keywords);
        fl_hot_search = $(R.id.fl_hot_search);
        fl_history_search = $(R.id.fl_history_search);
    }

    private void initListense() {
        li_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        btn_delete_search.setOnClickListener(this);
    }

    List<String> hot_list = new ArrayList<>();
    List<String> history_list = new ArrayList<>();

    private void initLy() {


        flowhotAdapter = new FlowLayoutAdapter<String>(hot_list) {
            @Override
            public void bindDataToView(ViewHolder holder, int position, String bean) {
                holder.setText(R.id.tv_search_name, bean);
            }

            @Override
            public void onItemClick(int position, String bean) {
                edt_search_keywords.setText(bean);
                sureSearch(bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_search;
            }
        };

        flowhistoryAdapter = new FlowLayoutAdapter<String>(history_list) {
            @Override
            public void bindDataToView(ViewHolder holder, int position, String bean) {
                holder.setText(R.id.tv_search_name, bean);
            }

            @Override
            public void onItemClick(int position, String bean) {
                edt_search_keywords.setText(bean);
                sureSearch(bean);

            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_search;
            }
        };
        fl_hot_search.setAdapter(flowhotAdapter);
        fl_history_search.setAdapter(flowhistoryAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.ClickSearchReqeust();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.li_back:
                pop();
                break;
            case R.id.tv_search:
                String keyword = edt_search_keywords.getText().toString().trim();
                sureSearch(keyword);
                break;
            case R.id.btn_delete_search:
                mPresenter.clickRemoveRecordReqeust();
                break;
        }

    }

    int requestCode = 0x11;

    public void sureSearch(String searchKeyword) {
        if (TextUtils.isEmpty(searchKeyword)) {
            showToast("关键词不能爲空");
            return;
        }
        KeybordUtil.hideInput(this);
        SearchResultFragment resultFragment = new SearchResultFragment();
        resultFragment.setKeyWords(searchKeyword);
        startForResult(resultFragment, requestCode);
        if (history_list.size()>0){
            if (!isContains(searchKeyword))history_list.add(searchKeyword);
        }else {
            history_list.add(searchKeyword);
        }
        fl_history_search.setVisibility(View.VISIBLE);
        flowhistoryAdapter.notifyDataSetChanged();
    }

    public boolean isContains(String searchKeyword){
       for (String keyword:history_list){
           if (TextUtils.equals(searchKeyword,keyword)){
               return true;
           }
       }
       return false;
    }


    @Override
    public void clickSearchReqeustSucceed(@NotNull ProtocolStringList apiUserKeyWord, @NotNull ProtocolStringList sysUserKeyWord) {
        if (apiUserKeyWord.size() > 0) {
           // fl_hot_search.setVisibility(View.VISIBLE);
            for (String element : apiUserKeyWord) {
                hot_list.add(element);
            }
        }else {
           // fl_hot_search.setVisibility(View.GONE);
        }
        if (sysUserKeyWord.size() > 0) {
            fl_history_search.setVisibility(View.VISIBLE);
            for (String element : sysUserKeyWord) {
                history_list.add(element);
            }
        }else {
            fl_history_search.setVisibility(View.GONE);
        }
        flowhotAdapter.notifyDataSetChanged();
        flowhistoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void clickRemoveSucceed() {
        fl_history_search.setVisibility(View.GONE);
        history_list.clear();
        flowhistoryAdapter.notifyDataSetChanged();
    }
}
