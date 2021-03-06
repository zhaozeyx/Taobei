/*
 * 文件名: VideoPlayFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/22
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.component.log.Logger;
import com.hengrtec.taobei.ui.home.event.PlayCompletedEvent;
import com.hengrtec.taobei.utils.NetworkType;
import com.hengrtec.taobei.utils.NetworkUtil;

/**
 * 视频播放界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/22]
 */
public class VideoPlayFragment extends BaseAdvPlayFragment {
  private static final String TAG = "VideoPlayFragment";
  private static final String BUNDLE_ARG_URL = "url";
  private static final long SPACE = 1000L;

  @Bind(R.id.video_view)
  VideoView mVideoView;
  @Bind(R.id.count_down)
  TextView mCountDownView;

  private int mDuration;
  private CountDownTimer mCountDownTimer;

  private AlertDialog mWifiDialog;
  private int mFinishTime;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_video_play, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  public static VideoPlayFragment newInstance(String videoUrl) {
    VideoPlayFragment fragment = new VideoPlayFragment();
    Bundle args = new Bundle();
    args.putString(BUNDLE_ARG_URL, videoUrl);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (NetworkUtil.getNetworkType(getActivity()) != NetworkType.WIFI) {
      mWifiDialog = new AlertDialog.Builder(getActivity()).setMessage(R.string
          .adv_play_warn_not_wifi)
          .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
              mWifiDialog.cancel();
              initView(getArguments().getString(BUNDLE_ARG_URL));
            }
          }).setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              mWifiDialog.cancel();
              getActivity().finish();
            }
          }).create();
      mWifiDialog.show();
    } else {
      initView(getArguments().getString(BUNDLE_ARG_URL));
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  private void initView(final String url) {
    showProgressDialog("");
    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
        closeProgressDialog();
        mDuration = mediaPlayer.getDuration();
        startPlay();
      }
    });
    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        getComponent().getGlobalBus().post(new PlayCompletedEvent(mediaPlayer.getDuration() /
            1000));
      }
    });
    mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
      @Override
      public boolean onError(MediaPlayer mp, int what, int extra) {
        if(mFinishTime <= 1) {
          getComponent().getGlobalBus().post(new PlayCompletedEvent(mp.getDuration() /
              1000));
          return true;
        }
        return true;
      }
    });
    mVideoView.setVideoURI(Uri.parse(url));
  }

  private void startPlay() {
    mVideoView.start();
    mCountDownTimer = new CountDownTimer(mDuration, SPACE) {
      @Override
      public void onTick(long millisUntilFinished) {
        mFinishTime = (int) (millisUntilFinished / 1000L);
        if (mVideoView.isPlaying()) {
          mCountDownView.setText(getString(R.string.adv_play_count_down, mFinishTime));
          notifyPlaying(mDuration / 1000 - mFinishTime);
        }
      }

      @Override
      public void onFinish() {
        mCountDownView.setVisibility(View.GONE);
      }
    };
    mCountDownTimer.start();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    Logger.d(TAG, "onConfigurationChanged ");
  }

  @Override
  public boolean onBackPressed() {
    if (null != mVideoView && mVideoView.isPlaying()) {
      mVideoView.suspend();
    }
    return super.onBackPressed();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (null != mCountDownTimer) {
      mCountDownTimer.cancel();
    }
  }
}
