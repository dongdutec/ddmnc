package com.dongdutec.ddmnc.ui.my.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.db.DbConfig;
import com.dongdutec.ddmnc.http.HtmlUrls;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.dongdutec.ddmnc.web.WebsActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import rx.functions.Action1;

public class MyCodeActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;
    private View v_line;


    private ImageView image_userimage;
    private TextView tv_username;
    private ImageView image_code;
    private LinearLayout ll_code;
    private TextView tv_saveimage;
    private TextView tv_check;
    private String userName;
    private String userImage;
    private String codeContent;

    private static final int IMAGE_HALFWIDTH = 18;//宽度值，影响中间图片大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userImage = intent.getStringExtra("userImage");
        codeContent = HtmlUrls.getMyCode() + "?token=" + new DbConfig(MyCodeActivity.this).getToken() + "&yz=MNCDD";


        initView();
        init();
        bindView();
    }

    @Override
    protected void initView() {
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);
        v_line = findViewById(R.id.v_line);


        ll_code = findViewById(R.id.ll_code);
        image_code = findViewById(R.id.image_code);
        tv_username = findViewById(R.id.tv_username);
        image_userimage = findViewById(R.id.image_userimage);
        tv_saveimage = findViewById(R.id.tv_saveimage);
        tv_check = findViewById(R.id.tv_check);


        bar_left_img.setImageResource(R.mipmap.mycode_back);
        bar_title_text.setTextColor(getResources().getColor(R.color.theme_primary));
        v_line.setVisibility(View.GONE);
        bar_title_text.setText("我的专属码");
        //设置头像昵称
        CircleCrop transformation = new CircleCrop();
        RequestOptions requestOptions = RequestOptions.bitmapTransform(transformation);
        Glide.with(MyCodeActivity.this).load(userImage)
                .placeholder(R.mipmap.mycode_image)
                .apply(requestOptions)
                .into(image_userimage);
        tv_username.setText(userName);
    }

    @Override
    protected void init() {
        //调用方法createCode生成二维码
        Bitmap qrBitmap = generateBitmap(codeContent, 200, 200);
        //将二维码在界面中显示
        image_code.setImageBitmap(qrBitmap);
    }

    /**
     * 生成二维码
     *
     * @param string  二维码中包含的文本信息
     * @param mBitmap logo图片
     * @param format  编码格式
     *                [url=home.php?mod=space&uid=309376]@return[/url] Bitmap 位图
     * @throws WriterException
     */
    public Bitmap createCode(String string, Bitmap mBitmap, BarcodeFormat format)
            throws WriterException {
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH
                / mBitmap.getHeight();
        m.setScale(sx, sy);//设置缩放信息
        //将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
        BitMatrix matrix = writer.encode(string, format, 300, 300, hst);//生成二维码矩阵信息
        int width = matrix.getWidth();//矩阵高度
        int height = matrix.getHeight();//矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {//从行开始迭代矩阵
            for (int x = 0; x < width; x++) {//迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
                    //记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW
                            + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {//如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;//记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /*
     * 不带logo()
     * */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void bindView() {
        //生成海报
        RxViewAction.clickNoDouble(tv_saveimage).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //设置缓存
                ll_code.setDrawingCacheEnabled(true);
                ll_code.buildDrawingCache();
                //保存控件截图
                Bitmap drawingCache = ll_code.getDrawingCache();

                try {
                    File file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + Environment.DIRECTORY_DCIM
                            + File.separator + "Camera" + File.separator + "/" + "MNC个人海报" + new DbConfig(MyCodeActivity.this).getUser().getNick() + ".png");
                    FileOutputStream out = new FileOutputStream(file);
                    drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Toast.makeText(MyCodeActivity.this, "个人海报已保存至手机相册!", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //查看下级
        RxViewAction.clickNoDouble(tv_check).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(MyCodeActivity.this, WebsActivity.class);
                intent.putExtra("title", "查看下级");
                intent.putExtra("webUrl", HtmlUrls.getCheck() + "?token=" + new DbConfig(MyCodeActivity.this).getToken());
                startActivity(intent);
            }
        });
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
    }
}
