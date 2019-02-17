package com.kjmaster.inventorygenerators.common.upgrades;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.client.IHasModel;
import com.kjmaster.kjlib.common.items.ItemBase;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSpeedUpgrade extends ItemBase implements IHasModel {

    public ItemSpeedUpgrade() {
        super("speed_upgrade", InventoryGenerators.invGenTab, 3);
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        else {
            tooltip.add(StringHelper.getInfoText("info.invgens.speed_upgrade"));
        }
    }
}
