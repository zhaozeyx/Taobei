/*
 * 文件名: NearbyFragment
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.nearby;

import android.view.View;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/4/19]
 */
public class NearbyFragment extends BasicTitleBarFragment {
  @Override
  protected void onCreateViewCompleted(View view) {

  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_nearby;
  }

  @Override
  public boolean initializeTitleBar() {
    return super.initializeTitleBar();
  }
}
