package a13070817.ticketmanagementsystem;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Sam on 24/02/2017.
 *
 * http://stackoverflow.com/a/14551476/7087139
 */

public class Font {

    private static final int NUM_OF_CUSTOM_FONTS = 1;

    private static boolean fontLoaded = false;

    private static Typeface[] font = new Typeface[1];

    private static String [] fontPath = {
            "Fonts/Inconsolata.otf"
    };

    public static Typeface getTypeFace(Context context, int fontIdentifier){
        if (!fontLoaded) {
            loadFont(context);
        }
        return font[fontIdentifier];
        }

    public static void loadFont(Context context){
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++){
            font[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontLoaded = true;
    }
}
