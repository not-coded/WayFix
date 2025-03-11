package net.notcoded.wayfix.platforms;

public interface ModPlatform {
    String getModLoader();
    boolean isModLoaded(String modLoader);
    boolean isDevelopmentEnvironment();
}
