/*
 * 文件名: InviteFriendsActivity
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/6/7
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hengrtec.taobei.R;
import com.hengrtec.taobei.net.rpc.service.NetConstant;
import com.hengrtec.taobei.ui.basic.BasicTitleBarActivity;
import com.hengrtec.taobei.utils.ShareUtils;
import java.util.Hashtable;

/**
 * 邀请朋友界面<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/6/7]
 */
public class InviteFriendsActivity extends BasicTitleBarActivity {
  @Bind(R.id.code_bar)
  ImageView codeBarView;

  @Override
  public boolean initializeTitleBar() {
    setMiddleTitle(R.string.activity_invite_friends_title);
    setLeftTitleButton(R.mipmap.icon_title_bar_back, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    return true;
  }

  @Override
  public int getLayoutId() {
    return R.layout.activity_invite_friends;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: add setContentView(...) invocation
    ButterKnife.bind(this);
    createQRImage(NetConstant.URL_DOWNLOAD_APP);
  }

  @OnClick({R.id.btn_share_moments, R.id.btn_invite_friends})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_share_moments:
        ShareUtils.showShareWechatMoments(this, "", "");
        break;
      case R.id.btn_invite_friends:
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "测试文本");
        startActivity(intent);
        break;
    }
  }

  private int QR_WIDTH = 200, QR_HEIGHT = 200;
  Bitmap bitmap_zx;

  public void createQRImage(String url) {
    try {
      //判断URL合法性
      if (url == null || "".equals(url) || url.length() < 1) {
        return;
      }
      Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
      hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
      //图像数据转换，使用了矩阵转换
      BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH,
          QR_HEIGHT, hints);
      int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
      //下面这里按照二维码的算法，逐个生成二维码的图片，
      //两个for循环是图片横列扫描的结果
      for (int y = 0; y < QR_HEIGHT; y++) {
        for (int x = 0; x < QR_WIDTH; x++) {
          if (bitMatrix.get(x, y)) {
            pixels[y * QR_WIDTH + x] = 0xff000000;
          } else {
            pixels[y * QR_WIDTH + x] = 0xffffffff;
          }
        }
      }
      //生成二维码图片的格式，使用ARGB_8888
      bitmap_zx = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
      bitmap_zx.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
      codeBarView.setImageBitmap(bitmap_zx);
    } catch (WriterException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (null != bitmap_zx && !bitmap_zx.isRecycled()) {
      bitmap_zx.recycle();
    }
  }
}
