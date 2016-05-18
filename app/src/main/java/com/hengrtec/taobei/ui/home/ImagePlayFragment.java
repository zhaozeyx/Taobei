/*
 * 文件名: ImagePlayFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/3
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.utils.imageloader.ImageLoader;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片浏览界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/3]
 */
public class ImagePlayFragment extends BaseAdvPlayFragment {
  private static final String TAG = "ImagePlayFragment";
  private static final String ARGS_KEY_IMG_URI = "img_uri";
  private static final int TIME_LAST = 6 * 1000;
  private static final long TIME_INTERVAL = 1000L;
  private static final float SCALE_SIZE = 2.0f;
  @Bind(R.id.img_view)
  PhotoView mImgView;
  PhotoViewAttacher mAttacher;
  @Bind(R.id.count_down)
  TextView mCountDownView;

  private CountDownTimer mCountDownTimer = new CountDownTimer(TIME_LAST, TIME_INTERVAL) {
    @Override
    public void onTick(long l) {
      int timeLeave = (int) (l / 1000);
      mCountDownView.setText(getString(R.string.adv_play_count_down, l / 1000));
      notifyPlaying(TIME_LAST / 1000 - timeLeave);
    }

    @Override
    public void onFinish() {
      // TODO 关闭吗
      mCountDownView.setVisibility(View.GONE);
    }
  };

  public static ImagePlayFragment newInstance(String uri) {
    ImagePlayFragment fragment = new ImagePlayFragment();
    Bundle ars = new Bundle();
    ars.putString(ARGS_KEY_IMG_URI, uri);
    fragment.setArguments(ars);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_image_play, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mImgView.setZoomable(true);
    mAttacher = new PhotoViewAttacher(mImgView);
    mAttacher.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
      @Override
      public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
      }

      @Override
      public boolean onDoubleTap(MotionEvent motionEvent) {
        if (mAttacher.getScale() > 1.0f) {
          mAttacher.setScale(1.0f, true);
          return false;
        }
        mAttacher.setScale(SCALE_SIZE, motionEvent.getRawX(), motionEvent.getRawY(), true);
        return false;
      }

      @Override
      public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
      }
    });

    String uri = getArguments().getString(ARGS_KEY_IMG_URI);
    showProgressDialog("");
    ImageLoader.loadOptimizedHttpImage(getActivity(), uri).listener(new RequestListener<String,
        GlideDrawable>() {

      @Override
      public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
          isFirstResource) {
        closeProgressDialog();
        return false;
      }

      @Override
      public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable>
          target, boolean isFromMemoryCache, boolean isFirstResource) {
        closeProgressDialog();
        mAttacher.update();
        mCountDownTimer.start();
        return false;
      }
    }).into(mImgView);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    if (null != mCountDownTimer) {
      mCountDownTimer.cancel();
    }
  }
}
