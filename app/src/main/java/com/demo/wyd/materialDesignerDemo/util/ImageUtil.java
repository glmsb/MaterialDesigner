package com.demo.wyd.materialDesignerDemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * Description :图片压缩工具类
 * Created by wyd on 2016/9/10.
 */
public class ImageUtil {

    /**
     * 通过uri获取图片并进行图片压缩（按比例压缩，采样，有损）
     * <p/>
     * ——适用于取到图片之后先压缩再显示在控件中
     * 个人认为可以用系统的ThumbnailUtils类进行取代
     */
    public static Bitmap sampleSizeBitmap(Context context, Uri picUri, int desWidth, int desHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只读边,不读内容
        InputStream in = context.getContentResolver().openInputStream(picUri);
        BitmapFactory.decodeStream(in, null, options); //返回值为空
        if (in != null)
            in.close();

        //设置缩放比例
        options.inSampleSize = calculateInSameSize(options.outWidth, options.outHeight, desWidth, desHeight);
        options.inJustDecodeBounds = false;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
        in = context.getContentResolver().openInputStream(picUri);
        Bitmap newBitmap = BitmapFactory.decodeStream(in, null, options);
        options.inBitmap = newBitmap; //复用bitmap

        if (in != null)
            in.close();
        return newBitmap;
    }

    /**
     * 计算采样率
     * <p/>
     * 源图片的宽高和目标输出图片的宽高
     * 选择宽高比率最小的作为inSampleSize的值，这样可以保证最终图片的宽和高一定都会大于等于目标的宽和高。
     */
    private static int calculateInSameSize(int origWidth, int origHeight, int desWidth, int desHeight) {
        int inSampleSize = 1;
        if (origWidth > desWidth || origHeight > desHeight) {
            // 计算出源图片宽高和目标宽高的比率
            int widthRatio = Math.round((float) origWidth / (float) desWidth);
            int heightRatio = Math.round((float) origHeight / (float) desHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
            Log.e("widthRatio", widthRatio + "");
            Log.e("heightRatio", heightRatio + "");
            Log.e("inSampleSize", inSampleSize + "");
        }

       /* if (height > desHeight || width > desWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= desHeight && (halfWidth / inSampleSize) >= desWidth) {
                inSampleSize *= 2;
            }
        }*/
        return inSampleSize;
    }

    /**
     * 使用矩阵变换的缩放来压缩图片的宽高
     *
     * @param bitmap    源图片
     * @param desWidth  目标图片的宽
     * @param desHeight 目标图片的高
     * @return 压缩之后的图片
     */
    public static Bitmap reduceWithMatrix(Bitmap bitmap, int desWidth, int desHeight) {
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，
        float sx = new BigDecimal(desWidth).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(desHeight).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        // 大于1表示放大，小于1表示缩小
        // float inSampleSize = Math.min(sx, sy);
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 图片压缩（质量压缩方法——适用于压缩之后上传到服务器）
     * <p/>
     * 它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的。
     * 进过它压缩的图片文件大小会有改变，但是导入成bitmap后占得内存是不变的。
     * 因为要保持像素不变，所以它就无法无限压缩，到达一个值之后就不会继续变小了。
     */
    public static Bitmap compressBitmap(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os); //质量压缩方法,这里100表示不压缩,把压缩后的数据存放到os中
        int options = 100;
        while (os.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            os.reset(); //重置os,即清空os
            //第一个参数:图片格式,第二个参数:图片质量,100为最高，0为最差,第三个参数:保存压缩后的数据的流
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);   //这里压缩(100-options)%，把压缩后的数据存放到os中
            if (options > 10) {
                options -= 10;
            } else {
                options = 80;
            }
        }
        byte[] bytes = os.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }


    /**
     * 通过Uri获取文件
     */
    public static File getFileFromMediaUri(Context context, Uri uri) {
        if (uri.getScheme().compareTo("content") == 0) {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        } else if (uri.getScheme().compareTo("file") == 0) {
            return new File(uri.toString().replace("file://", ""));
        }
        return null;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param fileName 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String fileName) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(fileName);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
}
