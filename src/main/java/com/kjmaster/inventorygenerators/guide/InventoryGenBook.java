package com.kjmaster.inventorygenerators.guide;

import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.common.init.InitModGenerators;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@GuideBook
public class InventoryGenBook implements IGuideBook {

    private static Book invGenGuideBook;
    private static String[] generatorStrings = {
            "culinary",
            "death",
            "end",
            "explosive",
            "frosty",
            "halitosis",
            "magmatic",
            "nether_star",
            "overclocked",
            "pink",
            "potion",
            "slimey",
            "survivalist"
    };

    private static String[] upgradeStrings = {
            "auto_pull_upgrade",
            "no_effect_upgrade",
            "speed_upgrade",
    };

    private static Item[] generators = {
            InitModGenerators.invCulinaryGen,
            InitModGenerators.invDeathGen,
            InitModGenerators.invEndGen,
            InitModGenerators.invExplosiveGen,
            InitModGenerators.invFrostyGen,
            InitModGenerators.invHalitosisGen,
            InitModGenerators.invMagmaticGen,
            InitModGenerators.invNetherStarGen,
            InitModGenerators.invOverclockedGen,
            InitModGenerators.invPinkGen,
            InitModGenerators.invPotionGen,
            InitModGenerators.invSlimeyGen,
            InitModGenerators.invSurvivalistGen
    };

    private static Item[] upgrades = {
            InitModGenerators.autoPullUpgrade,
            InitModGenerators.noEffectUpgrade,
            InitModGenerators.speedUpgrade,
    };

    private static ShapedOreRecipe[] generatorRecipes = {

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_1"),
                    InitModGenerators.invCulinaryGen,
                    "WWW"
                    , "WPW",
                    "RFR",
                    'W', "cropWheat" ,
                    'P', Items.COOKED_PORKCHOP,
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_2"),
                    InitModGenerators.invDeathGen,
                    "BBB"
                    , "BSB",
                    "RFR",
                    'B', "bone" ,
                    'S', Items.SPIDER_EYE,
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_3"),
                    InitModGenerators.invExplosiveGen,
                    "GGG"
                    , "GTG",
                    "RFR",
                    'G', "gunpowder" ,
                    'T', Item.getItemFromBlock(Blocks.TNT),
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_4"),
                    InitModGenerators.invFrostyGen,
                    "SSS"
                    , "SIS",
                    "RFR",
                    'S', Items.SNOWBALL ,
                    'I', Item.getItemFromBlock(Blocks.ICE),
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_5"),
                    InitModGenerators.invFurnaceGen,
                    "III"
                    , "ISI",
                    "RFR",
                    'I', "ingotIron" ,
                    'S', InitModGenerators.invSurvivalistGen,
                    'R', "dustRedstone",
                    'F', Item.getItemFromBlock(Blocks.FURNACE)),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_6"),
                    InitModGenerators.invHalitosisGen,
                    "PPP"
                    , "PEP",
                    "RFR",
                    'P', Item.getItemFromBlock(Blocks.PURPUR_BLOCK) ,
                    'E', Item.getItemFromBlock(Blocks.END_ROD),
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_7"),
                    InitModGenerators.invMagmaticGen,
                    "GGG"
                    , "GLG",
                    "RFR",
                    'G', "ingotGold" ,
                    'L', Items.LAVA_BUCKET,
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_8"),
                    InitModGenerators.invNetherStarGen,
                    "WWW"
                    , "WNW",
                    "RFR",
                    'W', new ItemStack(Items.SKULL, 1, 1).getItem(),
                    'N', Items.NETHER_STAR,
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_9"),
                    InitModGenerators.invOverclockedGen,
                    "LLL"
                    , "LGL",
                    "RFR",
                    'L', "gemLapis" ,
                    'G', "blockGold",
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_10"),
                    InitModGenerators.invPinkGen,
                    "PPP"
                    , "PWP",
                    "RFR",
                    'W', new ItemStack(Blocks.WOOL, 1, 6).getItem(),
                    'P', "dyePink",
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_11"),
                    InitModGenerators.invPotionGen,
                    "BBB"
                    , "BSB",
                    "RFR",
                    'B', Items.BLAZE_ROD ,
                    'S', Item.getItemFromBlock(Blocks.BREWING_STAND),
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_12"),
                    InitModGenerators.invSlimeyGen,
                    "SSS"
                    , "SBS",
                    "RFR",
                    'S', "slimeball" ,
                    'B', "blockSlime",
                    'R', "dustRedstone",
                    'F', InitModGenerators.invFurnaceGen),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_13"),
                    InitModGenerators.invSurvivalistGen,
                    "SSS"
                    , "SIS",
                    "RFR",
                    'S', "cobblestone" ,
                    'I', "ingotIron",
                    'R', "dustRedstone",
                    'F', Item.getItemFromBlock(Blocks.FURNACE)),

    };

    private static ShapedOreRecipe[] upgradeRecipes = {

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_14"),
                    InitModGenerators.autoPullUpgrade,
                    "GHG"
                    , "RRR",
                    "RRR",
                    'H', Item.getItemFromBlock(Blocks.HOPPER),
                    'G', "ingotGold",
                    'R', "dustRedstone"),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_15"),
                    InitModGenerators.noEffectUpgrade,
                    "GBG"
                    , "RRR",
                    "RRR",
                    'B', Items.GLASS_BOTTLE,
                    'G', "ingotGold",
                    'R', "dustRedstone"),

            new ShapedOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "entry_16"),
                    InitModGenerators.speedUpgrade,
                    "GSG"
                    , "RRR",
                    "RRR",
                    'S', Items.SUGAR,
                    'G', "ingotGold",
                    'R', "dustRedstone"),
    };

    @Nullable
    @Override
    public Book buildBook() {

        // Generators
        Map<ResourceLocation, EntryAbstract> generatorEntries = new LinkedHashMap<>();

        int i = 0;
        for (String generator : generatorStrings) {
            List<IPage> pages = new ArrayList<>();
            pages.add(new PageText(new TextComponentTranslation("info.invgens." + generator).getUnformattedText()));
            pages.add(new PageIRecipe(generatorRecipes[i]));
            generatorEntries.put(new ResourceLocation(InventoryGenerators.MODID, "entry_" + i), new EntryItemStack(pages, new TextComponentTranslation("item.inv_" + generator + "_gen.name").getUnformattedText(), new ItemStack(generators[i])));
            i++;
        }

        // Upgrades
        Map<ResourceLocation, EntryAbstract> upgradeEntries = new LinkedHashMap<>();
        i = 0;
        for (String upgrade : upgradeStrings) {
            List<IPage> pages = new ArrayList<>();
            pages.add(new PageText(new TextComponentTranslation("info.invgens." + upgrade).getUnformattedText()));
            pages.add(new PageIRecipe(upgradeRecipes[i]));
            upgradeEntries.put(new ResourceLocation(InventoryGenerators.MODID, "entry_" + i), new EntryItemStack(pages, new TextComponentTranslation("item." + upgrade + ".name").getUnformattedText(), new ItemStack(upgrades[i])));
            i++;
        }

        // Setup the list of categories and add our entries to it.
        List<CategoryAbstract> categories = new ArrayList<>();
        categories.add(new CategoryItemStack(generatorEntries,new TextComponentTranslation("itemGroup.invgens").getUnformattedText(), new ItemStack(InitModGenerators.invNetherStarGen)));
        categories.add(new CategoryItemStack(upgradeEntries, new TextComponentTranslation("container.upgrades").getUnformattedText(), new ItemStack(InitModGenerators.speedUpgrade)));

        // Setup the book's base information
        BookBinder invGenGuideBinder = new BookBinder(new ResourceLocation(InventoryGenerators.MODID, "guide_book"));
        invGenGuideBinder.setGuideTitle(new TextComponentTranslation("itemGroup.invgens").getUnformattedText());
        invGenGuideBinder.setItemName("itemGroup.invgens");
        invGenGuideBinder.setAuthor("kjmaster1");
        invGenGuideBinder.setColor(Color.GRAY);
        for (CategoryAbstract categoryAbstract : categories) {
            invGenGuideBinder.addCategory(categoryAbstract);
        }
        invGenGuideBook = invGenGuideBinder.build();
        return invGenGuideBook;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleModel(@Nonnull ItemStack bookStack) {
        GuideAPI.setModel(invGenGuideBook);
    }

    @Override
    public void handlePost(@Nonnull ItemStack bookStack) {}

    @Nullable
    @Override
    public IRecipe getRecipe(@Nonnull ItemStack bookStack) {
        return new ShapelessOreRecipe(new ResourceLocation(InventoryGenerators.MODID, "book_recipe"), bookStack, Items.BOOK, Items.COAL, Items.COAL, Items.COAL).setRegistryName("inv_gen_guide");
    }
}
