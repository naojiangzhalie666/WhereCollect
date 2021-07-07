package com.gongwu.wherecollect.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    private static int BUFFER_SIZE = 8192;

    public static String AES256Encode(Context mContext, String uid, String stringToEncode)
            throws NullPointerException {
        if (mContext == null) {
            return null;
        }
        if (stringToEncode.length() == 0 || stringToEncode == null) {
            return null;
        }
        try {
            SecretKeySpec skeySpec = getKey(mContext);
            byte[] data = stringToEncode.getBytes(StandardCharsets.UTF_8);
            String ivStr = uid;
            ivStr = ivStr.substring(0, 16);
            final byte[] iv = ivStr.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            String encrypedValue = Base64.encodeToString(cipher.doFinal(data),
                    Base64.DEFAULT);
            return encrypedValue;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 说明 :将密钥转行成SecretKeySpec格式
     *
     * @return SecretKeySpec格式密钥
     */
    private static SecretKeySpec getKey(Context mContext)
            throws UnsupportedEncodingException {
        // 如果为128将长度改为128即可
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = getBytesFromDat(mContext);
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }

    /**
     * 获取密钥的byte数组
     *
     * @return
     */
    private static byte[] getBytesFromDat(Context context) {
        AssetManager am = context.getAssets();
        InputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] byteArrays = null;
        try {
            fis = am.open("public_key.aes");
            byte[] b = new byte[BUFFER_SIZE];
            int len;
            baos = new ByteArrayOutputStream();
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            byteArrays = baos.toByteArray();
            baos.flush();
            baos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrays;
    }

}
