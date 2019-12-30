package event;

public class HeartServiceEvents extends BaseEvent {

    public static final int TO_START_LOGIN = 101;//执行登陆流程

    public static final int TO_GESTURE_LOGIN = 102;//谈出手势密码

    public static final int TO_RETURN = 103;//返回

    public static final int TO_REFRESH = 104;//刷新

    public static final int TO_CHECK_PROXY = 105;//检测代理

    public static final int TO_CLICK_VIP = 106;//點擊VIP充值


    public static final int TO_BREAK_LINE_RESPONSE = 107;//接受到该协议为断线

    public static final int TO_UPLOAD_CRASH = 108;//接受到该协议为断线


    public static final int TO_REFRESH_DELI = 109;//刷新

    public static final int TO_SYSTEM_OUT = 110;//刷新

    public static final int CANCLE_GESTURE_PASSWORD = 111;//取消手勢密碼

    public static final int VIDEO_NOT_RETRY = 112;//视频不重新加载

    public static final int SURE_GESTURE_PASSWORD = 113;//确认手勢密碼

    public static final int TO_CLICK_PALY_NEXT = 114;//點擊播放下一个视频


    public HeartServiceEvents(int eventId, Object data) {
        super(eventId, data);
    }


}
