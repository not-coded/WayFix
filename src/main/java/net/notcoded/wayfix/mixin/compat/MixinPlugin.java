package net.notcoded.wayfix.mixin.compat;

//?if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?}
import net.notcoded.wayfix.util.WindowHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        //?if fabric {
        if(mixinClassName.equals("net.notcoded.wayfix.mixin.MonitorFixWindowMixin")
                && FabricLoader.getInstance().isModLoaded("vulkanmod")
        ) {
            WindowHelper.enabled = false;
            return false;
        }
        //?}
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
