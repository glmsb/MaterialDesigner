package com.demo.wyd.materialDesignerDemo.fragment;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.MediaController;
import android.widget.VideoView;

import com.demo.wyd.materialDesignerDemo.R;

import java.io.File;
import java.io.IOException;

/**
 * Description :
 * Created by wyd on 2016/7/20.
 */
public class Fragment7 extends Fragment implements View.OnClickListener {
    private static String SD_PATH = Environment.getExternalStorageDirectory() + "/MD_video/";

    private View rootView;
    private SurfaceView mSurfaceView;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording;
    private Camera camera;
    private Chronometer chronometer;
    private SurfaceHolder surfaceHolder;
    private VideoView videoView;
    private String videoPath;
    private Button btnPlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_7, container, false);
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
        chronometer = (Chronometer) rootView.findViewById(R.id.chronometer_record);
        FloatingActionButton fabRecord = (FloatingActionButton) rootView.findViewById(R.id.fab_record);
        btnPlay = (Button) rootView.findViewById(R.id.btn_play);
        fabRecord.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        initSurfaceView();
    }

    private void initSurfaceView() {
        mSurfaceView = ((SurfaceView) rootView.findViewById(R.id.sv_video));
        mSurfaceView.setVisibility(View.VISIBLE);
        surfaceHolder = mSurfaceView.getHolder();
        //surfaceHolder.setFixedSize(1080, 1920);
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //在控件创建的时候，进行相应的初始化工作
                surfaceHolder = holder;
                initCamera();
            }

            //当控件发生变化的时候调用，进行surfaceView和camera进行绑定，可以进行画面的显示
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                freeCameraResource();
            }
        });
    }

    public int getDegree() {
        //获取当前屏幕旋转的角度
        int rotating = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        //根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    private void initCamera() {
        if (camera != null) {
            freeCameraResource();
        }
        camera = Camera.open();
        //设置surfaceView旋转的角度，系统默认的录制是横向的画面
        camera.setDisplayOrientation(getDegree());
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
            freeCameraResource();
        }
        camera.startPreview();
        camera.unlock();//解锁
    }

    private void initMediaRecorder() {
        if (mMediaRecorder != null) {
            freeMediaRecorder();
        }
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(camera);//将camera添加到视频录制端口
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        mMediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setOrientationHint(getDegree());//设置播放时的角度
        mMediaRecorder.setMaxDuration(30 * 1000);   //设置记录会话的最大持续时间（毫秒）
        mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        File folder = new File(SD_PATH);
        if (!folder.exists()) {//判断是否已存在该文件夹
            folder.mkdir();
        }
        File file = new File(SD_PATH + System.currentTimeMillis() + ".mp4");
        videoPath = file.getAbsolutePath();
        mMediaRecorder.setOutputFile(videoPath);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            freeMediaRecorder();
        }
        mMediaRecorder.start();
    }

    private void initVideoView() {
        mSurfaceView.setVisibility(View.GONE);
        videoView = (VideoView) rootView.findViewById(R.id.video_view);
        videoView.setVisibility(View.VISIBLE);
        videoView.setKeepScreenOn(true);
        videoView.setVideoPath(videoPath);
        videoView.setMediaController(new MediaController(getContext()));
        videoView.start();
    }

    /**
     * 释放摄像头资源
     */
    private void freeCameraResource() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }

    /**
     * 释放视频录制的资源
     */
    private void freeMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 释放播放视频的资源
     */
    private void freeVideoView() {
        if (videoView != null) {
            videoView.suspend();
            videoView.setVisibility(View.GONE);
            mSurfaceView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_record:
                if (isRecording) {//停止
                    isRecording = false;
                    btnPlay.setVisibility(View.VISIBLE);

                    chronometer.stop();//停止计时
                    chronometer.setVisibility(View.GONE);

                    freeMediaRecorder();
                    return;
                }
                btnPlay.setVisibility(View.GONE);
                if (mSurfaceView.getVisibility() == View.GONE) {
                    initSurfaceView();
                }
                initMediaRecorder();
                isRecording = true;

                chronometer.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());// 将计时器清零
                chronometer.start();

                freeVideoView();
                break;
            case R.id.btn_play:
                initVideoView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        freeMediaRecorder();
        freeCameraResource();
        freeVideoView();
    }
}
