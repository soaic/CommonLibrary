
package com.xiaos.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {
	private static MD5Util md5Util;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	synchronized public static MD5Util getInstance() {
		return md5Util = md5Util==null?new MD5Util():md5Util;
	}
    public String createMd5(String str) {
        MessageDigest mMDigest;
        FileInputStream Input;
        File file = new File(str);
        byte buffer[] = new byte[1024];
        int len;
        if (!file.exists())
            return null;
        try {
            mMDigest = MessageDigest.getInstance("MD5");
            Input = new FileInputStream(file);
            while ((len = Input.read(buffer, 0, 1024)) != -1) {
                mMDigest.update(buffer, 0, len);
            }
            Input.close();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        BigInteger mBInteger = new BigInteger(1, mMDigest.digest());
        return mBInteger.toString(16);

    }

    public boolean checkMd5(String Md5, String file) {
        String str = createMd5(file);
        if(Md5.length()== str.length() + 1){
         str = "0" + str;
        }
        if(Md5.compareTo(str) == 0){
            return true;
        }else{
            return false;
        }
    }
}
