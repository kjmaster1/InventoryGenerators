package com.kjmaster.inventorygenerators.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.items.InitItems;
import de.ellpeck.actuallyadditions.mod.items.base.ItemSeed;
import de.ellpeck.actuallyadditions.mod.items.metalists.TheFoods;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RiceBallCheck {

    public static boolean isRiceball(Item item) {
        Item riceBall = new ItemStack(InitItems.itemFoods, TheFoods.RICE.ordinal()).getItem();
        return item.equals(riceBall);
    }
}
