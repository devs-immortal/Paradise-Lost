package net.id.paradise_lost.compat;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.item.tool.ParadiseLostToolMaterials;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.lang.reflect.Constructor;

import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_ID;
import static net.minecraft.world.item.Item.BASE_ATTACK_SPEED_ID;

public class ParadiseLostFarmersDelightCompat {

    public static RegistryObject<Block, Block> AUREL_CABINET;
    public static RegistryObject<Item, BlockItem> AUREL_CABINET_ITEM;
    public static RegistryObject<Block, Block> MOTHER_AUREL_CABINET;
    public static RegistryObject<Item, BlockItem> MOTHER_AUREL_CABINET_ITEM;
    public static RegistryObject<Block, Block> MENTH_CABINET;
    public static RegistryObject<Item, BlockItem> MENTH_CABINET_ITEM;
    public static RegistryObject<Block, Block> WISTERIA_CABINET;
    public static RegistryObject<Item, BlockItem> WISTERIA_CABINET_ITEM;

    public static RegistryObject<Block, Block> BLACKCURRANT_CRATE;
    public static RegistryObject<Item, BlockItem> BLACKCURRANT_CRATE_ITEM;

    public static RegistryObject<Item, Item> OLVITE_KNIFE;
    public static RegistryObject<Item, Item> GLAZED_GOLD_KNIFE;

    public static void addCompat() {
        tryAddBlocks();
        tryAddItems();
    }

    public static void tryAddBlocks() {
        try {
            Class<Block> cabinetBlockClass = (Class<Block>) Class.forName("vectorwing.farmersdelight.common.block.CabinetBlock");
            Constructor<Block>  cabinetBlockConstructor = cabinetBlockClass.getConstructor(BlockBehaviour.Properties.class);

            AUREL_CABINET = BlockRegistry.add("aurel_cabinet", () -> wrapInit(cabinetBlockConstructor, Block.Properties.ofFullCopy(Blocks.BARREL)));
            MOTHER_AUREL_CABINET = BlockRegistry.add("mother_aurel_cabinet", () -> wrapInit(cabinetBlockConstructor, Block.Properties.ofFullCopy(Blocks.BARREL)));
            MENTH_CABINET = BlockRegistry.add("menth_cabinet", () -> wrapInit(cabinetBlockConstructor, Block.Properties.ofFullCopy(Blocks.BARREL)));
            WISTERIA_CABINET = BlockRegistry.add("wisteria_cabinet", () -> wrapInit(cabinetBlockConstructor, Block.Properties.ofFullCopy(Blocks.BARREL)));

            BLACKCURRANT_CRATE = BlockRegistry.add("blackcurrant_crate", () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
        } catch (Exception e) {
            ModConstants.LOGGER.error("Failed to load Farmer's Delight Compatability blocks!!!!!", e);
        }
    }

    public static void tryAddItems() {
        try {
            AUREL_CABINET_ITEM = ItemRegistry.add("aurel_cabinet", () -> new BlockItem(AUREL_CABINET.get(), new Item.Properties()));
            MOTHER_AUREL_CABINET_ITEM = ItemRegistry.add("mother_aurel_cabinet", () -> new BlockItem(MOTHER_AUREL_CABINET.get(), new Item.Properties()));
            MENTH_CABINET_ITEM = ItemRegistry.add("menth_cabinet", () -> new BlockItem(MENTH_CABINET.get(), new Item.Properties()));
            WISTERIA_CABINET_ITEM = ItemRegistry.add("wisteria_cabinet", () -> new BlockItem(WISTERIA_CABINET.get(), new Item.Properties()));

            BLACKCURRANT_CRATE_ITEM = ItemRegistry.add("blackcurrant_crate", () -> new BlockItem(BLACKCURRANT_CRATE.get(), new Item.Properties()));

            Class<Item> knifeItemClass = (Class<Item>) Class.forName("vectorwing.farmersdelight.common.item.KnifeItem");
            Constructor<Item> knifeItemConstructor = knifeItemClass.getConstructor(Tier.class, Item.Properties.class);

            OLVITE_KNIFE = ItemRegistry.add("olvite_knife", () -> wrapInit(knifeItemConstructor, ParadiseLostToolMaterials.OLVITE, knifeAttributes(ParadiseLostToolMaterials.OLVITE, 0.5F, -2.0F)));
            GLAZED_GOLD_KNIFE = ItemRegistry.add("glazed_gold_knife", () -> wrapInit(knifeItemConstructor, ParadiseLostToolMaterials.GLAZED_GOLD, knifeAttributes(ParadiseLostToolMaterials.GLAZED_GOLD, 0.5F, -2.0F)));

        } catch (Exception e) {
            ModConstants.LOGGER.error("Failed to load Farmer's Delight Compatability items!!!!!", e);
        }
    }

    public static Item.Properties knifeAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return new Item.Properties().attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build());
    }

    private static <T> T wrapInit(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
