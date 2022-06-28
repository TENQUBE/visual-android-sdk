package tenqube.parser.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SecretKeyManager {

    SharedPreferences preferences1;
    SharedPreferences preferences2;
    SharedPreferences preferences3;

    private static SecretKeyManager mInstance;

    private Context context;

    public static SecretKeyManager getInstance(Context context){
        synchronized (SecretKeyManager.class) {
            if(mInstance == null){
                mInstance = new SecretKeyManager(context.getApplicationContext());
            }

        }
        return mInstance;
    }

    private SecretKeyManager(Context context) {
        this.context = context;
        preferences1 = context.getSharedPreferences("VisualFile", Context.MODE_PRIVATE);
        preferences2 = context.getSharedPreferences("VisualResource", Context.MODE_PRIVATE);
        preferences3 = context.getSharedPreferences("VisualKey", Context.MODE_PRIVATE);

    }

    public void saveKey(String key) {

        if(TextUtils.isEmpty(key)) return;

        int len = key.length() / 3;

        String first = key.substring(0, len);
        String second = key.substring(len, 2*len);
        String third = key.substring(2*len);


        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.putString("visual", first);
        editor1.apply();

        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.putString("visual", second);
        editor2.apply();

        SharedPreferences.Editor editor3 = preferences3.edit();
        editor3.putString("visual", third);
        editor3.apply();

    }

    public String getKey() {
        return preferences1.getString("visual", "") + preferences2.getString("visual", "") + preferences3.getString("visual", "");
    }


}
