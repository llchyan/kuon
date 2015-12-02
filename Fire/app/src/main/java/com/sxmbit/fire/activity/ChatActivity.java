package com.sxmbit.fire.activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sxmbit.fire.R;
import com.sxmbit.library.chat.Constans;
import com.sxmbit.library.chat.KeyboardStateLayout;
import com.sxmbit.library.chat.MediaUtil;
import com.sxmbit.library.chat.MessageSendView;
import com.sxmbit.library.chat.RecordVoiceView;

import butterknife.Bind;

public class ChatActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.text)
    TextView textView;//textView,mText
    @Bind(R.id.record_state_view)
    RecordVoiceView recordVoiceView;//,mRecordStateView
    @Bind(R.id.chat_editor)
    MessageSendView messageSendView;//mChatEditor
    @Bind(R.id.rootView)
    KeyboardStateLayout mRootView;//

    private Context mContext;
    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final String message_denied = "Permission \\\"%1$s\\\" has been denied.";
    private static final String message_granted = "All permissions have been granted.";
    private long voiceLength;


    @Override
    protected int getContentViewLayoutID()
    {
        mContext = this;
        return R.layout.activity_chat;
    }

    @Override
    protected void initView()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar)
            return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("聊一聊");
//        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction()
//        {
//            @Override
//            public void onGranted()
//            {
//                Toast.makeText(mContext, message_granted, Toast.LENGTH_SHORT).show();
//                recordAudio();
//            }
//
//
//            @Override
//            public void onDenied(String permission)
//            {
//                String message = String.format(Locale.getDefault(), message_denied, permission);
//                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//            }
//        });
        mRootView.requestFocus();
        mRootView.setOnkbdStateListener(state -> {
            switch (state)
            {
                case KeyboardStateLayout.KEYBOARD_STATE_HIDE:
                    //TODO
                    break;
                case KeyboardStateLayout.KEYBOARD_STATE_SHOW:
                    //TODO
                    break;
            }
        });
        setMessageSendView();
    }

    private void setMessageSendView()
    {
        View explandableView = getLayoutInflater().inflate(R.layout.expand_view, null);
        messageSendView.setExpandView(explandableView);
        messageSendView.setOnSendClickListener(view -> {
            String text = view.getEditText();
            //TODO 发送文字消息请求
            messageSendView.clearEditText();
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        });

        messageSendView.setOnExpandListener(new MessageSendView.OnExpandListener()
        {
            @Override
            public void onExpand()
            {
                hideInputMethod(messageSendView);
            }

            @Override
            public void onCollapse()
            {
            }
        });

        recordAudio();

        ImageView takePhotoView = (ImageView) explandableView.findViewById(R.id.chat_editor_more_camera);
        takePhotoView.setOnClickListener(v -> pickImageWithCamera());
        ImageView pickImageView = (ImageView) explandableView.findViewById(R.id.chat_editor_more_gallary);
        pickImageView.setOnClickListener(v -> pickImageSingleFromGallery());
    }

    private void recordAudio()
    {
        messageSendView.setOnRecordListener(new MessageSendView.OnRecordVoiceListener()
        {
            @Override
            public void onStart(MessageSendView view)
            {
//                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(ChatActivity.this,
//                        new String[]{
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.RECORD_AUDIO
//                        }, new PermissionsResultAction()
//                        {
//                            @Override
//                            public void onGranted()
//                            {
//                                Log.i(TAG, "onGranted");
//                                recordVoiceView.setVisibility(View.VISIBLE);
//                                recordVoiceView.setState(false);
//                            }
//
//                            @Override
//                            public void onDenied(String permission)
//                            {
//                                Log.i(TAG, "onDenied: " + permission);
//                                String message = String.format(Locale.getDefault(), message_denied, permission);
//                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
            }

            @Override
            public void onFinish(MessageSendView view, String fileName, boolean cancelled)
            {
                recordVoiceView.setVisibility(View.GONE);
                if (cancelled)
                    return;

                long amrDuration = MediaUtil.getAmrDuration(fileName);
                voiceLength = amrDuration / 1000;
                if (voiceLength < Constans.MIN_CHAT_VOICE_SECONDS)
                    return;
                //                voicePath = fileName;
                //TODO 发送语音消息请求
                messageSendView.clearEditText();
            }

            @Override
            public void onInfo(MessageSendView view, int remainSeconds)
            {
                recordVoiceView.setRemainIcons(remainSeconds);
            }

            @Override
            public void willCancel(boolean willCancel)
            {
                recordVoiceView.setState(willCancel);
            }
        });
    }

    private void pickImageSingleFromGallery()
    {
        //TODO 从相册选择照片
        Toast.makeText(mContext, "choose picture from gallery", Toast.LENGTH_LONG).show();
    }

    private void pickImageWithCamera()
    {
        //TODO 照相
        Toast.makeText(mContext, "take photo with camera", Toast.LENGTH_LONG).show();
    }

    public void hideInputMethod(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed()
    {
        if (messageSendView.isExpanded())
        {
            messageSendView.collapse();
        } else
        {
            super.onBackPressed();
        }
    }
}
