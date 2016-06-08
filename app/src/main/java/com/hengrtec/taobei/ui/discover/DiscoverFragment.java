package com.hengrtec.taobei.ui.discover;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.ui.basic.BasicTitleBarFragment;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class DiscoverFragment extends BasicTitleBarFragment {

  @Bind(R.id.info)
  ShimmerTextView mInfoView;

  @Override
  protected void onCreateViewCompleted(View view) {

  }

  @Override
  public int getLayoutId() {
    return R.layout.fragment_discover;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
      savedInstanceState) {
    // TODO: inflate a fragment view
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Shimmer shimmer = new Shimmer();
    shimmer.start(mInfoView);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
