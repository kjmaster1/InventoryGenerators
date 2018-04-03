package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInvHalitosisGen extends ItemInventoryGenerator {

    public ItemInvHalitosisGen() {
        super("inv_halitosis_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.halitosis"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem().equals(Items.DRAGON_BREATH);
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return stack.isEmpty() ? 0 : 12000;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        ModelResourceLocation onModel1 = new ModelResourceLocation(getRegistryName() + "_on1", "inventory");
        ModelResourceLocation onModel2 = new ModelResourceLocation(getRegistryName() + "_on2", "inventory");
        ModelResourceLocation onModel3 = new ModelResourceLocation(getRegistryName() + "_on3", "inventory");
        ModelResourceLocation onModel4 = new ModelResourceLocation(getRegistryName() + "_on4", "inventory");
        ModelResourceLocation offModel = new ModelResourceLocation(getRegistryName() + "_off", "inventory");

        ModelBakery.registerItemVariants(this, onModel1, onModel2, onModel3, onModel4, offModel);

        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                int burnTime = getBurnTime(stack);
                if (isOn(stack) && burnTime > 0) {
                    if (burnTime < 3000) {
                        return onModel1;
                    } else if (burnTime < 6000) {
                        return onModel2;
                    } else if (burnTime < 9000) {
                        return onModel3;
                    } else {
                        return onModel4;
                    }
                } else {
                    return offModel;
                }
            }
        });
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }
}
