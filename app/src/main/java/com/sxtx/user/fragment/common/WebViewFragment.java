package com.sxtx.user.fragment.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.like.base.base.BaseToolbarFragment;
import com.sxtx.user.R;
import com.sxtx.user.util.AndroidBug5497Workaround;
import com.sxtx.user.util.CameraProvider;
import com.sxtx.user.util.FileCompressUtil;
import com.sxtx.user.util.TakephotoAndroid7Bug;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 加载h5显示页面
 * Created by Administrator on 2018/1/1.
 */

public class WebViewFragment extends BaseToolbarFragment {

    private WebView common_webview;

    //参数
    private final static String PARAMS_TITLE = "PARAMS_TITLE";
    private final static String PARAMS_URL = "PARAMS_URL";
    private String mTitle = "";
    private String mUrl = "";
    private ValueCallback<Uri[]> uploadFiles;
    private ValueCallback<Uri> mUploadMessage;
    private Uri cameraUri;
    private static final int CHOOSE_REQUEST_CODE = 0x9001;
    private static final int FILECHOOSER_RESULTCODE = 0x9002;
    private static final int PHOTO_FILE_CHOOSER_RESULT_CODE = 0x9003;


    public static WebViewFragment newIntance(String title, String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(PARAMS_TITLE, title);
        args.putString(PARAMS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TakephotoAndroid7Bug.dealWithAndroid7Bug();
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        showView(MAIN_VIEW);
        common_webview = (WebView) $(R.id.common_webview);
        AndroidBug5497Workaround.assistActivity(getBaseActivity());
        setWebSetting(common_webview);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString(PARAMS_TITLE);
            mUrl = args.getString(PARAMS_URL);
        }
        setToolbarTitle(mTitle);
        common_webview.loadUrl(mUrl);

    }

    @Override
    public Object getResId() {
        return R.layout.fragment_webview_layout;
    }

    private void setWebSetting(WebView webView) {
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //防止調用外部瀏覽器
        common_webview.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        //webview内部调起相机相册
        common_webview.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFile(uploadMsg);
            }

            // For Android > 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                openFile(uploadMsg);
            }

            // Andorid 3.0 +
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFile(uploadMsg);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadFiles = filePathCallback;
                if (fileChooserParams.isCaptureEnabled()) {
                    if (CameraProvider.hasCamera()&&CameraProvider.isCameraCanUse())
                        takePhoto();
                    else
                        showToast("當前設備相機不可用");
                } else {
                    takeAblum();
                }
                return true;
            }
        });
    }



    private void takePhoto() {
        Intent take_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imagePaths = Environment.getExternalStorageDirectory().getPath()
                + "/dabaitu/"
                + (System.currentTimeMillis() + ".jpg");
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(imagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        cameraUri = Uri.fromFile(vFile);
        take_intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(take_intent, PHOTO_FILE_CHOOSER_RESULT_CODE);
    }

    private void openFile(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.putExtra("return-data", true);
        i.setType("image/*");
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(Intent.createChooser(i, "選擇相冊"),
                FILECHOOSER_RESULTCODE);
    }


    private void takeAblum() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.putExtra("return-data", true);
        i.setType("image/*");
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(Intent.createChooser(i, "選擇相冊"),
                CHOOSE_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 5.0适配，主要是因为5.0的返回参数不同。
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage) return;
            if (null == data) {
                receiveNull();
            } else {
                Uri result = data == null || resultCode != Activity.RESULT_OK ? null
                        : data.getData();
                File file = new File(result.getPath());
                String imagePaths = Environment.getExternalStorageDirectory().getPath()
                        + "/dabaitu/"
                        + (System.currentTimeMillis() + ".jpg");
                // 必须确保文件夹路径存在，否则拍照后无法完成回调
                File vFile = new File(imagePaths);
                FileCompressUtil.samplingRateCompress(file.getPath(),vFile,4);
                mUploadMessage.onReceiveValue(Uri.fromFile(vFile));
                mUploadMessage = null;
            }
        } else if (requestCode == CHOOSE_REQUEST_CODE) {
            if (uploadFiles != null && null != data) {
                File file = new File(FileCompressUtil.getRealFilePath(getContext(), data.getData()));
                String imagePaths = Environment.getExternalStorageDirectory().getPath()
                        + "/dabaitu/"
                        + (System.currentTimeMillis() + ".jpg");
                // 必须确保文件夹路径存在，否则拍照后无法完成回调
                File vFile = new File(imagePaths);
                FileCompressUtil.samplingRateCompress(file.getPath(),vFile,4);
                Uri selectedImage = Uri.fromFile(vFile);
                uploadFiles.onReceiveValue(new Uri[]{selectedImage});
                uploadFiles = null;
            } else {
                receiveNull();
            }
        } else if (requestCode == PHOTO_FILE_CHOOSER_RESULT_CODE) {
            //相机返回
            if (uploadFiles != null) {
                Uri uri = null;
                File file = new File(cameraUri.getPath());
                String imagePaths = Environment.getExternalStorageDirectory().getPath()
                        + "/dabaitu/"
                        + (System.currentTimeMillis() + ".jpg");
                // 必须确保文件夹路径存在，否则拍照后无法完成回调
                File vFile = new File(imagePaths);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                //采样率
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
                if (bitmap == null){
                    receiveNull();
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                try {
                    if (vFile.exists()) {
                        vFile.delete();
                    } else {
                        vFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(vFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                uri = Uri.fromFile(vFile);
                uploadFiles.onReceiveValue(new Uri[]{uri});
                uploadFiles = null;
            }else{
                receiveNull();
            }
        }
    }

    private void receiveNull() {
        uploadFiles.onReceiveValue(null);
        uploadFiles = null;
    }
}
