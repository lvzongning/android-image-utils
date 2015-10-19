package com.github.zcwfeng.aiu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextPaint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ==========================================
 * Created by David Zhang on 2015/08/30.
 * Description：
 * Copyright © 2015 张传伟. All rights reserved.
 * Modified by:
 * Modified Content:
 * ==========================================
 */
public class MergeImageUtil {

    /**
     * 用于存储二维码图片的文件路径
     */
    public final static String filePath = getFileRoot() + File.separator
            + "qr_share_" + SystemClock.currentThreadTimeMillis() + ".jpg";


    public static String getGeneratPath() {
        return filePath;
    }

    /**
     * 删除生成的临时文件
     * 吊用此方法,必须确保分享成功
     */
    public static void delTempGeneratePic() {
        FileUtil.delStatisticFile(filePath);
    }

    /**
     * 生成二维码Bitmap
     * 最多是5张图片
     * 图片详情取1张,图册取前五张
     *
     * @param isV      是否是大V
     * @param content  内容
     * @param headIcon 头像
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(String content, ArrayList<Bitmap> shouBitmap, Bitmap headIcon, boolean isV) {

        if (content == null || shouBitmap == null || "".equals(content) || shouBitmap.size() < 1) {
            return false;
        }

        /*
         * 1.判断是几张图片
         * 2.计算图片的高度和每张图片的尺寸画法,一大四小,一大多小,一张大图
         * 3.计算二维码和文字的位置
         * 4.画图
         */
        int marginGap = 5;
        int screenW = CommonUtils.getScreenWidth();
        int screenH = CommonUtils.getScreenHeight();

        int bigImgW = screenW;//大
        int bigImgH = bigImgW * 2 / 3;

        int smallImgW = shouBitmap.size() > 1 ? ((screenW - marginGap * 5) >> 2) : 0;//小
        int smallImgH = smallImgW;
        int smallImgY = bigImgH + 5;

        //90x90
        int headIconX = (screenW - 90) >> 1;
        int headIconY = shouBitmap.size() > 1 ? smallImgY + smallImgH : smallImgY - 55;
        int vX = headIconX + 80;
        int vY = headIconY + 80;

        int qrcodeY = headIconY + 90 + 80;
        int qrcodeH = (screenH - qrcodeY - 150);
        int qrcodeW = qrcodeH;
        int qrcodeX = ((screenW - qrcodeW) >> 1);
        int textY = screenH - 80;


        Bitmap qrcodeBitmap = null;
        Bitmap canvasbitmap = null;
        boolean flag = false;
        try {
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 2); //default is 4


            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = null;
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, qrcodeW, qrcodeW, hints);
            int[] pixels = new int[qrcodeW * qrcodeW];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < qrcodeW; y++) {
                for (int x = 0; x < qrcodeW; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * qrcodeW + x] = 0xff000000;
                    } else {
                        pixels[y * qrcodeW + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            qrcodeBitmap = Bitmap.createBitmap(qrcodeW, qrcodeW, Bitmap.Config.ARGB_8888);
            qrcodeBitmap.setPixels(pixels, 0, qrcodeW, 0, 0, qrcodeW, qrcodeW);


            canvasbitmap = Bitmap.createBitmap(screenW, screenH, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(canvasbitmap);//创建画布
            canvas.drawColor(Color.WHITE);


            canvas.drawBitmap(resizeImage(shouBitmap.get(0), bigImgW, bigImgH), 0, 0, null);//话第一张大图片

            int smallImgX = 0;
            if (shouBitmap.size() > 1) {
                for (int i = 1; i < shouBitmap.size(); i++) {
                    if (i == 1) {
                        smallImgX = smallImgX + 5;
                    } else {
                        smallImgX = smallImgX + 5 + smallImgW;
                    }
                    canvas.drawBitmap(shouBitmap.get(i), smallImgX, smallImgY, null);
                }
            }

            //draw head icon
            canvas.drawBitmap(headIcon, headIconX, headIconY, null);

            //draw v
            if (isV)
                canvas.drawBitmap(BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.about), vX, vY, null);

            //draw qrcode
            canvas.drawBitmap(qrcodeBitmap, qrcodeX, qrcodeY, null);

            //draw text
            TextPaint paint = new TextPaint();
            paint.setColor(Color.DKGRAY);
            paint.setTextSize(40);
            canvas.drawText("加油吧兄弟", 100, textY, paint);


            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

            flag = canvasbitmap != null && canvasbitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return flag;
        } catch (Exception e) {
            return false;
        } finally {
            //release
            if (qrcodeBitmap != null) {
                qrcodeBitmap.recycle();
            }

            if (canvasbitmap != null) {
                canvasbitmap.recycle();
            }
        }

    }


    //使用Bitmap加Matrix来缩放
    private static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    //文件存储根目录
    private static String getFileRoot() {

        try {
            return Environment.getExternalStorageDirectory().getPath();
        } catch (Exception e) {
            return null;
        }


    }
}
