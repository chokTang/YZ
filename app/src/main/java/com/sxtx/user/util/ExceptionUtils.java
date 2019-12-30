package com.sxtx.user.util;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.cmd.Command;
import com.lyh.protocol.login.Api;

import org.greenrobot.eventbus.EventBus;

import event.HeartServiceEvents;

public class ExceptionUtils {


    /**
     * 状态检测
     */

    public static boolean checkStates(Object classOfT, BaseMessage.NetMessage netMessage) {
        try {


            if (netMessage.getCommand()== Command.CmdType.S_C_BREAK_LINE_RESPONSE.getNumber()){
                EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_BREAK_LINE_RESPONSE, null));
               return false;
            }
            if (classOfT instanceof Api.ClickSearchResponse) {
                Api.ClickSearchResponse response = ((Api.ClickSearchResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.ClickRemoveRecordResponse) {
                Api.ClickRemoveRecordResponse response = ((Api.ClickRemoveRecordResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.SureSearchKeyWordResponse) {
                Api.SureSearchKeyWordResponse response = ((Api.SureSearchKeyWordResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.ClickCollectResponse) {
                Api.ClickCollectResponse response = ((Api.ClickCollectResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.CheckCollectRecrdResponse) {
                Api.CheckCollectRecrdResponse response = ((Api.CheckCollectRecrdResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.ClickCollectResponse) {
                Api.ClickCollectResponse response = ((Api.ClickCollectResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.DeleteCollectRecrdResponse) {
                Api.DeleteCollectRecrdResponse response = ((Api.DeleteCollectRecrdResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.CheckHistroyRecrdResponse) {
                Api.CheckHistroyRecrdResponse response = ((Api.CheckHistroyRecrdResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.DeleteHistroyRecrdResponse) {
                Api.DeleteHistroyRecrdResponse response = ((Api.DeleteHistroyRecrdResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.ClickMyMessageResponse) {
                Api.ClickMyMessageResponse response = ((Api.ClickMyMessageResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null && response.getResult().getResult() == 0) {//验证成功
                    EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN, null));
                }
            } else if (classOfT instanceof Api.GetNewestPageDataResponse) {
                Api.GetNewestPageDataResponse response = ((Api.GetNewestPageDataResponse) classOfT).parseFrom(netMessage.getBody());
              //   EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN, null));
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.InviteCodeResponse) {
                Api.InviteCodeResponse response = ((Api.InviteCodeResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.AutoPlayResponse) {
                Api.AutoPlayResponse response = ((Api.AutoPlayResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null && response.getResult().getResult() == 0) {//验证成功
                    EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN, null));
                }
            } else if (classOfT instanceof Api.ModifyAutoPlayResponse) {
                Api.ModifyAutoPlayResponse response = ((Api.ModifyAutoPlayResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetRecommendPageDataResponse) {
                Api.GetRecommendPageDataResponse response = ((Api.GetRecommendPageDataResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetRecommendInABatchResponse) {
                Api.GetRecommendInABatchResponse response = ((Api.GetRecommendInABatchResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetTrumpetResponse) {
                Api.GetTrumpetResponse response = ((Api.GetTrumpetResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetThreeBigVideoPageDataResponse) {
                Api.GetThreeBigVideoPageDataResponse response = ((Api.GetThreeBigVideoPageDataResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetSpecialPageDataResponse) {
                Api.GetSpecialPageDataResponse response = ((Api.GetSpecialPageDataResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetSpecialMoreVideoResponse) {
                Api.GetSpecialMoreVideoResponse response = ((Api.GetSpecialMoreVideoResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetThreeBigMoreVideoResponse) {
                Api.GetThreeBigMoreVideoResponse response = ((Api.GetThreeBigMoreVideoResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null && response.getResult().getResult() == 0) {//验证成功
                    EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN, null));
                }
            } else if (classOfT instanceof Api.GetPayDataResponse) {
                Api.GetPayDataResponse response = ((Api.GetPayDataResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.ExchageKaMiResponse) {
                Api.ExchageKaMiResponse response = ((Api.ExchageKaMiResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetTaskResponse) {
                Api.GetTaskResponse response = ((Api.GetTaskResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetTaskAwardResponse) {
                Api.GetTaskAwardResponse response = ((Api.GetTaskAwardResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetQrCodeResponse) {
                Api.GetQrCodeResponse response = ((Api.GetQrCodeResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.SaveQrCodeResponse) {
                Api.SaveQrCodeResponse response = ((Api.SaveQrCodeResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetPlayVideoResponse) {
                Api.GetPlayVideoResponse response = ((Api.GetPlayVideoResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetApiUserInfoResponse) {
                Api.GetApiUserInfoResponse response = ((Api.GetApiUserInfoResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.RechargeRecordResponse) {
                Api.RechargeRecordResponse response = ((Api.RechargeRecordResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.WatichVideoResponse) {
                Api.WatichVideoResponse response = ((Api.WatichVideoResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.ClickAdResponse) {
                Api.ClickAdResponse response = ((Api.ClickAdResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetRecommedThrHostResponse) {
                Api.GetRecommedThrHostResponse response = ((Api.GetRecommedThrHostResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.GetLivePlatformResponse) {
                Api.GetLivePlatformResponse response = ((Api.GetLivePlatformResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            } else if (classOfT instanceof Api.WatichLiveResponse) {
                Api.WatichLiveResponse response = ((Api.WatichLiveResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }else if (classOfT instanceof Api.MobileCodeResponse) {
                Api.MobileCodeResponse response = ((Api.MobileCodeResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }else if (classOfT instanceof Api.GetLiveFreeTimeResponse) {
                Api.GetLiveFreeTimeResponse response = ((Api.GetLiveFreeTimeResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }else if (classOfT instanceof Api.LikeToTrampleResponse) {
                Api.LikeToTrampleResponse response = ((Api.LikeToTrampleResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }else if (classOfT instanceof Api.GetFilterDataResponse) {
                Api.GetFilterDataResponse response = ((Api.GetFilterDataResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }else if (classOfT instanceof Api.GetFilterVideoDataResponse) {
                Api.GetFilterVideoDataResponse response = ((Api.GetFilterVideoDataResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }else if (classOfT instanceof Api.GetRrCodeRetrievePasswordResponse) {
                Api.GetRrCodeRetrievePasswordResponse response = ((Api.GetRrCodeRetrievePasswordResponse) classOfT).parseFrom(netMessage.getBody());
                if (response.getResult() != null) {//验证成功
                    ckeckResult(response.getResult().getResult());
                }
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void ckeckResult(int result){
        if (result == 0) {
            EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN, null));
        }
    }
}
