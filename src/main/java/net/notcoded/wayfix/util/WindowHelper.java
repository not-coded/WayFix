package net.notcoded.wayfix.util;

import net.minecraft.client.MinecraftClient;
import net.notcoded.wayfix.WayFix;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindowHelper {

    public static boolean canUseWindowHelper = false;

    public static void checkIfCanUseWindowHelper() {
        if(!System.getenv("XDG_CURRENT_DESKTOP").contains("KDE")) return; // kdotool only works with KDE and I haven't found an alternative.

        try {
            new ProcessBuilder("kdotool").start();
            canUseWindowHelper = true;
        } catch (IOException ignored) {
            WayFix.LOGGER.warn("WayFix recommends installing 'kdotool' to properly fix the minecraft full-screening functionality.");
        }
    }

    private static String windowID = "";

    public static int[] getWindowPos() {
        if(windowID.trim().isEmpty() && !setWindowID()) return null;

        String[] command = new String[]{"kdotool", "getwindowgeometry", windowID};

        ProcessBuilder builder = new ProcessBuilder(command);

        try {
            Process process = builder.start();
            String result = new String(IOUtils.toByteArray(process.getInputStream()));
            Pattern pattern = Pattern.compile("Position:\\s*(\\d+),(\\d+)");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                return new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))};
            } else {
                return null;
            }
        } catch (IOException ignored) {
            return null;
        }
    }

    public static boolean setWindowID() {
        if(!MinecraftClient.getInstance().isWindowFocused()) return false;
        String[] command = new String[]{"kdotool", "getactivewindow", "getwindowgeometry"};

        ProcessBuilder builder = new ProcessBuilder(command);

        try {
            Process process = builder.start();
            String result = new String(IOUtils.toByteArray(process.getInputStream()));
            Pattern pattern = Pattern.compile("Window \\{(\\w+-\\w+-\\w+-\\w+-\\w+)}");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                windowID = "{" + matcher.group(1) + "}"; // let's just hope this is the minecraft window 🙏
                return true;
            }
            return false;
        } catch (IOException ignored) {
            return false;
        }
    }
}
