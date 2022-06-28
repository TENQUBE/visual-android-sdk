package tenqube.parser.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


class AES128Cipher {

    private static AES128Cipher INSTANCE;

    private Context mContext;
    private String securityKey;
    private String IV = ""; //16bit

    static AES128Cipher getInstance(Context context) {
        synchronized (AES128Cipher.class) {
            if (INSTANCE == null) {
                INSTANCE = new AES128Cipher(context);
            }
            return INSTANCE;
        }
    }


    private AES128Cipher(Context context){

        this.mContext = context;

    }



    //복호화
    String decrypt(String str) {

        if (TextUtils.isEmpty(str))
            return "";

        SecretKeyManager secretKeyManager = SecretKeyManager.getInstance(mContext);

        securityKey = secretKeyManager.getKey().replace("QS","KJ");

        if(TextUtils.isEmpty(securityKey)) { // 키정보 업데이트
            securityKey = PrefUtils.getInstance(mContext).loadStringValue(PrefUtils.SECURITY_KEY, "");
            secretKeyManager.saveKey(securityKey);
        }

        try {
            IV = securityKey.substring(0,16);
        } catch (IndexOutOfBoundsException e) {
            IV = "aaaabbbbccccddddeeee".substring(0, 16);
        }

        String result="";
        try {

            byte[] keyData = securityKey.getBytes();
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));

            byte[] byteStr = Base64.decode(str.getBytes(),  Base64.DEFAULT);

            result= new String(c.doFinal(byteStr),"UTF-8");

        } catch (UnsupportedEncodingException e){

        } catch (NoSuchAlgorithmException e){

        } catch (NoSuchPaddingException e){

        } catch (InvalidKeyException e){

        } catch (InvalidAlgorithmParameterException e){

        } catch (IllegalBlockSizeException e){

        } catch (BadPaddingException e){

        } catch (IllegalArgumentException e) {

        }

        return result;

    }

}