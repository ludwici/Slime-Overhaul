package com.ludwici.slimeoverhaul;

import com.ludwici.crumbslib.api.*;
import com.ludwici.slimeoverhaul.effect.AntiDepthEffect;
import com.ludwici.slimeoverhaul.effect.BaseMobEffect;
import com.ludwici.slimeoverhaul.entity.custom.elementals.*;
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
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class Content {
    public static final CrumbSupplier<EntityType<AirSlime>>     AIR_SLIME     = EntityHelper.register("air_slime",     getSlimeFactory(AirSlime::new));
    public static final CrumbSupplier<EntityType<WaterSlime>>   WATER_SLIME   = EntityHelper.register("water_slime",   getSlimeFactory(WaterSlime::new).fireImmune());
    public static final CrumbSupplier<EntityType<EarthSlime>>   EARTH_SLIME   = EntityHelper.register("earth_slime",   getSlimeFactory(EarthSlime::new));
    public static final CrumbSupplier<EntityType<FlameSlime>>   FLAME_SLIME   = EntityHelper.register("flame_slime",   getSlimeFactory(FlameSlime::new).fireImmune());
    public static final CrumbSupplier<EntityType<ThunderSlime>> THUNDER_SLIME = EntityHelper.register("thunder_slime", getSlimeFactory(ThunderSlime::new).fireImmune());

    public static final CrumbSupplier<Item> AIR_SLIME_EGG     = ItemHelper.registerSpawnEgg(AIR_SLIME,     0x93ffff, 0x32adb7);
    public static final CrumbSupplier<Item> WATER_SLIME_EGG   = ItemHelper.registerSpawnEgg(WATER_SLIME,   0x20a7d4, 0x004799);
    public static final CrumbSupplier<Item> EARTH_SLIME_EGG   = ItemHelper.registerSpawnEgg(EARTH_SLIME,   0x6e362b, 0x24100b);
    public static final CrumbSupplier<Item> FLAME_SLIME_EGG   = ItemHelper.registerSpawnEgg(FLAME_SLIME,   0xdb5f2e, 0xb83209);
    public static final CrumbSupplier<Item> THUNDER_SLIME_EGG = ItemHelper.registerSpawnEgg(THUNDER_SLIME, 0x93ffff, 0x32adb7);

    public static final CrumbSupplier<Item> WATER_SLIME_BUCKET = ItemHelper.register("water_slime_bucket", () -> new MobBucketItem(WATER_SLIME.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_AXOLOTL, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));
    public static final CrumbSupplier<Item> FIRE_SLIME_BUCKET  = ItemHelper.register("fire_slime_bucket",  () -> new MobBucketItem(FLAME_SLIME.get(), Fluids.LAVA, SoundEvents.BUCKET_EMPTY_AXOLOTL, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));

    public static final CrumbSupplier<Item> AIR_SLIME_BALL     = ItemHelper.register("air_slime_ball",     () -> new Item(new Item.Properties()));
    public static final CrumbSupplier<Item> WATER_SLIME_BALL   = ItemHelper.register("water_slime_ball",   () -> new Item(new Item.Properties()));
    public static final CrumbSupplier<Item> EARTH_SLIME_BALL   = ItemHelper.register("earth_slime_ball",   () -> new Item(new Item.Properties()));
    public static final CrumbSupplier<Item> FIRE_SLIME_BALL    = ItemHelper.register("fire_slime_ball",    () -> new Item(new Item.Properties().fireResistant()));
    public static final CrumbSupplier<Item> THUNDER_SLIME_BALL = ItemHelper.register("thunder_slime_ball", () -> new Item(new Item.Properties()));

    public static final CrumbSupplier<Block> AIR_SLIME_BLOCK     = registerSlimeBlock("air_slime_block");
    public static final CrumbSupplier<Block> WATER_SLIME_BLOCK   = registerSlimeBlock("water_slime_block");
    public static final CrumbSupplier<Block> EARTH_SLIME_BLOCK   = registerSlimeBlock("earth_slime_block");
    public static final CrumbSupplier<Block> FIRE_SLIME_BLOCK    = registerFireResistanceBlock("fire_slime_block", () -> new GlowingSlimeBlock(getSlimeBlockProperties()));
    public static final CrumbSupplier<Block> THUNDER_SLIME_BLOCK = registerSlimeBlock("thunder_slime_block");

    public static final TagKey<EntityType<?>> SLIMES = TagHelper.entityType("slimes");
    public static final TagKey<EntityType<?>> ELEMENTAL_SLIMES = TagHelper.entityType("elemental_slimes");
    public static final TagKey<Block> SLIME_BLOCK_ITEMS = TagHelper.block("slime_blocks");
    public static final TagKey<Biome> AIR_SLIME_BIOME_TAG   = TagHelper.biome("entity_gen/is_air_slime_biome");
    public static final TagKey<Biome> WATER_SLIME_BIOME_TAG = TagHelper.biome("entity_gen/is_water_slime_biome");
    public static final TagKey<Biome> EARTH_SLIME_BIOME_TAG = TagHelper.biome("entity_gen/is_earth_slime_biome");
    public static final TagKey<Biome> FLAME_SLIME_BIOME_TAG = TagHelper.biome("entity_gen/is_flame_slime_biome");

    public static final TagKey<BannerPattern> SLIME_BANNER_PATTERN = TagHelper.bannerPattern( "pattern_item/slime");
    public static final TagKey<BannerPattern> AIR_SLIME_SIGN_BANNER_PATTERN = TagHelper.bannerPattern("pattern_item/air_slime_sign");
    public static final TagKey<BannerPattern> WATER_SLIME_SIGN_BANNER_PATTERN = TagHelper.bannerPattern("pattern_item/water_slime_sign");
    public static final TagKey<BannerPattern> EARTH_SLIME_SIGN_BANNER_PATTERN = TagHelper.bannerPattern("pattern_item/earth_slime_sign");
    public static final TagKey<BannerPattern> FIRE_SLIME_SIGN_BANNER_PATTERN = TagHelper.bannerPattern("pattern_item/fire_slime_sign");

    public static final CrumbSupplier<MobEffect> DOUBLE_JUMP_EFFECT = MobEffectHelper.register("double_jump", () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 9699327));
    public static final CrumbSupplier<MobEffect> WATER_ANTI_DEPTH_EFFECT = MobEffectHelper.register("water_anti_depth", () -> new AntiDepthEffect(MobEffectCategory.BENEFICIAL, Fluids.WATER, 2140116));
    public static final CrumbSupplier<MobEffect> KNOCK_BACK_EFFECT = MobEffectHelper.register("knockback", () -> new BaseMobEffect(MobEffectCategory.BENEFICIAL, 7222827)
            .addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, ResourceLocation.fromNamespaceAndPath(MODID, "knockback"), 1.0f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final CrumbSupplier<MobEffect> LAVA_ANTI_DEPTH_EFFECT = MobEffectHelper.register("lava_anti_depth", () -> new AntiDepthEffect(MobEffectCategory.BENEFICIAL, Fluids.LAVA, 14376750));

    public static final CrumbSupplier<Potion> DOUBLE_JUMP_POTION = PotionHelper.register("double_jump_potion", () -> new Potion(new MobEffectInstance(DOUBLE_JUMP_EFFECT.getHolder(), 1200)));
    public static final CrumbSupplier<Potion> WATER_ANTI_DEPTH_POTION = PotionHelper.register("water_anti_depth_potion", () -> new Potion(new MobEffectInstance(WATER_ANTI_DEPTH_EFFECT.getHolder(), 1200)));
    public static final CrumbSupplier<Potion> KNOCKBACK_POTION = PotionHelper.register("knockback_potion", () -> new Potion(new MobEffectInstance(KNOCK_BACK_EFFECT.getHolder(), 1200)));
    public static final CrumbSupplier<Potion> LAVA_ANTI_DEPTH_POTION = PotionHelper.register("lava_anti_depth_potion", () -> new Potion(new MobEffectInstance(LAVA_ANTI_DEPTH_EFFECT.getHolder(), 1200)));

    public static final CrumbSupplier<Potion> MIDDLE_JUMP_POTION = PotionHelper.register("middle_double_jump_potion", () -> new Potion("double_jump_potion", new MobEffectInstance(DOUBLE_JUMP_EFFECT.getHolder(), 3600)));
    public static final CrumbSupplier<Potion> MIDDLE_WATER_ANTI_DEPTH_POTION = PotionHelper.register("middle_water_anti_depth_potion", () -> new Potion("water_anti_depth_potion", new MobEffectInstance(WATER_ANTI_DEPTH_EFFECT.getHolder(), 3600)));
    public static final CrumbSupplier<Potion> MIDDLE_KNOCKBACK_POTION = PotionHelper.register("middle_knockback_potion", () -> new Potion("knockback_potion", new MobEffectInstance(KNOCK_BACK_EFFECT.getHolder(), 3600)));
    public static final CrumbSupplier<Potion> MIDDLE_LAVA_ANTI_DEPTH_POTION = PotionHelper.register("middle_lava_anti_depth_potion", () -> new Potion("lava_anti_depth_potion", new MobEffectInstance(LAVA_ANTI_DEPTH_EFFECT.getHolder(), 3600)));

    public static final CrumbSupplier<Potion> MIDDLE_FIRE_RESISTANCE = PotionHelper.register("middle_fire_resistance", () -> new Potion("fire_resistance", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000)));
    public static final CrumbSupplier<Potion> MIDDLE_WATER_BREATHING = PotionHelper.register("middle_water_breathing", () -> new Potion("water_breathing", new MobEffectInstance(MobEffects.WATER_BREATHING, 6000)));

    public static final CrumbSupplier<BannerPatternItem> PATTERN_SLIME = ItemHelper.registerBanner("slime", SLIME_BANNER_PATTERN);
    public static final CrumbSupplier<BannerPatternItem> PATTERN_AIR_SLIME_SIGN = ItemHelper.registerBanner("air_slime_sign", AIR_SLIME_SIGN_BANNER_PATTERN);
    public static final CrumbSupplier<BannerPatternItem> PATTERN_WATER_SLIME_SIGN = ItemHelper.registerBanner("water_slime_sign", WATER_SLIME_SIGN_BANNER_PATTERN);
    public static final CrumbSupplier<BannerPatternItem> PATTERN_EARTH_SLIME_SIGN = ItemHelper.registerBanner("earth_slime_sign", EARTH_SLIME_SIGN_BANNER_PATTERN);
    public static final CrumbSupplier<BannerPatternItem> PATTERN_FIRE_SLIME_SIGN = ItemHelper.registerBanner("fire_slime_sign", FIRE_SLIME_SIGN_BANNER_PATTERN);

    public static void registerPotions(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, AIR_SLIME_BLOCK.get().asItem(), DOUBLE_JUMP_POTION.getHolder());
        builder.addMix(Potions.AWKWARD, WATER_SLIME_BLOCK.get().asItem(), WATER_ANTI_DEPTH_POTION.getHolder());
        builder.addMix(Potions.AWKWARD, EARTH_SLIME_BLOCK.get().asItem(), KNOCKBACK_POTION.getHolder());
        builder.addMix(Potions.AWKWARD, FIRE_SLIME_BLOCK.get().asItem(), LAVA_ANTI_DEPTH_POTION.getHolder());

        builder.addMix(Potions.FIRE_RESISTANCE, FIRE_SLIME_BLOCK.get().asItem(), MIDDLE_FIRE_RESISTANCE.getHolder());
        builder.addMix(Potions.WATER_BREATHING, WATER_SLIME_BLOCK.get().asItem(), MIDDLE_WATER_BREATHING.getHolder());
    }

    public static void registerAnvilEvent(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.is(Tags.Items.TOOLS_SHIELD) && right.is(EARTH_SLIME_BALL.get())) {
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

    private static CrumbSupplier<Block> registerSlimeBlock(String name) {
        return BlockHelper.registerWithItem(name, () -> new GlowingSlimeBlock(getSlimeBlockProperties()));
    }

    private static <T extends Block> CrumbSupplier<Block> registerFireResistanceBlock(String name, Supplier<T> supplier) {
        var ret = BlockHelper.register(name, supplier);
        ItemHelper.register(name, () -> new BlockItem(ret.get(), new Item.Properties().fireResistant()));
        return ret;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return state -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static BlockBehaviour.Properties getSlimeBlockProperties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().lightLevel(litBlockEmission(8));
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
                output.accept(THUNDER_SLIME_BALL.get());

                output.accept(AIR_SLIME_BLOCK.get());
                output.accept(WATER_SLIME_BLOCK.get());
                output.accept(EARTH_SLIME_BLOCK.get());
                output.accept(FIRE_SLIME_BLOCK.get());
                output.accept(THUNDER_SLIME_BLOCK.get());

                output.accept(WATER_SLIME_BUCKET.get());
                output.accept(FIRE_SLIME_BUCKET.get());

                output.accept(PATTERN_SLIME.get());
                output.accept(PATTERN_AIR_SLIME_SIGN.get());
                output.accept(PATTERN_WATER_SLIME_SIGN.get());
                output.accept(PATTERN_EARTH_SLIME_SIGN.get());
                output.accept(PATTERN_FIRE_SLIME_SIGN.get());

                output.accept(AIR_SLIME_EGG.get());
                output.accept(WATER_SLIME_EGG.get());
                output.accept(EARTH_SLIME_EGG.get());
                output.accept(FLAME_SLIME_EGG.get());
                output.accept(THUNDER_SLIME_EGG.get());
            }))
    );

    public static void init() {

    }
}
