package com.sxtx.user.model.request;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.cmd.Command;
import com.lyh.protocol.login.Api;
import com.lyh.protocol.login.LoginServer;
import com.sxtx.user.model.encryption.Aes256Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.sxtx.user.ConfigData.AES_IV;
import static com.sxtx.user.ConfigData.AES_KEY;
import static com.sxtx.user.ConfigData.MEDIA_TYPE;

/**
 * 介紹:
 * 作者:CHOK
 */
public class RequestUtil {


    /**
     * 数据返回
     *
     * @param s
     * @return
     */
    public static BaseMessage.NetMessage Response(InputStream s) {
        DataInputStream dataInputStream = new DataInputStream(s);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        while (true) {
            try {
                if ((len = dataInputStream.read(data, 0, data.length)) == -1)
                    break;
                baos.write(data, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            byte[] msgData = baos.toByteArray();
            byte[] aes256Data = Aes256Utils.decrypt(msgData, AES_KEY, AES_IV);
            if (aes256Data != null) {
                BaseMessage.NetMessage netMessage = BaseMessage.NetMessage.parseFrom(aes256Data);
                // if (netMessage.getBody().)
                return netMessage;
            } else {
                return null;
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 数据返回流-》String格式
     *
     * @param s
     * @return
     */
    public static String Response1(InputStream s) {
        DataInputStream dataInputStream = new DataInputStream(s);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        while (true) {
            try {
                if ((len = dataInputStream.read(data, 0, data.length)) == -1)
                    break;
                baos.write(data, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] msgData = baos.toByteArray();


        InputStream inputStream = null;

        inputStream = new ByteArrayInputStream(msgData);
        InputStreamReader input = new InputStreamReader(inputStream);
        BufferedReader bf = new BufferedReader(input);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if ((line = bf.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
        return sb.toString();
    }


    /**
     * 加密
     *
     * @param enMsg
     * @return
     */
    private static RequestBody encreptionMsg(BaseMessage.NetMessage enMsg) {
        byte[] data = enMsg.toByteArray();
        final byte[] aes256Data = Aes256Utils.encrypt(data, AES_KEY, AES_IV);

        RequestBody parms = RequestBody.create(MediaType.parse(MEDIA_TYPE), aes256Data);
        return parms;
    }

    /**
     * 普通登錄 token
     * <p>
     * 加密
     *
     * @param loginRequest
     * @return
     */
    public static RequestBody requestBodyCommonLoginReqeust(LoginServer.CommonLoginReqeust loginRequest) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_L_COMMON_LOGIN_REQUEST.getNumber())
                .setBody(loginRequest.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 獲取版本信息
     * <p>
     * 加密
     *
     * @param getAppVersionReqeust
     * @return
     */
    public static RequestBody requestGetAppVersionReqeust(Api.GetAppVersionReqeust getAppVersionReqeust) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_APP_VERSION_REQUEST.getNumber())
                .setBody(getAppVersionReqeust.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 获取IP
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyGetLogicIpRequest(LoginServer.GetLogicIpRequest request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_L_GET_LOGIC_IP_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);

    }


    /**
     * 手机登陆
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyMobileLoginRequest(LoginServer.MobileLoginRequest request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_L_MOBILE_LOGIN_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 找回賬號
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody getMyTopAcount(Api.GetMyTopAcountReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_MY_TOP_ACOUNT_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求二维码找回密码
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody setAccountKey(Api.GetRrCodeRetrievePasswordReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_QR_CODE_RETRIEVE_PASSWORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 修改密碼
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody modifyPassword(Api.ModifyPasswordReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_MODIFY_PASSWORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 用户逻辑服验证请求
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyVertifyUserRequest(Api.VertifyUserRequest request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_VERTIFY_USER_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * //客户端心跳消息(每1-2分钟发一次)
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyHeartRequest(Api.HeartRequest request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.HEART_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    //////主頁相關接口


    /**
     * 请求最新/排行的视频数
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetNewestPageDataReqeust(Api.GetNewestPageDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_NEWEST_PAGE_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 获取观看历史
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody inviteCodeReqeust(Api.InviteCodeReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_INVITECODE_BIND_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 绑定手机
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody BingdingPhoneReqeust(Api.MobileCodeReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_MOBILE_BIND_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 获取观看历史
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestCheckHistroyRecrdReqeust(Api.CheckHistroyRecrdReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CHECK_HISTORY_RECORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 获取我的消息
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestClickMyMessageReqeust(Api.ClickMyMessageReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CLICK_MY_MESSAGE_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 獲取自動播放
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody AutoPlayReqeust(Api.AutoPlayReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_AUTO_PLAY_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 修改自動播放
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestModifyAutoPlayReqeust(Api.ModifyAutoPlayReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_MODIFY_AUTO_PLAY_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 清除搜索
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestClickRemoveRecordReqeust(Api.ClickRemoveRecordReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CLICK_REMOVE_RECORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();
        return encreptionMsg(enMsg);
    }

    /**
     * 搜索查詢
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestClickSearchReqeust(Api.ClickSearchReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CLICK_SEARCH_REQUEST.getNumber())
                .setBody(request.toByteString()).build();
        return encreptionMsg(enMsg);
    }

    /**
     * 確認搜索
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestSureSearchKeyWordReqeust(Api.SureSearchKeyWordReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_SURE_SEARCH_KEYWORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();
        return encreptionMsg(enMsg);
    }

    /**
     * 获取收藏历史
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestCheckCollectRecrdReqeust(Api.CheckCollectRecrdReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CHECK_COLLECT_RECORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 删除观看历史
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestDeleteHistroyRecrdReqeust(Api.DeleteHistroyRecrdReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_DELETE_HISTORY_RECORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求推荐的视频数
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetRecommendPageDataReqeust(Api.GetRecommendPageDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_RECOMMEND_PAGE_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求推荐的换一批
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetRecommendInABatchReqeust(Api.GetRecommendInABatchReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_RECOMMEND_IN_A_BATCH_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求公告信息  跑馬燈
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetTrumpetReqeust(Api.GetTrumpetReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_TRUMPET_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求公告信息
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestClickNoticeReqeust(Api.ClickNoticeReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CLICK_NOTICE_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求首页请求三大数据
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetThreeBigVideoPageDataReqeust(Api.GetThreeBigVideoPageDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_THREE_BIG_VIDEO_PAGE_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求专题
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetSpecialPageDataReqeust(Api.GetSpecialPageDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_SPECIAL_PAGE_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 专题进入专题的更多
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetSpecialMoreVideoReqeust(Api.GetSpecialMoreVideoReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_SPECIAL_MORE_VIDEO_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 点击三大分类的更多
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetThreeBigMoreVideoReqeust(Api.GetThreeBigMoreVideoReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_THREE_BIG_MORE_VIDEO_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求 收藏
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestClickCollectReqeust(Api.ClickCollectReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CLICK_COLLECT_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 删除 收藏
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestDeleteCollectRecrdReqeustReqeust(Api.DeleteCollectRecrdReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_DELETE_COLLECT_RECORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求 充值數據
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetPayDataReqeust(Api.GetPayDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_PAY_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求 兑换卡密
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestExchageKaMiReqeust(Api.ExchageKaMiReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_EXCHAGE_KAMI_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 请求 獲取主播
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetRecommedThrHostReqeust(Api.GetRecommedThrHostReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_RECOMMEND_THE_HOST_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求 直播平台
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetLivePlatformReqeust(Api.GetLivePlatformReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_LIVE_PLATFORM_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 获取任务数据
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyGetTaskRequest(Api.GetTaskReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_TASK_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求领取任务奖励
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyGetTaskAwardRequest(Api.GetTaskAwardReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_TASK_AWARD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求回去推广链接（二维码）
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyGetQrCodeRequest(Api.GetQrCodeReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_QR_CODE_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 请求保存二维码
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodySaveQrCodeRequest(Api.SaveQrCodeReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_SAVE_QR_CODE_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 請求視頻播放
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetpPlayVideoReqeust(Api.GetpPlayVideoReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_PLAY_VIDEO_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 獲取用戶信息
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodySaveApiUserInfoRequest(Api.GetApiUserInfoReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_API_USER_INFO_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 獲取充值記錄信息
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyRechargeRecodeRequest(Api.RechargeRecordReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_RECHARGE_RECORD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 獲取启动页图片信息
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyLauncherImageRequest(Api.GetAdvertisementLoadingImgReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_ADVERTISEMENT_LOADING_IMG_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 上传视频播放时间
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestWatichVideoReqeust(Api.WatichVideoReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_WATCH_VIDEO_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 点赞视频
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestLikeToTrampleReqeust(Api.LikeToTrampleReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_LIKE_TO_TRAMPLE_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 上传直播觀看播放时间
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestWatichLiveReqeust(Api.WatichLiveReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_WATCH_LIVE_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 点击广告
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestBodyClickAdRequest(Api.ClickAdReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_CLICK_AD_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 上传直播觀看播放时间
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetLiveFreeTimeReqeust(Api.GetLiveFreeTimeReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_LIVE_FREE_TIME_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }

    /**
     * 获取筛TAG选数据
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetFilterDataReqeust(Api.GetFilterDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_FILTER_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


    /**
     * 获取筛选的视频数据数据
     * <p>
     * 加密
     *
     * @param request
     * @return
     */
    public static RequestBody requestGetFilterVideoDataReqeust(Api.GetFilterVideoDataReqeust request) {
        BaseMessage.NetMessage enMsg = BaseMessage.NetMessage.newBuilder()
                .setCommand(Command.CmdType.C_S_GET_FILTER_VIDEO_DATA_REQUEST.getNumber())
                .setBody(request.toByteString()).build();

        return encreptionMsg(enMsg);
    }


}
