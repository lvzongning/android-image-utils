package com.github.zcwfeng.aiu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * ==========================================
 * Created by David Zhang on 2015/08/30.
 * Description：
 * Copyright © 2015 张传伟. All rights reserved.
 * Modified by:
 * Modified Content:
 * ==========================================
 */
public class ImageToolsTestActivity extends Activity{
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_img);
        imageView = (ImageView) findViewById(R.id.create_qr_iv);

        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(() -> {

            String url = "https://m.baidu.com/";
            ArrayList<Bitmap> bitmapArrayList = new ArrayList<Bitmap>();
            Bitmap test = BitmapFactory.decodeResource(getResources(), R.mipmap.demo);
            bitmapArrayList.add(test);
//                bitmapArrayList.add(test);
//                bitmapArrayList.add(test);
//                bitmapArrayList.add(test);
//                bitmapArrayList.add(test);
            Bitmap head = BitmapFactory.decodeResource(getResources(), R.mipmap.edit);

            boolean success = MergeImageUtil.createQRImage(url, bitmapArrayList, head, true);
            if (success) {
                runOnUiThread(() -> imageView.setImageBitmap(BitmapFactory.decodeFile(MergeImageUtil.getGeneratPath())));
            }
        }).start();
    }
}
