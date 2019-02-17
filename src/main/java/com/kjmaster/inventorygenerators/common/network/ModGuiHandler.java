package com.kjmaster.inventorygenerators.common.network;

import com.kjmaster.inventorygenerators.client.gui.generator.ContainerGenerator;
import com.kjmaster.inventorygenerators.client.gui.generator.GuiGenerator;
import com.kjmaster.inventorygenerators.client.gui.generator.InventoryGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler {

    public static final int culinary = 0;
    public static final int death = 1;
    public static final int end = 2;
    public static final int explosive = 3;
    public static final int frosty = 4;
    public static final int furnace = 5;
    public static final int halitosis = 6;
    public static final int magmatic = 7;
    public static final int netherstar = 8;
    public static final int overclocked = 9;
    public static final int pink = 10;
    public static final int potion = 11;
    public static final int slimey = 12;
    public static final int survivalist = 13;

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack stack = player.getHeldItemMainhand();
        switch (ID) {
            case culinary:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "culinary");
            case death:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "death");
            case end:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "end");
            case explosive:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "explosive");
            case frosty:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "frosty");
            case furnace:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "furnace");
            case halitosis:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "halitosis");
            case magmatic:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "magmatic");
            case netherstar:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "nether_star");
            case overclocked:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "overclocked");
            case pink:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "pink");
            case potion:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "potion");
            case slimey:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "slimey");
            case survivalist:
                return new GuiGenerator(player.inventory, new InventoryGenerator(stack), "survivalist");

        }
        return null;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID < 14) {
            ItemStack stack = player.getHeldItemMainhand();
            return new ContainerGenerator(player.inventory, new InventoryGenerator(stack));
        }
        return null;
    }
}