package com.ludwici.slimeoverhaul.world.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.VillagePools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.fml.config.IConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class FireShrineStructure extends Structure {
    private static final ResourceLocation FLOOR = ResourceLocation.fromNamespaceAndPath(MODID, "fire_shrine_floor");

    private static final List<Block> DEEPSLATE = List.of(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS);
    private static final List<Block> DEEPSLATE_TILES = List.of(Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES);
    private static final List<Block> BLACKSTONE = List.of(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);

    private static final List<List<Block>> BLOCK_SELECTOR = List.of(DEEPSLATE, DEEPSLATE_TILES, BLACKSTONE);

    public static final MapCodec<FireShrineStructure> CODEC = RecordCodecBuilder.<FireShrineStructure>mapCodec(instance ->
        instance.group(settingsCodec(instance)).apply(instance, FireShrineStructure::new)
    );

    protected FireShrineStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
//        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom worldgenRandom = context.random();
        Rotation rotation = Rotation.getRandom(worldgenRandom);
        BlockPos blockPos = getLowestYIn5by5BoxOffset7Blocks(context, rotation);
//        if (worldgenRandom.nextDouble() >= new )
//        System.out.println("findGenerationPoint: " + blockPos);
        return Optional.of(new GenerationStub(blockPos, builder -> builder.addPiece(createPieces(blockPos))));
//        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, generator -> generatePieces(generator, context));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        WorldgenRandom worldgenRandom = context.random();
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(worldgenRandom);
        addPieces(context.structureTemplateManager(), blockPos, rotation, builder, worldgenRandom);
    }

    private void addPieces(StructureTemplateManager manager, BlockPos startPos, Rotation rotation, StructurePieceAccessor pieces, RandomSource random) {
//        pieces.addPiece(new FireShrinePiece(manager, FLOOR, startPos, rotation, 0));
        pieces.addPiece(new FireShrineNewPiece(FIRE_SHRINE_PIECE.get(), 0, new BoundingBox(startPos.getX(), startPos.getY(), startPos.getZ(), startPos.getX(), startPos.getY(), startPos.getZ())));
    }

    private FireShrineNewPiece createPieces(BlockPos startPos) {
        return new FireShrineNewPiece(FIRE_SHRINE_PIECE.get(), 0, new BoundingBox(startPos.getX(), startPos.getY(), startPos.getZ(), startPos.getX()+8, startPos.getY()+4, startPos.getZ()+8));
    }

    @Override
    public StructureType<?> type() {
        return FIRE_SHRINE.get();
    }

    public static class FireShrineNewPiece extends StructurePiece {

        protected FireShrineNewPiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
            super(type, genDepth, boundingBox);
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {

        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            int radius = 4 * 4;
            BlockPos center = box.getCenter();
//            BlockPos.betweenClosedStream(-radius, 0, -radius, radius, 1, radius)
            BlockPos.betweenClosedStream(center.offset(0, 0, 0), center.offset(3, 0, 3)).forEach(position -> {
//            BlockPos.betweenClosedStream(-radius, 0, -radius, radius, 1, radius).map(BlockPos::immutable).forEach(position -> {
//                if (level.getBlockState(position).isAir()) {
                    level.setBlock(position, Blocks.GOLD_BLOCK.defaultBlockState(), 2);
//                }
            });
            System.out.println(pos + "|" + center);
//            generateBox();
        }
    }

    public static class FireShrinePiece extends TemplateStructurePiece {
        public FireShrinePiece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(FIRE_SHRINE_PIECE.get(), tag, context.structureTemplateManager(), factory -> makeSettings(Rotation.CLOCKWISE_90, factory));
        }

        public FireShrinePiece(StructureTemplateManager manager, ResourceLocation location, BlockPos startPos, Rotation rotation, int down) {
            super(FIRE_SHRINE_PIECE.get(), 0, manager, location, location.toString(), makeSettings(rotation, location), startPos);
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation, ResourceLocation location) {
            return new StructurePlaceSettings()
                    .setRotation(rotation)
                    .setMirror(Mirror.NONE);
        }

//        private static BlockPos makePosition(ResourceLocation location, BlockPos pos, int down) {
//            return pos.offset()
//        }


        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("Rot", placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {

        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
            ResourceLocation resourceLocation = ResourceLocation.parse(templateName);
            StructurePlaceSettings structurePlaceSettings = makeSettings(this.placeSettings.getRotation(), resourceLocation);
//            BlockPos blockPos = templatePosition.offset(StructureTemplate.calculateRelativePosition(structurePlaceSettings,));
            int i = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, templatePosition.getX(), templatePosition.getZ());
            this.templatePosition = this.templatePosition.offset(0, i - 90 - 1, 0);
            super.postProcess(level, structureManager, generator, random, box, chunkPos, pos);
//            if (resourceLocation.equals(FLOOR)) {
//                BlockPos blockPos = templatePosition.offset(StructureTemplate.calculateRelativePosition(structurePlaceSettings, new BlockPos(3, 0, 5)));
//                BlockState blockState = level.getBlockState(blockPos.below());
//                if (blockState.isAir()) {
//                    level.setBlock(blockPos, FIRE_SLIME_BLOCK.get().defaultBlockState(), 3);
//                }
//            }
        }
    }
}
