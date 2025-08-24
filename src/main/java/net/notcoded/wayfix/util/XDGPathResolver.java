package net.notcoded.wayfix.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/*
- Credits to moehreag
- https://github.com/moehreag/wayland-fixes
*/

public class XDGPathResolver {

    public static Path getHome(){
        String home = System.getenv().getOrDefault("HOME", System.getProperty("user.home"));
        if (home == null || home.isEmpty()) {
            //throw new IllegalStateException("could not resolve user home");
            return null;
        }
        return Paths.get(home);
    }

    public static Path getUserDataLocation() {
        // Prefer using the home directory over XDG_DATA_HOME because flatpak points to /home/user/.var/app/org.prismlauncher.PrismLauncher/data/ instead
        if(getHome() != null) {
            return getHome().resolve(".local/share/");
        }
        return Paths.get(System.getenv("XDG_DATA_HOME"));
    }
}