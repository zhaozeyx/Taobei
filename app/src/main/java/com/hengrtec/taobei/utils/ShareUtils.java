package com.hengrtec.taobei.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import java.io.File;
import java.util.HashMap;

/**
 * Created by jiao on 2016/6/3.
 */
public class ShareUtils {
  //微信朋友圈
  public static void showShareWechatMoments(Context context, String imagepath, String imageurl) {
    ShareSDK.initSDK(context);
    WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
    sp.setTitle("测试的标题");
    sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
    sp.setText("测试的文本");
    //                Log.e("==", ""+childfile.getPath());
    //                        sp.setImagePath(childfile.getPath());
    //                        Bitmap  bm=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    //                        sp.setImageData(bm);
    if (!"".equals(imagepath)) {
      sp.setImagePath(imagepath);
    }
    if (!"".equals(imageurl)) {
      sp.setImageUrl(imageurl);
    }
    sp.setSite("呵呵");
    sp.setSiteUrl("http://www.baidu.com");
    Platform qzone = ShareSDK.getPlatform(context, WechatMoments.NAME);

    qzone.setPlatformActionListener(new PlatformActionListener() {

      @Override public void onError(Platform arg0, int arg1, Throwable arg2) {
        Log.e("share", "onError");
      }

      @Override public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Log.e("share", "onComplete");
      }

      @Override public void onCancel(Platform arg0, int arg1) {
        Log.e("share", "onCancel");
      }
    }); // 设置分享事件回调
    // 执行图文分享
    qzone.share(sp);
  }

  //QQ
  public static void showShareQQ(Context context, String imagepath, String imageurl) {
    ShareSDK.initSDK(context);
    QQ.ShareParams sp = new QQ.ShareParams();
    sp.setTitle("测试的标题");
    sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
    sp.setText("测试的文本");
    //                Log.e("==", ""+childfile.getPath());
    //                        sp.setImagePath(childfile.getPath());
    //                        Bitmap  bm=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    //                        sp.setImageData(bm);

    if (!"".equals(imagepath)) {
      sp.setImagePath(imagepath);
    }
    if (!"".equals(imageurl)) {
      sp.setImageUrl(imageurl);
    }
    sp.setSite("呵呵");
    sp.setSiteUrl("http://www.baidu.com");
    Platform qzone = ShareSDK.getPlatform(context, QQ.NAME);

    qzone.setPlatformActionListener(new PlatformActionListener() {

      @Override public void onError(Platform arg0, int arg1, Throwable arg2) {
        Log.e("share", "onError");
      }

      @Override public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Log.e("share", "onComplete");
      }

      @Override public void onCancel(Platform arg0, int arg1) {
        Log.e("share", "onCancel");
      }
    }); // 设置分享事件回调
    // 执行图文分享
    qzone.share(sp);
  }

  //QQ空间
  public static void showShareQzone(Context context, String imagepath, String imageurl) {
    ShareSDK.initSDK(context);
    QZone.ShareParams sp = new QZone.ShareParams();
    sp.setTitle("测试的标题");
    sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
    sp.setText("测试的文本");
    //                Log.e("==", ""+childfile.getPath());
    //                        sp.setImagePath(childfile.getPath());
    //                        Bitmap  bm=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    //                        sp.setImageData(bm);
    //sp.setImageUrl("http://www.baidu.com/img/bdlogo.gif");
    sp.setImagePath(
        Environment.getExternalStorageDirectory().getPath() + File.separator + "bb.png");
    sp.setSite("呵呵");
    sp.setSiteUrl("http://www.baidu.com");
    Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);

    qzone.setPlatformActionListener(new PlatformActionListener() {

      @Override public void onError(Platform arg0, int arg1, Throwable arg2) {
        Log.e("share", "onError");
      }

      @Override public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Log.e("share", "onComplete");
      }

      @Override public void onCancel(Platform arg0, int arg1) {
        Log.e("share", "onCancel");
      }
    }); // 设置分享事件回调
    // 执行图文分享
    qzone.share(sp);
  }

  //微信朋友
  public static void showShareWechat(Context context, String imagepath, String imageurl) {
    ShareSDK.initSDK(context);
    Wechat.ShareParams sp = new Wechat.ShareParams();
    sp.setTitle("测试的标题");
    sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
    sp.setText("测试的文本");
    //                Log.e("==", ""+childfile.getPath());
    //                        sp.setImagePath(childfile.getPath());
    //                        Bitmap  bm=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    //                        sp.setImageData(bm);
    if (!"".equals(imagepath)) {
      sp.setImagePath(imagepath);
    }
    if (!"".equals(imageurl)) {
      sp.setImageUrl(imageurl);
    }
    sp.setSite("呵呵");
    sp.setSiteUrl("http://www.baidu.com");
    Platform qzone = ShareSDK.getPlatform(context, Wechat.NAME);

    qzone.setPlatformActionListener(new PlatformActionListener() {

      @Override public void onError(Platform arg0, int arg1, Throwable arg2) {
        Log.e("share", "onError");
      }

      @Override public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Log.e("share", "onComplete");
      }

      @Override public void onCancel(Platform arg0, int arg1) {
        Log.e("share", "onCancel");
      }
    }); // 设置分享事件回调
    // 执行图文分享
    qzone.share(sp);
  }

  //新浪微博
  public static void showShareSign(Context context, String imagepath, String imageurl) {
    ShareSDK.initSDK(context);
    SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
    sp.setTitle("测试的标题");
    sp.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
    sp.setText("测试的文本");
    if (!"".equals(imagepath)) {
      sp.setImagePath(imagepath);
    }
    if (!"".equals(imageurl)) {
      sp.setImageUrl(imageurl);
    }
    sp.setSite("呵呵");
    sp.setSiteUrl("http://www.baidu.com");
    Platform qzone = ShareSDK.getPlatform(context, SinaWeibo.NAME);

    qzone.setPlatformActionListener(new PlatformActionListener() {

      @Override public void onError(Platform arg0, int arg1, Throwable arg2) {
        Log.e("share", "onError");
      }

      @Override public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        Log.e("share", "onComplete");
      }

      @Override public void onCancel(Platform arg0, int arg1) {
        Log.e("share", "onCancel");
      }
    }); // 设置分享事件回调
    // 执行图文分享
    qzone.share(sp);
  }
}
