package com.demo.wyd.materialDesignerDemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.util.ImageUtil;

import java.io.File;
import java.io.IOException;

/**
 * Description :选择照片，处理图片
 * Created by wyd on 2016/7/20.
 */
public class Fragment2 extends Fragment {

    private static final int REQUEST_CODE_CAMERA = 0;
    private static final int REQUEST_CODE_SELECT_PIC = 1;
    private static String SD_PATH = Environment.getExternalStorageDirectory() + "/MD_photo/";

    private View rootView;
    private ImageView imvPic;
    private Uri picUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_2, container, false);
            initView();
        } else {
            ViewGroup p = (ViewGroup) rootView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return rootView;
    }

    private void initView() {
        imvPic = (ImageView) rootView.findViewById(R.id.imv_pic);
        TextView tvSelectPic = (TextView) rootView.findViewById(R.id.tv_select_photo);
        TextView tvTakePic = (TextView) rootView.findViewById(R.id.tv_take_photo);
        tvSelectPic.setOnClickListener(clickListener);
        tvTakePic.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.tv_select_photo: //调用系统相册
//                    Intent.ACTION_PICK
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*"); //指定类型可以是image的任意类型，包括jpg/bmp/gif等等
                    startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
                    break;
                case R.id.tv_take_photo:  //调用系统相机进行拍照
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File folder = new File(SD_PATH);
                    if (!folder.exists()) {//判断是否已存在该文件夹
                        folder.mkdir();
                    }
                    File file = new File(SD_PATH + System.currentTimeMillis() + ".jpg");
                    //如果指定了目标uri，data就没有数据，如果没有指定uri，则data就返回有数据,照相机有自己默认的存储路径，
                    // 拍摄的照片将返回一个缩略图。如果想访问原始图片，可以通过dat extra能够得到原始图片位置.
                    picUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    dealCameraResult();
                    break;
                case REQUEST_CODE_SELECT_PIC:
                    dealSelectResult(data);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 处理选择照片返回的结果
     *
     * @param data 返回的结果数据
     */
    private void dealSelectResult(Intent data) {
        Uri picUri = data.getData();    //picUri格式：content://media/external/images/media/7058

        /** 显示本地图片的方法一 **/
        //imvPic.setImageURI(picUri);   //Uri是一个本地图片路径，无法显示网络图片的

        /** 显示本地图片的方法二 */
        try {
            // 根据统一资源标识符来查找获取bitmap,大小完全由要加载的本地图片的大小决定
            Bitmap srcBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
            Log.e("srcBitmap_Size", srcBitmap.getByteCount() + "byte");
            Log.e("srcBitmap_width", srcBitmap.getWidth() + "");
            Log.e("srcBitmap_height", srcBitmap.getHeight() + "");
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(srcBitmap, imvPic.getWidth(), imvPic.getHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            Log.e("ThumbnailBitmap_Size", bitmap.getByteCount() + "byte");
            Log.e("Bitmap_RowBytes", bitmap.getRowBytes() + "byte");
            Log.e("Bitmap.Config", bitmap.getRowBytes() / bitmap.getWidth() + "byte");
            Log.e("Bitmap_width", bitmap.getWidth() + "");
            Log.e("Bitmap_height", bitmap.getHeight() + "");
            imvPic.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("catch_exception", e.getMessage());
        }
    }

    /**
     * 处理拍照返回之后的结果
     */
    private void dealCameraResult() {
        /** 1、此方法会由Camera直接产生照片回传给应用程序，但是返回的是压缩图片，显示不清晰*/
                  /*if (data.hasExtra("data")){
                      Bitmap thumbnail = data.getParcelableExtra("data");
                    }

                    Bundle bundle = data.getExtras();
                    Bitmap thumbnail = (Bitmap) bundle.get("data");
                    imvPic.setImageBitmap(thumbnail);*/

        /** 2、此方法所拍即所得（sd中保存的是未经过压缩的原图），但是会在Sd卡上产生临时文件*/
        Bitmap smallBitmap = null;
        try {
            smallBitmap = ImageUtil.sampleSizeBitmap(getContext(), picUri, imvPic.getWidth(), imvPic.getHeight());
//            Bitmap srcBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
//            smallBitmap = ImageUtil.reduceWithMatrix(srcBitmap, imvPic.getWidth(), imvPic.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (smallBitmap != null) {
            Log.e("ThumbnailBitmap_Size", smallBitmap.getByteCount() + "byte");
            Log.e("Bitmap.Config", smallBitmap.getConfig() + "");
            Log.e("Bitmap_width", smallBitmap.getWidth() + "");
            Log.e("Bitmap_height", smallBitmap.getHeight() + "");
            imvPic.setImageBitmap(smallBitmap);
        }

        /** 解决拍照后照片保存在文件管理器中但是系统相册不显示问题*/
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, picUri);
        getContext().sendBroadcast(intent);
    }
}
