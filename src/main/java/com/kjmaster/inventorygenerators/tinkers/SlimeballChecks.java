package com.kjmaster.inventorygenerators.tinkers;

import net.minecraft.item.Item;
import slimeknights.tconstruct.shared.TinkerCommons;

public class SlimeballChecks {

    public static boolean isBlueOrOrange(Item item) {
        return item.equals(TinkerCommons.slimedropBlue.getItem()) || item.equals(TinkerCommons.slimedropMagma.getItem());
    }

    public static boolean isPurple(Item item) {
        return item.equals(TinkerCommons.slimedropPurple.getItem());
    }

    public static boolean isBlood(Item item) {
        return item.equals(TinkerCommons.slimedropBlood.getItem());
    }
}
