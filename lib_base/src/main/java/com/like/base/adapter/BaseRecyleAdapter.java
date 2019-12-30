package com.like.base.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.like.base.adapter.rvhelper.BaseViewHolder;
import com.like.base.adapter.rvhelper.DefaultAnimation;
import com.like.base.adapter.rvhelper.IRecycleAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * recycleView 基本适配器
 * 暂时还对 HEADER_VIEW LOADING_VIEW FOOTER_VIEW EMPTY_VIEW未进行封装
 * Created by longshao on 2016/10/24.
 */

public abstract class BaseRecyleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //基本数据处理
    protected List<T> _list;
    private int _layoutRes;
    protected Context _context;

    protected abstract void convert(BaseViewHolder holder, T item);//绑定数据

    //添加动画
    private IRecycleAnimation _mCustomAnimation;
    private IRecycleAnimation _mSelectAnimation = new DefaultAnimation();
    private int _mDuration = 300;//动画时间
    private boolean _isOpenAnimation = false;//是否打开动画
    private boolean _isFirstOnly = true;//动画是否只加载一次
    private int _lastPosition = -1;
    private Interpolator _mInterpolator = new LinearInterpolator();

    private SpanSizeLookup mSpanSizeLookup;

    public BaseRecyleAdapter(Context context, int layoutRes, List<T> list) {
        if (_list == null) {
            _list = new ArrayList<>();
        }
        if (list != null && list.size() > 0) {
            _list.addAll(list);
        }
        _layoutRes = layoutRes;
        _context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder;
        switch (viewType) {
            default:
                holder = onChildCreateViewHolder(parent);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            default:
                convert((BaseViewHolder) holder, _list.get(position));
                break;
        }
    }

    @Override
    public final int getItemViewType(int position) {
        return getChildItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * 当这个适配器创建的视图被连接到一个窗口时调用时。
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
//        int type = holder.getItemViewType();
//        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW || type == GROUP_COLUMN) {
//            setFullSpan(holder);
//        } else {
//            addAimation(holder);
//        }
        addAimation(holder);
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     * 当recycleView观察到此适配器的时候调用此方法
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (mSpanSizeLookup == null)
                        return 1;
                    else
                        return mSpanSizeLookup.getSpanSize(gridManager, position);
                }
            });
        }
    }

    /**
     * 删除一行
     *
     * @param position he item address
     */
    public void remove(int position) {
        _list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 新增一行
     *
     * @param position he item address
     * @param item     实体类
     */
    public void add(int position, T item) {
        _list.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 新增一行， 在末尾
     *
     * @param item 实体类
     */
    public void add(T item) {
        _list.add(item);
        notifyItemInserted(_list.size());
    }

    /**
     * 新增多行
     *
     * @param items 实体类集合
     */
    public void addAll(List<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        final int startPostion = _list.size();
        _list.addAll(items);
        notifyItemRangeChanged(startPostion, items.size());
    }

    /**
     * 更新所有数据
     *
     * @param items 实体类集合
     */
    public void update(List<T> items) {
        _list.clear();
        _list.addAll(items);
        notifyDataSetChanged();
    }

    public T getPositionModel(int position) {
        return _list.get(position);
    }

    /**
     * 修改某一行的值
     *
     * @param position the item address
     * @param item     实体类
     */
    public void change(int position, T item) {
        _list.set(position, item);
        notifyItemChanged(position);
    }

    public List<T> get_list() {
        return _list;
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(IRecycleAnimation animation) {
        this._isOpenAnimation = true;
        this._mCustomAnimation = animation;
    }

    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this._isOpenAnimation = true;
    }

    /**
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    public void isFirstOnly(boolean firstOnly) {
        this._isFirstOnly = firstOnly;
    }

    /**
     * 设置listview类模式
     *
     * @param rview
     * @param state
     */
    public void setLayoutManager(RecyclerView rview, int state) {
        if (rview == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context);
        if (state == LinearLayoutManager.VERTICAL) {
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        rview.setLayoutManager(layoutManager);
    }

    /**
     * 设置listview类模式 默认竖向加载
     *
     * @param rview
     */
    public void setLayoutManager(RecyclerView rview) {
        if (rview == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(_context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rview.setLayoutManager(layoutManager);
    }

    /**
     * 设置Gridview模式
     *
     * @param rview
     */
    public void setGridLayoutManager(RecyclerView rview, int row) {
        if (rview == null) {
            return;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(_context, row);
        rview.setLayoutManager(layoutManager);
    }

    /**
     * 交给子类处理 item的类型
     *
     * @param position
     * @return
     */
    protected int getChildItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 交给子类来创建 Item的视图
     *
     * @param parent
     * @return
     */
    protected BaseViewHolder onChildCreateViewHolder(ViewGroup parent) {
        return new BaseViewHolder(_context, getItemView(_layoutRes, parent));
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * 加载布局
     *
     * @param layoutResId
     * @param parent
     * @return
     */
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return LayoutInflater.from(_context).inflate(layoutResId, parent, false);
    }

    //自定义接口
    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    /**
     * 添加动画
     *
     * @param holder
     */
    private void addAimation(RecyclerView.ViewHolder holder) {
        if (_isOpenAnimation) {
            if (!_isFirstOnly || holder.getLayoutPosition() > _lastPosition) {
                IRecycleAnimation animation = null;
                if (_mCustomAnimation != null) {
                    animation = _mCustomAnimation;
                } else {
                    animation = _mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    anim.setDuration(_mDuration).start();
                    anim.setInterpolator(_mInterpolator);
                }
                _lastPosition = holder.getLayoutPosition();
            }
        }
    }
}
