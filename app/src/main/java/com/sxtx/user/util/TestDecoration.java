package com.sxtx.user.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDecoration extends RecyclerView.ItemDecoration {

    private boolean isFirst = true;
    private int groupViewHeight = 0;
    private View groupView;
    private Context mContext;
    private List<Integer> groupList = new ArrayList<>();//用户设置的分组列表
    private Map<Object, Integer> groups = new HashMap<>();//保存startPosition与分组对象的对应关系
    private int[] groupPositions;//保存分组startPosition的数组
    private int positionIndex;//分组对应的startPosition在groupPositions中的索引

    private DecorationCallback decorationCallback;
    private int indexCache = -1;
    private boolean isStickyHeader = true;//是否粘性头部

    public TestDecoration(Context context, View groupView, DecorationCallback decorationCallback) {
        this.mContext = context;
        this.groupView = groupView;
        this.decorationCallback = decorationCallback;
    }

    /*通过Rect为每个Item设置偏移，用于绘制Decoration。*/
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (isFirst) {
            measureView(groupView, parent);//绘制View需要先测量View的大小及相应的位置
            if (groupList.size() == 0) {//若用户没有设置分组，跳出（下同）
                return;
            }
            groupPositions = new int[groupList.size()];
            positionIndex = 0;

            for (int i = 0; i < groupList.size(); i++) {//保存groupItem与其startPosition的对应关系
                int p = groupList.get(i);
                if (groups.get(p) == null) {
                    groups.put(p, groupList.get(i));
                    groupPositions[i] = p;
                }
            }
            isFirst = false;
        }
    }

    /*通过该方法，在Canvas上绘制内容，在绘制Item之前调用。（如果没有通过getItemOffsets设置偏移的话，Item的内容会将其覆盖）*/
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (groupList.size() == 0) {//若用户没有设置分组，跳出（下同）
            return;
        }
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            float left = child.getLeft();
            float right = child.getRight();
            float top = child.getTop();

            int position = parent.getChildAdapterPosition(child);
            if (groups.get(position) != null) {
                Rect rect = new Rect((int) left, (int) (top - groupViewHeight), (int) right, (int) top);
//                groups.get(position).setData(KEY_RECT,rect);//用于判断点击范围

                c.save();
                c.translate(left, top - groupViewHeight);//将画布起点移动到之前预留空间的左上角
                decorationCallback.buildGroupView(groupView, groups.get(position));//通过接口回调得知GroupView内部控件的数据
                measureView(groupView, parent);//因为内部控件设置了数据，所以需要重新测量View
                groupView.draw(c);
                c.restore();
            }
        }
    }

    /*通过该方法，在Canvas上绘制内容,在Item之后调用。(画的内容会覆盖在item的上层)*/
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (groupList.size() == 0|| !isStickyHeader) {
            return;
        }
        int childCount = parent.getChildCount();
        Map<Object, Object> map = new HashMap<>();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            float top = child.getTop();
            int position = parent.getChildAdapterPosition(child);
            if (groups.get(position) != null) {
                positionIndex = searchGroupIndex(groupPositions, position);
                if (map.get("cur") == null) {
                    map.put("cur", positionIndex);
                    map.put("curTop", top);
                } else {
                    if (map.get("next") == null) {
                        map.put("next", positionIndex);
                        map.put("nextTop", top);
                    }
                }
            }
        }
        c.save();
        if (map.get("cur") != null) {//如果当前组不为空，说明RecyclerView可见部分至少有一个GroupView
            indexCache = (int)map.get("cur");
            float curTop = (float)map.get("curTop");
            if(curTop-groupViewHeight<=0){//保持当前组GroupView一直在顶部
                curTop = 0;
            }else {
                map.put("pre",(int)map.get("cur")-1);
                if(curTop - groupViewHeight < groupViewHeight){//判断与上一组的碰撞，推动当前的顶部GroupView
                    curTop = curTop - groupViewHeight*2;
                }else {
                    curTop = 0;
                }
                indexCache = (int)map.get("pre");
            }
            if(map.get("next")!=null){
                float nextTop = (float)map.get("nextTop");
                if(nextTop - groupViewHeight < groupViewHeight){//判断与下一组的碰撞，推动当前的顶部GroupView
                    curTop = nextTop - groupViewHeight*2;
                }
            }

            c.translate(0,curTop);
            if(map.get("pre")!=null){//判断顶部childView的分组归属，绘制对应的GroupView
                drawGroupView(c,parent,(int)map.get("pre"));
            }else {
                drawGroupView(c,parent,(int)map.get("cur"));
            }
        }else {//否则当前组为空时，通过之前缓存的索引找到上一个GroupView并绘制到顶部
            c.translate(0,0);
            drawGroupView(c,parent,indexCache);
        }
        c.restore();
    }

    /**
     * 绘制GroupView
     * @param canvas
     * @param parent
     * @param index
     */
    private void drawGroupView(Canvas canvas,RecyclerView parent,int index){
        if(index<0){
            return;
        }
        decorationCallback.buildGroupView(groupView,groups.get(groupPositions[index]));
        measureView(groupView,parent);
        groupView.draw(canvas);
    }
    /**
     * 查询startPosition对应分组的索引
     *
     * @param groupArrays
     * @param startPosition
     * @return
     */
    private int searchGroupIndex(int[] groupArrays, int startPosition) {
        Arrays.sort(groupArrays);
        int result = Arrays.binarySearch(groupArrays, startPosition);
        return result;
    }

    /**
     * 测量View的大小和位置
     *
     * @param view
     * @param parent
     */
    private void measureView(View view, View parent) {
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);

        int childHeight;
        if (view.getLayoutParams().height > 0) {
            childHeight = View.MeasureSpec.makeMeasureSpec(view.getLayoutParams().height, View.MeasureSpec.EXACTLY);
        } else {
            childHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
        }

        view.measure(childWidth, childHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        groupViewHeight = view.getMeasuredHeight();
    }

    public interface DecorationCallback {
        /**
         * 设置分组
         *
         * @param groupList
         */
        void setGroup(List<Integer> groupList);

        /**
         * 构建GroupView
         *
         * @param groupView
         * @param groupItem
         */
        void buildGroupView(View groupView, Integer groupItem);
    }

    /**
     * 开关粘性头部
     * @param isStickyHeader
     */
    public void setStickyHeader(boolean isStickyHeader){
        this.isStickyHeader = isStickyHeader;
    }
}
