package imageview.avatar.com.avatarimageview;

import android.content.Context;

import java.security.SecureRandom;

public final class ColorUtils {

    private Context mContext;

    private ColorUtils(Context context) {
        mContext = context;
    }

    public static ColorUtils getInstance(Context context) {
        return new ColorUtils(context);
    }

    public int getRandomColor() {
        SecureRandom secureRandom = new SecureRandom();

        int[] androidColors = mContext.getResources().getIntArray(R.array.avatarcolors);
        return androidColors[secureRandom.nextInt(androidColors.length)];
    }
}