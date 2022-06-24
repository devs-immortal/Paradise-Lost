package net.id.paradiselost.client.model.block;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.id.paradiselost.mixin.client.MinecraftClientAccessor;
import net.id.paradiselost.util.Holiday;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

/**
 * A simple overlay generator for holiday themed blocks.
 */
public final class HolidayBlockModel extends JsonUnbakedModel implements UnbakedModel {
    private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft", "block/block");
    
    // These are the same for every block.
    private static final List<Pair<SpriteIdentifier, SpriteIdentifier>> DECORATION_IDS;
    private static final Map<Identifier, HolidayBlockModel> MODEL_OVERRIDES;
    
    private static final int SEED_OFFSET;
    
    private static BlockColors blockColors;
    private static ItemColors itemColors;
    
    static {
        // To add holiday blocks add the case here with the amount of overlay models and add a matching case in the
        // MODEL_OVERRIDE switch a few lines down.
        var holiday = Holiday.get();
        SEED_OFFSET = holiday.ordinal();
        int modelCount = switch (holiday) {
            case CHRISTMAS -> 2;
            default -> 0;
        };
    
        if (modelCount != 0) {
            var name = holiday.getName();
            DECORATION_IDS = IntStream.range(0, modelCount)
                .mapToObj((index) -> Pair.of(
                    new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, locate("block/holiday/" + name + "_" + index)),
                    new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, locate("block/holiday/" + name + "_" + index + "_emissive"))
                ))
                .toList();
        
            MODEL_OVERRIDES = switch(Holiday.get()){
                // Grab all of the leaves
                case CHRISTMAS -> Registry.BLOCK.stream()
                    .filter((block) -> block instanceof LeavesBlock)
                    .map(Registry.BLOCK::getId)
                    .flatMap((identifier) -> {
                        var blockId = new Identifier(identifier.getNamespace(), "block/" + identifier.getPath());
                        var itemId = new Identifier(identifier.getNamespace(), "item/" + identifier.getPath());
                        var model = new HolidayBlockModel(identifier);
                        return Stream.of(
                            Map.entry(blockId, model),
                            Map.entry(itemId, model)
                        );
                    })
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
                
                default -> throw new RuntimeException("This should not happen, double check HolidayBlockModel.<clinit>");
            };
        } else {
            DECORATION_IDS = List.of();
            MODEL_OVERRIDES = Map.of();
        }
    }
    
    public static void init() {
        if(!DECORATION_IDS.isEmpty() && !MODEL_OVERRIDES.isEmpty()) {
            ModelLoadingRegistry.INSTANCE.registerResourceProvider((resourceManager) -> (identifier, context) -> MODEL_OVERRIDES.get(identifier));
        }
    }
    
    private final SpriteIdentifier baseSpriteId;
    private final List<SpriteIdentifier> textureDependencies;
    
    // UnbakedModel
    
    public HolidayBlockModel(Identifier identifier) {
        super(DEFAULT_BLOCK_MODEL, List.of(), Map.of(), false, null, ModelTransformation.NONE, List.of());
    
        baseSpriteId = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, new Identifier(identifier.getNamespace(), "block/" + identifier.getPath()));
        List<SpriteIdentifier> textureDependencies = new ArrayList<>();
        textureDependencies.add(baseSpriteId);
        DECORATION_IDS.forEach((pair)->{
            textureDependencies.add(pair.getFirst());
            textureDependencies.add(pair.getSecond());
        });
        this.textureDependencies = Collections.unmodifiableList(textureDependencies);
    }
    
    @Override
    public Collection<Identifier> getModelDependencies() {
        return Set.of(DEFAULT_BLOCK_MODEL);
    }
    
    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return textureDependencies;
    }
    
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
        if(blockColors == null || itemColors == null){
            blockColors = MinecraftClient.getInstance().getBlockColors();
            itemColors = ((MinecraftClientAccessor)MinecraftClient.getInstance()).getItemColors();
        }
        
        var defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
        parent = defaultBlockModel;
        
        var baseSprite = textureGetter.apply(baseSpriteId);
        List<Pair<Sprite, Sprite>> sprites = DECORATION_IDS.stream()
            .map((pair) -> Pair.of(
                textureGetter.apply(pair.getFirst()),
                textureGetter.apply(pair.getSecond())
            ))
            .toList();
    
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();
    
        var finder = renderer.materialFinder();
        
        var base = finder.find();
        var emissive = finder
            .emissive(0, true)
            .find();
        
        // Can we be dirty and share these between instances?
        List<Mesh> decoratedMeshes = new ArrayList<>();
        for (Pair<Sprite, Sprite> sprite : sprites) {
            for(Direction face : Direction.values()){
                emitter.square(face, 0, 0, 1, 1, 0);
                emitter.material(base);
                emitter.spriteBake(0, sprite.getFirst(), MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
        
                emitter.square(face, 0, 0, 1, 1, 0);
                emitter.material(emissive);
                emitter.spriteBake(0, sprite.getSecond(), MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
            }
            decoratedMeshes.add(builder.build());
        }
        decoratedMeshes = Collections.unmodifiableList(decoratedMeshes);
    
        /* Why doesn't this work?
        var itemColor = ITEM_COLORS.getColor(new ItemStack(Blocks.SPRUCE_LEAVES), 0);
        
        for(Direction face : Direction.values()){
            emitter.square(face, 0, 0, 1, 1, 0);
            emitter.material(base);
            emitter.spriteBake(0, baseSprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.spriteColor(0, itemColor, itemColor, itemColor, itemColor);
            emitter.emit();
        }
        var itemMesh = builder.build();
         */
        
        ModelOverrideList overrides;
        {
            var overrideList = defaultBlockModel.getOverrides();
            if (overrideList.isEmpty()) {
                overrides = ModelOverrideList.EMPTY;
            }else {
                overrides = new ModelOverrideList(loader, parent, loader::getOrLoadModel, overrideList);
            }
        }
        
        return new BakedHolidayModel(
            baseSprite,
            getTransformations(),
            overrides,
            decoratedMeshes/*,
            itemMesh*/
        );
    }
    
    private record BakedHolidayModel(
        Sprite particle,
        ModelTransformation transformations,
        ModelOverrideList overrides,
        List<Mesh> worldMeshes/*,
        Mesh itemMesh*/
    ) implements BakedModel, FabricBakedModel {
        // BakedModel
    
        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
            return List.of();
        }
    
        @Override
        public boolean useAmbientOcclusion() {
            return true;
        }
    
        @Override
        public boolean hasDepth() {
            return false;
        }
    
        @Override
        public boolean isSideLit() {
            return true;
        }
    
        @Override
        public boolean isBuiltin() {
            return false;
        }
    
        @Override
        public Sprite getParticleSprite() {
            return particle;
        }
    
        @Override
        public ModelTransformation getTransformation() {
            return transformations;
        }
    
        @Override
        public ModelOverrideList getOverrides() {
            return overrides;
        }
    
        // FabricBakedModel
    
        @Override
        public boolean isVanillaAdapter() {
            return false;
        }
    
        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
            var emitter = context.getEmitter();
    
            var color = blockColors.getColor(state, blockView, pos, 0) | 0xFF000000;
            
            for(var face : Direction.values()) {
                emitter.square(face, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, particle, MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, color, color, color, color);
                emitter.emit();
            }
    
            var seed = MathHelper.hashCode(pos.getX(), pos.getY() ^ SEED_OFFSET, pos.getZ());
            // 1 in 4
            if((seed & 0b11) == 0){
                seed >>>= 2;
                // 1 in n
                int number = (int)((seed & 0x7FFFFFFF) ^ (seed >> 31));
                context.meshConsumer().accept(worldMeshes.get(number % worldMeshes.size()));
            }
        }
    
        @Override
        public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
            // Why doesn't this work?
            //context.meshConsumer().accept(itemMesh);
            
            var color = itemColors.getColor(stack, 0) | 0xFF000000;
    
            var emitter = context.getEmitter();
            for(var face : Direction.values()) {
                emitter.square(face, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, particle, MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, color, color, color, color);
                emitter.emit();
            }
        }
    }
}
