package com.kjmaster.inventorygenerators.common;

import com.kjmaster.inventorygenerators.common.handlers.ChangeModeHandler;
import com.kjmaster.inventorygenerators.common.network.ChangeModePacket;
import com.kjmaster.inventorygenerators.common.network.PacketInstance;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;

public class CommonProxy {

    public static Set<Item> pinkThings;
    public static Set<Item> slimeThings;

    public void registerPackets() {
        PacketInstance.INSTANCE.registerMessage(ChangeModeHandler.class, ChangeModePacket.class, 1, Side.SERVER);
    }

    public static void findPinkThings() {
        pinkThings = new HashSet<>();
        for (Block block : Block.REGISTRY) {
            for (IBlockState state : block.getBlockState().getValidStates()) {
                boolean flag = false;
                if (!flag) {
                    for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
                        if (entry.getKey().getValueClass() == EnumDyeColor.class) {
                            if (entry.getValue() == EnumDyeColor.PINK) {
                                flag = true;
                                break;
                            }
                        }
                        if (entry.getKey().getName().contains("color")) {
                            if (((IProperty) entry.getKey()).getName(entry.getValue()).contains("pink")) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }

                if (flag) {
                    try {
                            for (int i = 0; i < 10; i++) {
                                NonNullList<ItemStack> drops = NonNullList.create();
                                block.getDrops(drops, new IBlockAccess() {
                                    @Nullable
                                    @Override
                                    public TileEntity getTileEntity(BlockPos pos) {
                                        return null;
                                    }

                                    @Override
                                    public int getCombinedLight(BlockPos pos, int lightValue) {
                                        return 0;
                                    }

                                    @Override
                                    public IBlockState getBlockState(BlockPos pos) {
                                        return null;
                                    }

                                    @Override
                                    public boolean isAirBlock(BlockPos pos) {
                                        return false;
                                    }

                                    @Override
                                    public Biome getBiome(BlockPos pos) {
                                        return null;
                                    }

                                    @Override
                                    public int getStrongPower(BlockPos pos, EnumFacing direction) {
                                        return 0;
                                    }

                                    @Override
                                    public WorldType getWorldType() {
                                        return null;
                                    }

                                    @Override
                                    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
                                        return false;
                                    }
                                }, new BlockPos(0, 0, 0), state, 300);

                                for (ItemStack drop : drops) {
                                    if (!drop.isEmpty()) {
                                        pinkThings.add(drop.getItem());
                                    }
                                }
                            }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (String string : OreDictionary.getOreNames()) {
            if (string.toLowerCase(Locale.ROOT).contains("pink")) {
                for (ItemStack stack : OreDictionary.getOres(string)) {
                    if (!stack.isEmpty()) {
                        pinkThings.add(stack.getItem());
                    }
                }
            }
        }
    }

    public static void findSlimeThings() {
        slimeThings = new HashSet<>();
        for (ItemStack stack : OreDictionary.getOres("slimeball")) {
            if (!stack.isEmpty()) {
                slimeThings.add(stack.getItem());
            }
        }
        for (ItemStack stack : OreDictionary.getOres("blockSlime")) {
            if (!stack.isEmpty()) {
                slimeThings.add(stack.getItem());
            }
        }
    }
}
