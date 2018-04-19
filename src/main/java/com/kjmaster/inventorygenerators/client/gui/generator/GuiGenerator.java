package com.kjmaster.inventorygenerators.client.gui.generator;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class GuiGenerator extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation(InventoryGenerators.MODID, "textures/gui/container/generator.png");

    private final String name;

    public GuiGenerator(InventoryPlayer playerInv, IItemHandler inv, String name) {
        super(new ContainerGenerator(playerInv, inv));
        this.name = name;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("item.inv_" + name + "_gen.name");
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
        fontRenderer.drawString(I18n.format("container.upgrades"), 8, ySize - 126 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
    }
}
