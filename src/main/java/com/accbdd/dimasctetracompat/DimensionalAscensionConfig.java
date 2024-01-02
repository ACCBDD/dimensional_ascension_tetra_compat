package com.accbdd.dimasctetracompat;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;
import java.util.Arrays;

public class DimensionalAscensionConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLUE_SKIES_MATERIALS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        //todo: actual validator
        BLUE_SKIES_MATERIALS = builder.comment("A list of Tetra material IDs that you would like to have not nerfed in Blue Skies dimensions")
            .defineList("blue_skies_materials", getDefaultMaterials(), (Object test) -> true);

        COMMON_CONFIG = builder.build();
    }

    private static List<String> getDefaultMaterials() {
        List<String> defaults = Arrays.asList("falsite","ventium","horizonite","diopside","charoite","moonstone","pyrope","aquite","turquoise_stone","lunar_stone","bluebright","lunar_wood","frostbright","maple","starlit","dusk","cherry");
        return defaults;
    }
}
