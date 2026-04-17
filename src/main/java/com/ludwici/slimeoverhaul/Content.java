package com.ludwici.slimeoverhaul;

import com.ludwici.crumbslib.api.*;
import com.ludwici.slimeoverhaul.block.*;
import com.ludwici.slimeoverhaul.block.crystallized.*;
import com.ludwici.slimeoverhaul.block.entities.*;
import com.ludwici.slimeoverhaul.block.slimy.*;
import com.ludwici.slimeoverhaul.effect.*;
import com.ludwici.slimeoverhaul.entity.custom.elementals.*;
import com.ludwici.slimeoverhaul.item.*;
import com.ludwici.slimeoverhaul.world.structure.FireShrineStructure;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class Content {
    public static final CrumbSupplier<EntityType<AirSlime>>   AIR_SLIME   = EntityHelper.register("air_slime",   getSlimeFactory(AirSlime::new));
    public static final CrumbSupplier<EntityType<WaterSlime>> WATER_SLIME = EntityHelper.register("water_slime", getSlimeFactory(WaterSlime::new).fireImmune());
    public static final CrumbSupplier<EntityType<EarthSlime>> EARTH_SLIME = EntityHelper.register("earth_slime", getSlimeFactory(EarthSlime::new));
    public static final CrumbSupplier<EntityType<FlameSlime>> FLAME_SLIME = EntityHelper.register("flame_slime", getSlimeFactory(FlameSlime::new).fireImmune());

    public static final CrumbSupplier<Item> AIR_SLIME_EGG   = ItemHelper.registerSpawnEgg(AIR_SLIME,   0x93ffff, 0x32adb7);
    public static final CrumbSupplier<Item> WATER_SLIME_EGG = ItemHelper.registerSpawnEgg(WATER_SLIME, 0x20a7d4, 0x004799);
    public static final CrumbSupplier<Item> EARTH_SLIME_EGG = ItemHelper.registerSpawnEgg(EARTH_SLIME, 0x6e362b, 0x24100b);
    public static final CrumbSupplier<Item> FLAME_SLIME_EGG = ItemHelper.registerSpawnEgg(FLAME_SLIME, 0xdb5f2e, 0xb83209);

    public static final CrumbSupplier<Item> WATER_SLIME_BUCKET = ItemHelper.register("water_slime_bucket", () -> new MobBucketItem(WATER_SLIME.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_AXOLOTL, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));
    public static final CrumbSupplier<Item> FIRE_SLIME_BUCKET  = ItemHelper.register("fire_slime_bucket",  () -> new MobBucketItem(FLAME_SLIME.get(), Fluids.LAVA, SoundEvents.BUCKET_EMPTY_AXOLOTL, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));

    public static final CrumbSupplier<Item> AIR_SLIME_BALL   = ItemHelper.registerSimpleItem("air_slime_ball");
    public static final CrumbSupplier<Item> WATER_SLIME_BALL = ItemHelper.registerSimpleItem("water_slime_ball", new Item.Properties().fireResistant());
    public static final CrumbSupplier<Item> EARTH_SLIME_BALL = ItemHelper.registerSimpleItem("earth_slime_ball");
    public static final CrumbSupplier<Item> FIRE_SLIME_BALL  = ItemHelper.registerSimpleItem("fire_slime_ball",  new Item.Properties().fireResistant());

    public static final CrumbSupplier<Item> CLEANSING_BRUSH = ItemHelper.register("cleansing_brush", () -> new CleansingBrushItem(new Item.Properties().durability(64)));

    public static final CrumbSupplier<Item> ANCIENT_AIR_FRAGMENTS = ItemHelper.registerSimpleItem("ancient_air_fragments");
    public static final CrumbSupplier<Item> ANCIENT_WATER_FRAGMENTS = ItemHelper.registerSimpleItem("ancient_water_fragments", new Item.Properties().fireResistant());
    public static final CrumbSupplier<Item> ANCIENT_EARTH_FRAGMENTS = ItemHelper.registerSimpleItem("ancient_earth_fragments");
    public static final CrumbSupplier<Item> ANCIENT_FIRE_FRAGMENTS = ItemHelper.registerSimpleItem("ancient_fire_fragments", new Item.Properties().fireResistant());

    public static final CrumbSupplier<Item> AIR_CRYSTALLIZED_DUST = ItemHelper.register("air_crystallized_dust", () -> new CrystallizedDustItem(new Item.Properties()));
    public static final CrumbSupplier<Item> WATER_CRYSTALLIZED_DUST = ItemHelper.register("water_crystallized_dust", () -> new CrystallizedDustItem(new Item.Properties().fireResistant()));
    public static final CrumbSupplier<Item> EARTH_CRYSTALLIZED_DUST = ItemHelper.register("earth_crystallized_dust", () -> new CrystallizedDustItem(new Item.Properties()));
    public static final CrumbSupplier<Item> PYROCIDE_DUST = ItemHelper.register("pyrocide_dust", () -> new CrystallizedDustItem(new Item.Properties().fireResistant()));

    public static final CrumbSupplier<Item> BLANK_FIRE_TEMPLATE = ItemHelper.registerSimpleItem("blank_fire_template", new Item.Properties().fireResistant());
    public static final CrumbSupplier<Item> FIRE_TEMPLATE = ItemHelper.registerSimpleItem("fire_template", new Item.Properties().fireResistant());

    public static final CrumbSupplier<Block> AIR_SLIME_BLOCK   = registerSlimeBlock("air_slime_block", MapColor.COLOR_CYAN);
    public static final CrumbSupplier<Block> WATER_SLIME_BLOCK = registerSlimeBlock("water_slime_block", MapColor.WATER);
    public static final CrumbSupplier<Block> EARTH_SLIME_BLOCK = registerSlimeBlock("earth_slime_block", MapColor.DIRT);
    public static final CrumbSupplier<Block> FIRE_SLIME_BLOCK  = registerFireResistanceBlock("fire_slime_block", () -> new GlowingSlimeBlock(getSlimeBlockProperties().mapColor(MapColor.COLOR_ORANGE)));

    public static final CrumbSupplier<Block> ANCIENT_AIR_SLIMY_BLOCK = BlockHelper.registerWithItem("ancient_air_slimy_block", () -> new AncientAirSlimyBlock(getAncientBlockProperties(Optional.empty())));
    public static final CrumbSupplier<Block> ANCIENT_WATER_SLIMY_BLOCK = BlockHelper.registerWithItem("ancient_water_slimy_block", () -> new AncientWaterSlimyBlock(getAncientBlockProperties(Optional.empty())));
    public static final CrumbSupplier<Block> ANCIENT_EARTH_SLIMY_BLOCK = BlockHelper.registerWithItem("ancient_earth_slimy_block", () -> new AncientEarthSlimyBlock(getAncientBlockProperties(Optional.empty())));
    public static final CrumbSupplier<Block> ANCIENT_FIRE_SLIMY_BLOCK = BlockHelper.registerWithItem("ancient_fire_slimy_block", () -> new AncientFireSlimyBlock(getAncientBlockProperties(Optional.of(MapColor.DEEPSLATE))));

    public static final CrumbSupplier<Block> AIR_SLIME_COAT = BlockHelper.registerWithItem("air_slime_coat", () -> new SlimeCoatBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noOcclusion()));
    public static final CrumbSupplier<Block> WATER_SLIME_COAT = BlockHelper.registerWithItem("water_slime_coat", () -> new SlimeCoatBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noOcclusion()));
    public static final CrumbSupplier<Block> EARTH_SLIME_COAT = BlockHelper.registerWithItem("earth_slime_coat", () -> new SlimeCoatBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noOcclusion()));
    public static final CrumbSupplier<Block> FIRE_SLIME_COAT = BlockHelper.registerWithItem("fire_slime_coat", () -> new SlimeCoatBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).noOcclusion()));

    public static final CrumbSupplier<Block> AIR_CRYSTALLIZED_SLIME_BLOCK = BlockHelper.registerWithItem("air_crystallized_slime_block", () -> new PyrocideBlock(getCrystallizedSlimeProperties(0)));
    public static final CrumbSupplier<Block> WATER_CRYSTALLIZED_SLIME_BLOCK = BlockHelper.registerWithItem("water_crystallized_slime_block", () -> new PyrocideBlock(getCrystallizedSlimeProperties(0)));
    public static final CrumbSupplier<Block> EARTH_CRYSTALLIZED_SLIME_BLOCK = BlockHelper.registerWithItem("earth_crystallized_slime_block", () -> new PyrocideBlock(getCrystallizedSlimeProperties(0)));
    public static final CrumbSupplier<Block> SMALL_PYROCIDE_BLOCK = BlockHelper.registerWithItem("small_pyrocide_block", () -> new PyrocideBlock(getCrystallizedSlimeProperties(2)));
    public static final CrumbSupplier<Block> MEDIUM_PYROCIDE_BLOCK = BlockHelper.registerWithItem("medium_pyrocide_block", () -> new PyrocideBlock(getCrystallizedSlimeProperties(4)));
    public static final CrumbSupplier<Block> LARGE_PYROCIDE_BLOCK = BlockHelper.registerWithItem("large_pyrocide_block", () -> new PyrocideBlock(getCrystallizedSlimeProperties(8)));

    public static final CrumbSupplier<BlockEntityType<AncientSlimyBlockEntity>> ANCIENT_SLIMY_BLOCK_ENTITY = BlockEntityHelper.register("slimy_block_entity", AncientSlimyBlockEntity::new, ANCIENT_AIR_SLIMY_BLOCK, ANCIENT_WATER_SLIMY_BLOCK, ANCIENT_EARTH_SLIMY_BLOCK, ANCIENT_FIRE_SLIMY_BLOCK);

    public static final TagKey<EntityType<?>> SLIMES = TagHelper.entityType("slimes");
    public static final TagKey<EntityType<?>> ELEMENTAL_SLIMES = TagHelper.entityType("elemental_slimes");
    public static final TagKey<Block> SLIME_BLOCK_ITEMS = TagHelper.block("slime_blocks");
    public static final TagKey<Biome> AIR_SLIME_BIOME_TAG   = TagHelper.biome("entity_gen/is_air_slime_biome");
    public static final TagKey<Biome> WATER_SLIME_BIOME_TAG = TagHelper.biome("entity_gen/is_water_slime_biome");
    public static final TagKey<Biome> EARTH_SLIME_BIOME_TAG = TagHelper.biome("entity_gen/is_earth_slime_biome");
    public static final TagKey<Biome> FLAME_SLIME_BIOME_TAG = TagHelper.biome("entity_gen/is_flame_slime_biome");
    public static final TagKey<Item> ANCIENT_AIR_FRAGMENTS_TAG = TagHelper.item("ancient_air_fragments");
    public static final TagKey<Item> ANCIENT_WATER_FRAGMENTS_TAG = TagHelper.item("ancient_water_fragments");
    public static final TagKey<Item> ANCIENT_EARTH_FRAGMENTS_TAG = TagHelper.item("ancient_earth_fragments");
    public static final TagKey<Item> ANCIENT_FIRE_FRAGMENTS_TAG = TagHelper.item("ancient_fire_fragments");
    public static final TagKey<Item> ANCIENT_ALL_FRAGMENTS_TAG = TagHelper.item("ancient_all_fragments");
    public static final TagKey<Item> AIR_ITEMS_TAG = TagHelper.item("air_items");
    public static final TagKey<Item> WATER_ITEMS_TAG = TagHelper.item("water_items");
    public static final TagKey<Item> EARTH_ITEMS_TAG = TagHelper.item("earth_items");
    public static final TagKey<Item> FIRE_ITEMS_TAG = TagHelper.item("fire_items");
    public static final TagKey<Item> SLIME_COAT_ITEMS_TAG = TagHelper.item("slime_coat_items");

    public static final CrumbSupplier<MobEffect> DOUBLE_JUMP_EFFECT = MobEffectHelper.register("double_jump", MobEffectCategory.BENEFICIAL, 9699327);
    public static final CrumbSupplier<MobEffect> WATER_ANTI_DEPTH_EFFECT = MobEffectHelper.register("water_anti_depth", () -> new AntiDepthEffect(MobEffectCategory.BENEFICIAL, Fluids.WATER, 2140116));
    public static final CrumbSupplier<MobEffect> KNOCK_BACK_EFFECT = MobEffectHelper.register("knockback", () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 7222827)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(MODID, "knockback"), 1.0f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final CrumbSupplier<MobEffect> LAVA_ANTI_DEPTH_EFFECT = MobEffectHelper.register("lava_anti_depth", () -> new AntiDepthEffect(MobEffectCategory.BENEFICIAL, Fluids.LAVA, 14376750));
    public static final CrumbSupplier<MobEffect> HAND_BURN_EFFECT = MobEffectHelper.register("hand_burn_effect", () -> new HandBurnEffect(MobEffectCategory.HARMFUL, 14376750));
    public static final CrumbSupplier<MobEffect> SLIPPERY_EFFECT = MobEffectHelper.register("slippery_effect", MobEffectCategory.NEUTRAL, 2140116);

    public static final CrumbSupplier<Potion> DOUBLE_JUMP_POTION = PotionHelper.register("double_jump_potion", () -> new Potion(new MobEffectInstance(DOUBLE_JUMP_EFFECT.getHolder(), 1200, 0)));
    public static final CrumbSupplier<Potion> WATER_ANTI_DEPTH_POTION = PotionHelper.register("water_anti_depth_potion", () -> new Potion(new MobEffectInstance(WATER_ANTI_DEPTH_EFFECT.getHolder(), 1200, 0)));
    public static final CrumbSupplier<Potion> KNOCKBACK_POTION = PotionHelper.register("knockback_potion", () -> new Potion(new MobEffectInstance(KNOCK_BACK_EFFECT.getHolder(), 1200, 0)));
    public static final CrumbSupplier<Potion> LAVA_ANTI_DEPTH_POTION = PotionHelper.register("lava_anti_depth_potion", () -> new Potion(new MobEffectInstance(LAVA_ANTI_DEPTH_EFFECT.getHolder(), 1200, 0)));

    public static final CrumbSupplier<Potion> MIDDLE_FIRE_RESISTANCE = PotionHelper.register("middle_fire_resistance", () -> new Potion("fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000)));
    public static final CrumbSupplier<Potion> MIDDLE_WATER_BREATHING = PotionHelper.register("middle_water_breathing", () -> new Potion("water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 6000)));

    public static final CrumbSupplier<BannerPatternItem> PATTERN_SLIME = ItemHelper.registerBanner("slime");
    public static final CrumbSupplier<BannerPatternItem> PATTERN_AIR_SLIME_SIGN = ItemHelper.registerBanner("air_slime_sign");
    public static final CrumbSupplier<BannerPatternItem> PATTERN_WATER_SLIME_SIGN = ItemHelper.registerBanner("water_slime_sign");
    public static final CrumbSupplier<BannerPatternItem> PATTERN_EARTH_SLIME_SIGN = ItemHelper.registerBanner("earth_slime_sign");
    public static final CrumbSupplier<BannerPatternItem> PATTERN_FIRE_SLIME_SIGN = ItemHelper.registerBanner("fire_slime_sign");

    public static final CrumbSupplier<StructureType<FireShrineStructure>> FIRE_SHRINE = StructureHelper.registerStructure("fire_shrine", () -> () -> FireShrineStructure.CODEC);
    public static final CrumbSupplier<StructurePieceType> FIRE_SHRINE_PIECE = StructureHelper.registerPiece("fire_shrine", () -> FireShrineStructure.FireShrinePiece::new);

    public static void registerPotions(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, AIR_SLIME_BLOCK.get().asItem(), DOUBLE_JUMP_POTION.getHolder());
        builder.addMix(Potions.AWKWARD, WATER_SLIME_BLOCK.get().asItem(), WATER_ANTI_DEPTH_POTION.getHolder());
        builder.addMix(Potions.AWKWARD, EARTH_SLIME_BLOCK.get().asItem(), KNOCKBACK_POTION.getHolder());
        builder.addMix(Potions.AWKWARD, FIRE_SLIME_BLOCK.get().asItem(), LAVA_ANTI_DEPTH_POTION.getHolder());

        builder.addMix(Potions.FIRE_RESISTANCE, FIRE_SLIME_BLOCK.get().asItem(), MIDDLE_FIRE_RESISTANCE.getHolder());
        builder.addMix(Potions.WATER_BREATHING, WATER_SLIME_BLOCK.get().asItem(), MIDDLE_WATER_BREATHING.getHolder());
    }

    public static boolean isShieldItem(ItemStack itemStack) {
        return itemStack.is(Tags.Items.TOOLS_SHIELD) || itemStack.getItem() instanceof ShieldItem;
    }

    public static void registerAnvilEvent(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (isShieldItem(left) && right.is(EARTH_SLIME_BALL.get())) {
            ItemStack result = left.copy();
            CustomData data = result.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag;
            if (data != null) {
                tag = data.copyTag();
            } else {
                tag = new CompoundTag();
            }
            tag.putInt("bounce", 32);
            result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            event.setOutput(result);
            event.setCost(3);
            event.setMaterialCost(1);
        }
    }

    public static void spawns(RegisterSpawnPlacementsEvent event) {
        event.register(AIR_SLIME.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AirSlime::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(WATER_SLIME.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, WaterSlime::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(EARTH_SLIME.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EarthSlime::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(FLAME_SLIME.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE_WG, FlameSlime::checkSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    private static CrumbSupplier<Block> registerSlimeBlock(String name, MapColor color) {
        return BlockHelper.registerWithItem(name, () -> new GlowingSlimeBlock(getSlimeBlockProperties().mapColor(color)));
    }

    private static <T extends Block> CrumbSupplier<Block> registerFireResistanceBlock(String name, Supplier<T> supplier) {
        var ret = BlockHelper.register(name, supplier);
        ItemHelper.register(name, () -> new BlockItem(ret.get(), new Item.Properties().fireResistant()));
        return ret;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return state -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static BlockBehaviour.Properties getCrystallizedSlimeProperties(int lightLevel) {
        return BlockBehaviour.Properties.of()
                .noOcclusion()
                .requiresCorrectToolForDrops()
                .strength(2.0f, 3.0f)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.AMETHYST)
                .mapColor(MapColor.COLOR_ORANGE)
                .lightLevel(state -> lightLevel);
    }

    private static BlockBehaviour.Properties getSlimeBlockProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.GRASS)
                .friction(0.8F)
                .sound(SoundType.SLIME_BLOCK)
                .noOcclusion()
                .lightLevel(litBlockEmission(8));
    }

    private static BlockBehaviour.Properties getAncientBlockProperties(Optional<MapColor> mapColor) {
        return BlockBehaviour.Properties.of()
                .strength(-1, 3600000.0F)
                .mapColor(mapColor.orElse(MapColor.NONE))
                ;
    }

    private static <T extends Entity> EntityType.Builder<T> getSlimeFactory(EntityType.EntityFactory<T> factory) {
        return EntityType.Builder.of(factory, MobCategory.MONSTER)
                .sized(0.52F, 0.52F)
                .eyeHeight(0.325F)
                .spawnDimensionsScale(4.0F)
                .clientTrackingRange(10);
    }

    public static final CrumbSupplier<CreativeModeTab> MAIN_TAB = CreativeTabHelper.register("main", (tab) -> tab
            .title(Component.literal("Slime Overhaul"))
            .icon(() -> FLAME_SLIME_EGG.get().getDefaultInstance())
            .displayItems(((itemDisplayParameters, output) -> {
                output.accept(AIR_SLIME_BALL.get());
                output.accept(WATER_SLIME_BALL.get());
                output.accept(EARTH_SLIME_BALL.get());
                output.accept(FIRE_SLIME_BALL.get());

                output.accept(AIR_SLIME_BLOCK.get());
                output.accept(WATER_SLIME_BLOCK.get());
                output.accept(EARTH_SLIME_BLOCK.get());
                output.accept(FIRE_SLIME_BLOCK.get());

//                output.accept(ANCIENT_AIR_SLIMY_BLOCK.get());
//                output.accept(ANCIENT_WATER_SLIMY_BLOCK.get());
//                output.accept(ANCIENT_EARTH_SLIMY_BLOCK.get());
                output.accept(ANCIENT_FIRE_SLIMY_BLOCK.get());

//                output.accept(AIR_CRYSTALLIZED_DUST.get());
//                output.accept(WATER_CRYSTALLIZED_DUST.get());
//                output.accept(EARTH_CRYSTALLIZED_DUST.get());
                output.accept(PYROCIDE_DUST.get());

//                output.accept(AIR_SLIME_COAT.get());
//                output.accept(WATER_SLIME_COAT.get());
//                output.accept(EARTH_SLIME_COAT.get());
//                output.accept(FIRE_SLIME_COAT.get());

//                output.accept(ANCIENT_AIR_FRAGMENTS.get());
//                output.accept(ANCIENT_WATER_FRAGMENTS.get());
//                output.accept(ANCIENT_EARTH_FRAGMENTS.get());
                output.accept(ANCIENT_FIRE_FRAGMENTS.get());

                output.accept(BLANK_FIRE_TEMPLATE.get());
                output.accept(FIRE_TEMPLATE.get());

//                output.accept(AIR_CRYSTALLIZED_SLIME_BLOCK.get());
//                output.accept(WATER_CRYSTALLIZED_SLIME_BLOCK.get());
//                output.accept(EARTH_CRYSTALLIZED_SLIME_BLOCK.get());
                output.accept(SMALL_PYROCIDE_BLOCK.get());
                output.accept(MEDIUM_PYROCIDE_BLOCK.get());
                output.accept(LARGE_PYROCIDE_BLOCK.get());

                output.accept(WATER_SLIME_BUCKET.get());
                output.accept(FIRE_SLIME_BUCKET.get());
                output.accept(CLEANSING_BRUSH.get());

                output.accept(AIR_SLIME_EGG.get());
                output.accept(WATER_SLIME_EGG.get());
                output.accept(EARTH_SLIME_EGG.get());
                output.accept(FLAME_SLIME_EGG.get());

                output.accept(PATTERN_SLIME.get());
                output.accept(PATTERN_AIR_SLIME_SIGN.get());
                output.accept(PATTERN_WATER_SLIME_SIGN.get());
                output.accept(PATTERN_EARTH_SLIME_SIGN.get());
                output.accept(PATTERN_FIRE_SLIME_SIGN.get());
            }))
    );

    public static void init() {

    }
}
