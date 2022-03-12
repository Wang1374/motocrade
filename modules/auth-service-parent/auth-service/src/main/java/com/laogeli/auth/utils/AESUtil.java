package com.laogeli.auth.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

public class AESUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 生成key
     */
//    private static SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(WxProgramPayConfig.KEY).toLowerCase().getBytes(), ALGORITHM);

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
//    public static String encryptData(String data) throws Exception {
//        // 创建密码器
//        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
//        // 初始化
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        return Base64Util.encode(cipher.doFinal(data.getBytes()));
//    }

    /**
     * AES解密
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
//    public static String decryptData(String base64Data) throws Exception {
//        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        return new String(cipher.doFinal(Base64Util.decode(base64Data)));
//    }


    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decrypt(String data, String key, String iv) {
        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (ivByte.length % base != 0) {
                int groups = ivByte.length / base
                        + (ivByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(ivByte, 0, temp, 0, ivByte.length);
                ivByte = temp;
            }
            // 初始化
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return result;
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String wxDecrypt(String encrypted, String sessionKey, String iv) {
        byte[] encrypData = Base64.decodeBase64(encrypted);
        byte[] ivData = Base64.decodeBase64(iv);
        byte[] sKey = Base64.decodeBase64(sessionKey);
        int base = 16;
        if (ivData.length % base != 0) {
            int groups = ivData.length / base
                    + (ivData.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(ivData, 0, temp, 0, ivData.length);
            ivData = temp;
        }
        try {
            String decrypt = decrypt(sKey, ivData, encrypData);
            return decrypt;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
        //解析解密后的字符串
        String resultString = null;
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            resultString = new String(cipher.doFinal(encData), "UTF-8");
        } catch (Exception e) {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            resultString = new String(cipher.doFinal(encData), "UTF-8");
        }
        return resultString;
    }
}
