package com.sxtx.user.fragment.account;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.MultiFormatReader;
import com.like.base.base.BaseStatusToolbarFragment;
import com.like.base.base.rxjava.CommonEvent;
import com.like.base.widget.ClearEditText;
import com.like.utilslib.other.LogUtil;
import com.like.utilslib.other.RegexUtil;
import com.sxtx.user.R;
import com.sxtx.user.dialog.ButtomDialogView;
import com.sxtx.user.mvp.presenter.account.ForgetPasswordPresenter;
import com.sxtx.user.mvp.view.account.IForgetPasswordView;
import com.sxtx.user.util.FileCompressUtil;
import com.sxtx.user.util.NetWorkUtils;
import com.sxtx.user.util.zxing.android.CaptureActivity;
import com.sxtx.user.util.zxing.decode.QRCodeDecoder;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import event.HeartServiceEvents;


public class ForgetPasswordFragment extends BaseStatusToolbarFragment<ForgetPasswordPresenter> implements IForgetPasswordView, View.OnClickListener {
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int FILECHOOSER_RESULTCODE = 0x9002;

    TextView tv_login;
    TextView tv_modify;
    TextView tv_find_proof;


    ClearEditText edt_account;
    EditText edt_psd;

    MultiFormatReader multiFormatReader;



    public static ForgetPasswordFragment newInstance(String mobile) {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        Bundle args = new Bundle();
        args.putString("mobile", mobile);
        fragment.setArguments(args);
        return fragment;
    }

    String mobile="";
    @Override
    public Integer getMainResId() {
        return R.layout.fragment_forget_password;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        setToolbarTitle("賬號找回");
        Bundle bundle=getArguments();
        mobile=bundle.getString("mobile");

        tv_login = $(R.id.tv_login);
        tv_modify=$(R.id.tv_modify);
//        tv_register = $(R.id.tv_register);

        edt_account = $(R.id.edt_account);

        edt_psd = $(R.id.edt_psd);
       tv_find_proof=$(R.id.tv_find_proof);

        multiFormatReader = new MultiFormatReader();

        showView(MAIN_VIEW);

        tv_login.setOnClickListener(this);
        tv_modify.setOnClickListener(this);
       tv_find_proof.setOnClickListener(this);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onLoadData() {

    }



    @Override
    public void loginSucc() {
        showToast("找回成功");
        EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN,null));
        EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_REFRESH_DELI,null));
        pop();
    }


    @Override
    public void handleEvent(CommonEvent event) {
    }


    @Override
    public boolean isRxBus() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login://賬號切換
                if (TextUtils.isEmpty(edt_account.getText().toString().trim())){
                    edt_account.requestFocus();
                    edt_account.setSelection(edt_account.getText().length());
                    showToast("請輸入手機號");
                    return;
                }
                if (!RegexUtil.checkMobile(edt_account.getText().toString().trim())){
                    edt_account.requestFocus();
                    edt_account.setSelection(edt_account.getText().length());
                    showToast("請輸入正確的手機號");
                    return;
                }
                if (TextUtils.isEmpty(edt_psd.getText().toString().trim())){
                    edt_psd.requestFocus();
                    edt_psd.setSelection(edt_psd.getText().length());
                    showToast("請輸入密碼");
                    return;
                }
                if (TextUtils.equals(mobile,edt_account.getText().toString().trim())){
                    showToast("不可使用當前賬號");
                    return;
                }

                if (NetWorkUtils.isNetConnect(getActivity())) mPresenter.getMyTopAcount(edt_account.getText().toString().trim(),edt_psd.getText().toString().trim());
                else showToast("请检查网络连接");
                break;
            case R.id.tv_modify:
                start(ModifyPasswordFragment.newInstance());
                break;
            case R.id.tv_find_proof:
                showBottomDialog();
                break;
       }
    }
  public void goScan(){
      Intent intent = new Intent(getContext(), CaptureActivity.class);
      startActivityForResult(intent, REQUEST_CODE_SCAN);
  }


    private void showBottomDialog() {
        ButtomDialogView.newInstance(getContext())
                .setClickListen(new ButtomDialogView.TwoSelDialog() {
                    @Override
                    public void topClick() {
                        //动态权限申请
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                        } else {
                            goScan();
                        }
                    }

                    @Override
                    public void bottomClick() {
                        openFile();
                    }
                }).show();
    }

    private void openFile() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.putExtra("return-data", true);
        i.setType("image/*");
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(Intent.createChooser(i, "選擇相冊"),
                FILECHOOSER_RESULTCODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    Toast.makeText(getContext(), "你拒绝了权限申请，可能无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (data == null) return;
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
              //  Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                LogUtil.loge("你扫描到的内容是：" + content);
                mPresenter.setAcountKey(content);
            }
        }else     if (requestCode == FILECHOOSER_RESULTCODE) {
            //获取选取的图片的绝对地址
            String photoPath = FileCompressUtil.getRealFilePath(getContext(), data.getData());
            if (photoPath == null) {
                showToast("訪問出錯");
            } else {
                //解析图片
                parsePhoto(photoPath);
            }
        }
    }

    /**
     * 启动线程解析二维码图片
     *
     * @param path
     */
    private void parsePhoto(String path) {
        //启动线程完成图片扫码
        new QrCodeAsyncTask(_mActivity, path).execute(path);
    }

    class QrCodeAsyncTask extends AsyncTask<String, Integer, String> {
        private WeakReference<Activity> mWeakReference;
        private String path;

        public QrCodeAsyncTask(Activity activity, String path) {
            mWeakReference = new WeakReference<>(activity);
            this.path = path;
        }

        @Override
        protected String doInBackground(String... strings) {
            // 解析二维码/条码
            return QRCodeDecoder.syncDecodeQRCode(path);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //识别出图片二维码/条码，内容为s
            LogUtil.loge("掃描結果："+s);
            mPresenter.setAcountKey(s);

        }
    }
}
