/*
 * 文件名: MD5Encoder
 * 版    权：  Copyright Hengrtech Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:16/5/3
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.hengrtec.taobei.utils;

import android.support.annotation.NonNull;
import com.hengrtec.taobei.component.log.Logger;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密<BR>
 *
 * @author zhaozeyang
 * @version [Taobei Client V20160411, 16/5/3]
 */
public class MD5Encoder {
  private static final String TAG = "MD5Encoder";

  public static String encode(@NonNull String text) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(text.getBytes("UTF-8"));
      byte[] encryption = md5.digest();

      StringBuffer strBuf = new StringBuffer();
      for (int i = 0; i < encryption.length; i++) {
        if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
          strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
        } else {
          strBuf.append(Integer.toHexString(0xff & encryption[i]));
        }
      }
      Logger.d(TAG, "password ===> " + strBuf.toString());

      return strBuf.toString();
    } catch (NoSuchAlgorithmException e) {
      Logger.d(TAG, "", e);
      return "";
    } catch (UnsupportedEncodingException e) {
      Logger.d(TAG, "", e);
      return "";
    }
  }
}
