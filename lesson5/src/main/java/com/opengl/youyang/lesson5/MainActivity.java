package com.opengl.youyang.lesson5;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 着色器 是 软件与GPU硬件的桥梁，可以通过着色器去控制GPU去进行高性能的绘制。 如果不使用着色器，我们就只能通过cpu去进行渲染，两者的效率天差地别。
 *
 * 这节课开始接触着色器，用着色器 绘制一个普通的顶点Point 1.学会写简单的着色器 2.了解如何加载着色器 3.学会如何在java代码中动态给着色器的属性赋值
 */

public class MainActivity extends AppCompatActivity implements ViewListener {

    GLSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        checkPermission();
        requestCameraPermission();
        view = new GLSurfaceView(this);

        //第一步 检验是否支持opengl 2.0
        final boolean isSuppotES2 = isSupportES2();
        //第二步创建渲染器
        final PreviewGLRender render = new PreviewGLRender(this);
        render.setListener(this);
        if (isSuppotES2) {
            //设定egl版本
            view.setEGLContextClientVersion(2);
            //设置渲染器，设置完这一步之后相当于开启了 另一条渲染线程
            view.setRenderer(render);
            //设置渲染模式 一直渲染
            view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        } else {
            Toast.makeText(this, "该手机不支持opengl es 2.0", Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(view);
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
        }
    }



    private static final int REQUEST_PERMISSION_CAMERA_CODE = 1;
    @TargetApi(Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            int grantResult = grantResults[0];
            boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    /**
     * 检验是否支持opengl 2.0
     */
    private boolean isSupportES2() {
        return ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }

    @Override
    public void requestRender() {
        view.requestRender();
    }
}
