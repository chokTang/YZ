package com.like.base.dialog.fragment;

import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.like.base.R;
import com.like.base.dialog.BaseDialogFragment;
import com.like.base.dialog.adapter.ArrayWheelAdapter;
import com.like.base.dialog.inter.IDialogTimeListener;
import com.like.utilslib.date.DateStyle;
import com.like.utilslib.date.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间选择器
 */
public class DialogTimeFragment extends BaseDialogFragment {

    @StringDef({YYYY_MM_DD, YYYY_MM_DD_HH_MM, HH_DD})
    public @interface DateTimeStyle {
    }

    public static final String YYYY_MM_DD = "YYYY_MM_DD";
    public static final String YYYY_MM_DD_HH_MM = "YYYY_MM_DD_HH_MM";
    public static final String HH_DD = "HH_MM";

    private WheelView wheel_year_time;
    private WheelView wheel_mouth_time;
    private WheelView wheel_day_time;
    private WheelView wheel_hour_time;
    private WheelView wheel_minuts_time;
    private TextView tv_title_time;
    private TextView tv_cancle_time;
    private TextView tv_sure_time;

    @DateTimeStyle
    private String mDateStyle = YYYY_MM_DD;
    private String mTitle;
    private String mCancle;
    private String mSure;
    private IDialogTimeListener mListener;
    private Date mDate = new Date();//选中的时间
    private boolean mCyclic = false;//是否循环
    private int mMaxYear = DateUtil.getYear(new Date());
    private int mMinYear = 1970;

    private final int textSize = 16;//字体大小
    private final float lineSpace = 2f;//行间距
    private final int textOutColor = R.color.fontcColor2;//区域外的颜色值
    private final int textCenterColor = R.color.fontcColor1;//中间的颜色值
    private List<String> mDayList = new ArrayList<>();

    public static DialogTimeFragment newIntance() {
        DialogTimeFragment fragment = new DialogTimeFragment();
        return fragment;
    }

    @Override
    public void initView() {
        wheel_year_time = (WheelView) findViewById(R.id.wheel_year_time);
        wheel_mouth_time = (WheelView) findViewById(R.id.wheel_mouth_time);
        wheel_day_time = (WheelView) findViewById(R.id.wheel_day_time);
        wheel_hour_time = (WheelView) findViewById(R.id.wheel_hour_time);
        wheel_minuts_time = (WheelView) findViewById(R.id.wheel_minuts_time);
        tv_title_time = (TextView) findViewById(R.id.tv_title_time);
        tv_cancle_time = (TextView) findViewById(R.id.tv_cancle_time);
        tv_sure_time = (TextView) findViewById(R.id.tv_sure_time);
    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(mTitle)) {
            tv_title_time.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mCancle)) {
            tv_cancle_time.setText(mCancle);
        }
        if (!TextUtils.isEmpty(mSure)) {
            tv_sure_time.setText(mSure);
        }
        tv_cancle_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancle();
                }
                getDialog().cancel();
            }
        });
        tv_sure_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateTime = "";
                Date date = null;
                switch (mDateStyle) {
                    case YYYY_MM_DD:
                        dateTime = String.format("%d-%d-%d", wheel_year_time.getCurrentItem() + mMinYear,
                                wheel_mouth_time.getCurrentItem() + 1, wheel_day_time.getCurrentItem() + 1);
                        date=DateUtil.StringToDate(dateTime, DateStyle.YYYY_MM_DD);
                        break;
                    case YYYY_MM_DD_HH_MM:
                        dateTime = String.format("%d-%d-%d %d:%d", wheel_year_time.getCurrentItem() + mMinYear,
                                wheel_mouth_time.getCurrentItem() + 1, wheel_day_time.getCurrentItem() + 1,
                                wheel_hour_time.getCurrentItem(), wheel_minuts_time.getCurrentItem());
                        date=DateUtil.StringToDate(dateTime, DateStyle.YYYY_MM_DD_HH_MM);
                        break;
                    case HH_DD:
                        dateTime = String.format("%s %d:%d",DateUtil.DateToString(new Date(),DateStyle.YYYY_MM_DD) ,wheel_hour_time.getCurrentItem()
                                , wheel_minuts_time.getCurrentItem());
                        date=DateUtil.StringToDate(dateTime, DateStyle.YYYY_MM_DD_HH_MM);
                        break;
                }
                if (mListener != null) {
                    mListener.onSure(date);
                }
                getDialog().cancel();
            }
        });
        switch (mDateStyle) {
            case YYYY_MM_DD:
                initYearView(DateUtil.getYear(mDate));
                initMouthView(DateUtil.getMonth(mDate) + 1);
                initDayView(DateUtil.getDay(mDate));
                break;
            case YYYY_MM_DD_HH_MM:
                initYearView(DateUtil.getYear(mDate));
                initMouthView(DateUtil.getMonth(mDate) + 1);
                initDayView(DateUtil.getDay(mDate));
                initHourView(DateUtil.getHour(mDate));
                initMinutsView(DateUtil.getMinute(mDate));
                break;
            case HH_DD:
                initHourView(DateUtil.getHour(mDate));
                initMinutsView(DateUtil.getMinute(mDate));
                break;
        }

        wheel_year_time.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mDayList.clear();
                int mouthOfDay = getMouthOfDay(index + mMinYear, wheel_mouth_time.getCurrentItem() + 1);
                for (int i = 0; i < mouthOfDay; i++) {
                    mDayList.add(String.format("%d日", i + 1));
                }
                wheel_day_time.setAdapter(new ArrayWheelAdapter(mDayList));
            }
        });

        wheel_mouth_time.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mDayList.clear();
                int mouthOfDay = getMouthOfDay(wheel_year_time.getCurrentItem() + mMinYear, index + 1);
                for (int i = 0; i < mouthOfDay; i++) {
                    mDayList.add(String.format("%d日", i + 1));
                }
                wheel_day_time.setAdapter(new ArrayWheelAdapter(mDayList));
                int daySelectIndex = wheel_day_time.getCurrentItem();
                wheel_day_time.setCurrentItem(daySelectIndex);
            }
        });
    }

    /**
     * 初始化年份
     *
     * @param year
     */
    private final void initYearView(int year) {
        if (mMaxYear < mMinYear) {
            throw new RuntimeException("最大时间小于了最小时间了");
        }
        wheel_year_time.setVisibility(View.VISIBLE);
        wheel_year_time.setCyclic(mCyclic);
        wheel_year_time.setTextSize(textSize);
        wheel_year_time.setLineSpacingMultiplier(lineSpace);
        wheel_year_time.setTextColorOut(ContextCompat.getColor(getContext(), textOutColor));
        wheel_year_time.setTextColorCenter(ContextCompat.getColor(getContext(), textCenterColor));
        List<String> myearList = new ArrayList<>();
        for (int i = mMinYear; i <= mMaxYear; i++) {
            myearList.add(String.format("%d年", i));
        }
        wheel_year_time.setAdapter(new ArrayWheelAdapter(myearList));
        wheel_year_time.setCurrentItem(year - mMinYear);
    }

    /**
     * 初始化月份
     *
     * @param mouth
     */
    private final void initMouthView(int mouth) {
        wheel_mouth_time.setVisibility(View.VISIBLE);
        wheel_mouth_time.setCyclic(mCyclic);
        wheel_mouth_time.setTextSize(textSize);
        wheel_mouth_time.setLineSpacingMultiplier(lineSpace);
        wheel_mouth_time.setTextColorOut(ContextCompat.getColor(getContext(), textOutColor));
        wheel_mouth_time.setTextColorCenter(ContextCompat.getColor(getContext(), textCenterColor));
        List<String> mouthList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            mouthList.add(String.format("%d月", i + 1));
        }
        wheel_mouth_time.setAdapter(new ArrayWheelAdapter(mouthList));
        wheel_mouth_time.setCurrentItem(mouth - 1);
    }

    /**
     * 初始化天
     *
     * @param day
     */
    private final void initDayView(int day) {
        wheel_day_time.setVisibility(View.VISIBLE);
        wheel_day_time.setCyclic(mCyclic);
        wheel_day_time.setTextSize(textSize);
        wheel_day_time.setLineSpacingMultiplier(lineSpace);
        wheel_day_time.setTextColorOut(ContextCompat.getColor(getContext(), textOutColor));
        wheel_day_time.setTextColorCenter(ContextCompat.getColor(getContext(), textCenterColor));
        int mouthOfDay = getMouthOfDay(wheel_year_time.getCurrentItem() + mMinYear, wheel_mouth_time.getCurrentItem() + 1);
        for (int i = 0; i < mouthOfDay; i++) {
            mDayList.add(String.format("%d日", i + 1));
        }
        wheel_day_time.setAdapter(new ArrayWheelAdapter(mDayList));
        wheel_day_time.setCurrentItem(day - 1);
    }

    /**
     * 初始化小时
     *
     * @param hour
     */
    private final void initHourView(int hour) {
        wheel_hour_time.setVisibility(View.VISIBLE);
        wheel_hour_time.setCyclic(mCyclic);
        wheel_hour_time.setTextSize(textSize);
        wheel_hour_time.setLineSpacingMultiplier(lineSpace);
        wheel_hour_time.setTextColorOut(ContextCompat.getColor(getContext(), textOutColor));
        wheel_hour_time.setTextColorCenter(ContextCompat.getColor(getContext(), textCenterColor));
        List<String> hourList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourList.add(String.format("%02d时", i));
        }
        wheel_hour_time.setAdapter(new ArrayWheelAdapter(hourList));
        wheel_hour_time.setCurrentItem(hour);
    }

    /**
     * 初始化分钟
     *
     * @param minuts
     */
    private final void initMinutsView(int minuts) {
        wheel_minuts_time.setVisibility(View.VISIBLE);
        wheel_minuts_time.setCyclic(mCyclic);
        wheel_minuts_time.setTextSize(textSize);
        wheel_minuts_time.setLineSpacingMultiplier(lineSpace);
        wheel_minuts_time.setTextColorOut(ContextCompat.getColor(getContext(), textOutColor));
        wheel_minuts_time.setTextColorCenter(ContextCompat.getColor(getContext(), textCenterColor));
        List<String> minutsList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutsList.add(String.format("%02d分", i));
        }
        wheel_minuts_time.setAdapter(new ArrayWheelAdapter(minutsList));
        wheel_minuts_time.setCurrentItem(minuts);
    }

    /**
     * 设置时间对话框样式
     *
     * @return
     */
    public DialogTimeFragment setDateStyle(@DateTimeStyle String dateStyle) {
        this.mDateStyle = dateStyle;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public DialogTimeFragment setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    /**
     * 设置取消按钮文字
     *
     * @param cancle
     * @return
     */
    public DialogTimeFragment setCancle(String cancle) {
        this.mCancle = cancle;
        return this;
    }

    /**
     * 设置确定按钮文字
     *
     * @param sure
     * @return
     */
    public DialogTimeFragment setSure(String sure) {
        this.mSure = sure;
        return this;
    }

    /**
     * 设置dialog事件监听
     *
     * @param listener
     * @return
     */
    public DialogTimeFragment setDialogListener(IDialogTimeListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 设置是否循环滚动时间轴，默认false
     *
     * @param cyclic
     * @return
     */
    public DialogTimeFragment setCyclic(boolean cyclic) {
        this.mCyclic = cyclic;
        return this;
    }

    /**
     * 设置选中时间
     *
     * @param date
     * @return
     */
    public DialogTimeFragment setCurrentDate(Date date) {
        if (date != null) {
            this.mDate = date;
        }
        return this;
    }

    /**
     * 设置最大年份
     *
     * @param maxYear
     * @return
     */
    public DialogTimeFragment setMaxYear(int maxYear) {
        if (maxYear > 1970) {
            this.mMaxYear = maxYear;
        }
        return this;
    }

    /**
     * 设置最小年份
     *
     * @param minYear
     * @return
     */
    public DialogTimeFragment setMinYear(int minYear) {
        this.mMinYear = minYear;
        return this;
    }

    @Override
    public Object getResId() {
        return R.layout.dialog_fragment_time_layout;
    }

    @Override
    public int getViewWidth() {
        return -1;
    }

    @Override
    public int getViewHeight() {
        return -2;
    }

    @Override
    public int getViewGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getAnimationType() {
        return FORM_BOTTOM_TO_BOTTOM;
    }

    /**
     * 根据月份获取天数
     *
     * @param mouth
     * @return
     */
    private final int getMouthOfDay(int year, int mouth) {
        int day = 0;
        switch (mouth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = 30;
                break;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {
                    day = 29;
                } else {
                    day = 28;
                }
                break;
        }
        return day;
    }
}
