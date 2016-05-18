/*
 * 文件名: BaseAdvPlayFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/17
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.home;

import com.hengrtec.taobei.ui.basic.BasicFragment;

/**
 * 播放基类<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/17]
 */
public abstract class BaseAdvPlayFragment extends BasicFragment {
  private OnPlayingListener mPlayListener;

  protected void notifyPlaying(int playTime) {
    if (null != mPlayListener) {
      mPlayListener.onPlaying(playTime);
    }
  }

  public void setOnPlayingListener(OnPlayingListener listener) {
    mPlayListener = listener;
  }

  public interface OnPlayingListener {
    void onPlaying(int playTime);
  }
}
