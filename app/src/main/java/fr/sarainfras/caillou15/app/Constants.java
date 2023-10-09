package fr.sarainfras.caillou15.app;

import java.awt.*;

public class Constants {
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 0;
    public static final int REVISION_VERSION = 1;

    public static final Color BACKGROUND_GREEN = new Color(0,210,0);

    private Constants() {}

    public static String getVersionString() {
        return MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
    }
}
