package com.sxmbit.fire.activity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.TextureView;

import com.sxmbit.fire.R;

import java.io.IOException;

import butterknife.Bind;

public class TextureActivity extends BaseActivity implements TextureView.SurfaceTextureListener
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.texture)
    TextureView mTexture;
    private Camera mCamera;

    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_texture;
    }

    @Override
    protected void initView()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar)
        {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Texture");
        mTexture.setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
    {
        mCamera = Camera.open();

        try
        {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
//            mTexture.setAlpha(1.0f);
            mTexture.setRotation(90.0f);//控件旋转，90.0f代表顺时针旋转90度，因为正常的位图逆时针90度的，ＯＲＺ．．．
        } catch (IOException ioe)
        {
            // Something bad happened
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
    {
        // Ignored, Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
    {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface)
    {
        // Invoked every time there's a new Camera preview frame
    }
}
