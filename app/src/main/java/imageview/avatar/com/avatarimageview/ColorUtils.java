package imageview.avatar.com.avatarimageview;

import android.graphics.Color;

import java.security.SecureRandom;
import java.util.Random;

public class ColorUtils {

    public static int getRandomColor() {
        SecureRandom secureRandom = new SecureRandom();
        return Color.argb(secureRandom.nextInt(256),
                secureRandom.nextInt(256),
                secureRandom.nextInt(256),
                secureRandom.nextInt(256));
    }
}