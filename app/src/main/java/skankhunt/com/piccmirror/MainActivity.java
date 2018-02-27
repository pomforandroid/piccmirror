package skankhunt.com.piccmirror;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import skankhunt.com.piccmirror.oss.ImageService;
import skankhunt.com.piccmirror.oss.OssService;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import skankhunt.com.piccmirror.oss.*;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    //private static final String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    //private static final String imgEndpoint = "http://img-cn-hangzhou.aliyuncs.com";
    private static final String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String imgEndpoint = "http://img-cn-hangzhou.aliyuncs.com";
    private static final String callbackAddress = "http://oss-demo.aliyuncs.com:23450";
    private static final String bucket = "";
    private String region = "杭州";

    /*
    private static final String accessKeyId = "******";
    private static final String accessKeySecret = "*******";*/

    //负责所有的界面更新
    private UIDisplayer UIDisplayer;

    //OSS的上传下载
    private OssService ossService;
    private ImageService imageService;
    private String picturePath = "";
    private WeakReference<PauseableUploadTask> task;

    private static final int RESULT_LOAD_IMAGE = 1;

    //初始化一个OssService用来上传下载
    public OssService initOSS(String endpoint, String bucket, UIDisplayer displayer) {
        //如果希望直接使用accessKey来访问的时候，可以直接使用OSSPlainTextAKSKCredentialProvider来鉴权。
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        OSSCredentialProvider credentialProvider;
        //使用自己的获取STSToken的类
        String stsServer = ((EditText) findViewById(R.id.stsserver)).getText().toString();
        if (stsServer .equals("")) {
            credentialProvider = new STSGetter();
        }else {
            credentialProvider = new STSGetter(stsServer);
        }

        bucket = ((EditText) findViewById(R.id.bucketname)).getText().toString();
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
        return new OssService(oss, bucket, displayer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ProgressBar bar = (ProgressBar) findViewById(R.id.bar);
        TextView textView =(TextView) findViewById(R.id.output_info);

        UIDisplayer = new UIDisplayer(imageView, bar, textView, this);
        ossService = initOSS(endpoint, bucket, UIDisplayer);
        //设置上传的callback地址，目前暂时只支持putObject的回调
        ossService.setCallbackAddress(callbackAddress);

        //图片服务和OSS使用不同的endpoint，但是可以共用SDK，因此只需要初始化不同endpoint的OssService即可
        imageService = new ImageService(initOSS(imgEndpoint, bucket, UIDisplayer));

        //从系统相册选择图片
        Button select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Button upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edit_text);
                String objectName = editText.getText().toString();

                ossService.asyncPutImage(objectName, picturePath);

            }
        });

        Button setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                EditText editText = (EditText) findViewById(R.id.edit_text);
                String objectName = editText.getText().toString();

                ossService.asyncPutImage(objectName, picturePath);
                */
                String bucketName = ((EditText)findViewById(R.id.bucketname)).getText().toString();
                //String cardNumber = Activity.this.getResources().getStringArray(R.array.debitCardNumber)[arg2];
                //String endpoint = ((EditText) findViewById(R.id)).getText().toString();
                ossService.SetBucketName(bucketName);
                String newOssEndpoint = GetOssEndpoint();
                String newImageEndpoint = GetImgEndpoint();
                //GetEndpoint(newOssEndpoint, newImageEndpoint);
                //GetEndpoint(newOssEndpoint, newImageEndpoint);
                Log.d(newOssEndpoint, newImageEndpoint);
                //return;
                //new AlertDialog.Builder(MainActivity.this).setTitle("错误3").setMessage(newOssEndpoint).show();
                //return;

                //ossService = initOSS(endpoint, bucketName, UIDisplayer);
                //设置上传的callback地址，目前暂时只支持putObject的回调
                //ossService.setCallbackAddress(callbackAddress);

                {
                    OSSCredentialProvider credentialProvider;
                    //使用自己的获取STSToken的类
                    String stsServer = ((EditText) findViewById(R.id.stsserver)).getText().toString();
                    if (stsServer.equals("")) {
                        credentialProvider = new STSGetter();
                    } else {
                        credentialProvider = new STSGetter(stsServer);
                    }

                    ClientConfiguration conf = new ClientConfiguration();
                    conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
                    conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
                    conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
                    conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                    OSS oss = new OSSClient(getApplicationContext(), newOssEndpoint, credentialProvider, conf);
                    imageService = new ImageService(initOSS(newImageEndpoint, bucketName, UIDisplayer));
                    ossService.InitOss(oss);
                }

                UIDisplayer.settingOK();

            }
        });

        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edit_text);
                String objectName = editText.getText().toString();
                ossService.asyncGetImage(objectName);
            }
        });

        Button multipart_upload = (Button) findViewById(R.id.multipart_upload);
        multipart_upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //为了简单化，这里只会同时运行一个断点上传的任务
                if ((task == null) || (task.get() == null)){
                    Log.d("MultiPartUpload", "Start");
                    EditText editText = (EditText) findViewById(R.id.edit_text);
                    String objectName = editText.getText().toString();
                    task = new WeakReference<>(ossService.asyncMultiPartUpload(objectName, picturePath));
                }
                else {
                    Log.d("MultiPartUpload", "AlreadyRunning");
                }

            }
        });

        Button multipart_pause = (Button) findViewById(R.id.multipart_pause);
        multipart_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task != null) {
                    final PauseableUploadTask uploadTask = task.get();
                    if (uploadTask != null) {
                        Log.d("Pause", "Task");
                        uploadTask.pause();
                    }
                    else{
                        Log.d("Pause", "AlreadyFinishTask");
                    }
                    task = null;
                }
                else {
                    Log.d("MultiPartUpload", "NotExist");
                }
            }
        });

        Button watermark = (Button) findViewById(R.id.watermark);
        watermark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edit_text);
                String objectName = editText.getText().toString();

                String text = ((EditText) findViewById(R.id.watermark_text)).getText().toString();
                try {
                    int size = Integer.valueOf(((EditText) findViewById(R.id.watermark_size)).getText().toString());
                    if (!text.equals("")) {
                        imageService.textWatermark(objectName, text, size);
                    }
                }
                catch (NumberFormatException e) {
                    new AlertDialog.Builder(MainActivity.this).setTitle("错误").setMessage(e.toString()).show();
                }


            }
        });

        Button resize = (Button) findViewById(R.id.resize);
        resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edit_text);
                String objectName = editText.getText().toString();

                try {
                    int width = Integer.valueOf(((EditText) findViewById(R.id.resize_width)).getText().toString());
                    int height = Integer.valueOf(((EditText) findViewById(R.id.resize_height)).getText().toString());
                    imageService.resize(objectName, width, height);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    new AlertDialog.Builder(MainActivity.this).setTitle("错误").setMessage(e.toString()).show();
                }

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.region);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] regions = getResources().getStringArray(R.array.bucketregion);
                region = regions[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.d("PickPicture", picturePath);
            cursor.close();

            try {
                Bitmap bm = UIDisplayer.autoResizeFromLocalFile(picturePath);
                UIDisplayer.displayImage(bm);
                /*
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bm);*/
                File file = new File(picturePath);

                UIDisplayer.displayInfo("文件: " + picturePath + "\n大小: " + String.valueOf(file.length()));
            }
            catch (IOException e) {
                e.printStackTrace();
                UIDisplayer.displayInfo(e.toString());
            }

        }
    }

    protected  String  GetOssEndpoint(){
        String ossEndpoint = "";
        if (region.equals("杭州")) {
            ossEndpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        }
        else if (region.equals("青岛")) {
            ossEndpoint = "http://oss-cn-qingdao.aliyuncs.com";
        }
        else if (region.equals("北京")) {
            ossEndpoint = "http://oss-cn-beijing.aliyuncs.com";
        }
        else if (region.equals("深圳")) {
            ossEndpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        }
        else if (region.equals("美国")) {
            ossEndpoint = "http://oss-us-west-1.aliyuncs.com";
        }
        else if (region.equals("上海")) {
            ossEndpoint = "http://oss-cn-shanghai.aliyuncs.com";
        }
        else {
            new AlertDialog.Builder(MainActivity.this).setTitle("错误的区域").setMessage(region).show();
        }
        return ossEndpoint;
    }

    protected  String GetImgEndpoint(){
        String imgEndpoint = "";
        if (region.equals("杭州")) {
            imgEndpoint = "http://img-cn-hangzhou.aliyuncs.com";
        }
        else if (region.equals("青岛")) {
            imgEndpoint = "http://img-cn-qingdao.aliyuncs.com";
        }
        else if (region.equals("北京")) {
            imgEndpoint = "http://img-cn-beijing.aliyuncs.com";
        }
        else if (region.equals("深圳")) {
            imgEndpoint = "http://img-cn-shenzhen.aliyuncs.com";
        }
        else if (region.equals("美国")) {
            imgEndpoint = "http://img-us-west-1.aliyuncs.com";
        }
        else if (region.equals("上海")) {
            imgEndpoint = "http://img-cn-shanghai.aliyuncs.com";
        }
        else {
            new AlertDialog.Builder(MainActivity.this).setTitle("错误的区域").setMessage(region).show();
            imgEndpoint = "";
        }
        return imgEndpoint;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }
}
