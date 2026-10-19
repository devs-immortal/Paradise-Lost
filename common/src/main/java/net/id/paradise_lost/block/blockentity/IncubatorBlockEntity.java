package net.id.paradise_lost.block.blockentity;

import net.id.paradise_lost.entity.passive.moa.MoaGenes;
import net.id.paradise_lost.registry.ItemRegistry;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.id.paradise_lost.util.PlayerInventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static net.id.paradise_lost.tag.ParadiseLostBlockTags.INCUBATOR_WARMER_LIGHTS;
import static net.id.paradise_lost.tag.ParadiseLostBlockTags.INCUBATOR_WARMER_BEDS;

public class IncubatorBlockEntity extends BlockEntity {

    private UUID owner;
    private int hatchTicks = 100;
    private ItemStack egg;
    private float offsetHeight;
    private Direction itemFacing = Direction.SOUTH;

    public IncubatorBlockEntity(BlockPos pos, BlockState state, float offsetHeight) {
        super(ParadiseLostBlockEntityTypes.INCUBATOR.get(), pos, state);
        this.egg = ItemStack.EMPTY;
        this.offsetHeight = offsetHeight;
    }

    public IncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(ParadiseLostBlockEntityTypes.INCUBATOR.get(), pos, state);
        this.egg = ItemStack.EMPTY;
        this.offsetHeight = 0.55F;
    }

    public static <T extends BlockEntity> void tickServer(Level world, BlockPos pos, BlockState state, T entity) {
        IncubatorBlockEntity incubator = (IncubatorBlockEntity) entity;
        if (incubator.egg.getItem() == ItemRegistry.MOA_EGG.get()) {
            if (world.getGameTime() % 10 == 0) {
                if (world.getBlockState(pos.above(2)).is(INCUBATOR_WARMER_LIGHTS) || world.getBlockState(pos.above(1)).is(INCUBATOR_WARMER_LIGHTS)) {
                    incubator.hatchTicks -= 2;
                }
                if (world.getBlockState(pos.below()).is(INCUBATOR_WARMER_BEDS)) {
                    incubator.hatchTicks -= 1;
                }
            }

            incubator.hatchTicks--;
            if (incubator.hatchTicks <= 0) {
                incubator.hatchTicks = 0;
                var moa = MoaGenes.getMoaFromEgg(world, incubator.egg, incubator.owner, pos);
                moa.moveTo(pos.getX() + 0.25, pos.getY() + 0.65, pos.getZ() + 0.25, world.getRandom().nextFloat() * 360 - 180, 0);
                world.playSound(null, pos, ParadiseLostSoundEvents.ENTITY_MOA_EGG_HATCH, SoundSource.BLOCKS, 0.8F, 0.5F);
                world.addFreshEntity(moa);
                incubator.egg = ItemStack.EMPTY;
                incubator.syncToClient();
            }
            incubator.setChanged();
        }
    }

    public void handleUse(Player player, InteractionHand hand, ItemStack handStack) {
        owner = player.getUUID();
        if (egg.isEmpty()) {
            if (handStack.isEmpty()) {
                return;
            }
            placeFromHand(player, handStack);
        } else if (handStack.isEmpty()) {
            ItemStack toReturn = egg.copy();
            egg = ItemStack.EMPTY;
            PlayerInventoryHelper.giveBack(player, hand, toReturn);
        } else {
            ItemStack toReturn = egg.copy();
            placeFromHand(player, handStack);
            PlayerInventoryHelper.giveBack(player, hand, toReturn);
        }
        syncToClient();
        setChanged();
    }

    private void placeFromHand(Player player, ItemStack handStack) {
        egg = handStack.copyWithCount(1);
        itemFacing = player.getDirection();
        if (!player.getAbilities().instabuild) {
            handStack.shrink(1);
        }
        hatchTicks = (int) (12000 / level.getBiome(worldPosition).value().getBaseTemperature());
    }

    private void syncToClient() {
        setChanged();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(worldPosition);
        }
    }

    public boolean hasItem() {
        return !egg.isEmpty();
    }

    public ItemStack getItem() {
        return egg;
    }

    public float getOffsetHeight() {
        return offsetHeight;
    }

    public Direction getItemFacing() {
        return itemFacing;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        if (!egg.isEmpty()) {
            nbt.put("egg", egg.save(registryLookup));
        } else {
            nbt.remove("egg");
        }
        nbt.putInt("hatchTicks", hatchTicks);
        nbt.putString("itemFacing", itemFacing.getSerializedName());
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        if (nbt.contains("egg")) {
            egg = ItemStack.parse(registryLookup, nbt.get("egg")).get();
        } else {
            egg = ItemStack.EMPTY;
        }

        hatchTicks = nbt.getInt("hatchTicks");
        if (nbt.contains("itemFacing")) {
            itemFacing = Direction.byName(nbt.getString("itemFacing"));
            if (itemFacing == null || !itemFacing.getAxis().isHorizontal()) {
                itemFacing = Direction.SOUTH;
            }
        } else {
            itemFacing = Direction.SOUTH;
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        CompoundTag nbtCompound = new CompoundTag();
        this.saveAdditional(nbtCompound, registryLookup);
        return nbtCompound;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
