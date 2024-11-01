package net.notcoded.wayfix.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/*
- Credits to moehreag
- https://github.com/moehreag/wayland-fixes
*/

public class XDGPathResolver {

    private static Path getHome(){
        String home = System.getenv().getOrDefault("HOME", System.getProperty("user.home"));
        if (home == null || home.isEmpty()) {
            throw new IllegalStateException("could not resolve user home");
        }
        return Paths.get(home);
    }

    public static Path getUserDataLocation() {
        String xdgDataHome = System.getenv("XDG_DATA_HOME");
        if (xdgDataHome == null || xdgDataHome.isEmpty()) {
            return getHome().resolve(".local/share/");
        }
        return Paths.get(xdgDataHome);
    }
}