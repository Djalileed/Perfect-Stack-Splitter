package dz.ratcommit.perfectstacksplitter.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SplitStackPacket {
    private final int slotId;
    private final int splitAmount;
    private final CompoundTag originalSlotStackTag;
    private final CompoundTag originalCarriedStackTag;

    public SplitStackPacket(int slotId, int splitAmount, ItemStack originalCarriedStack, ItemStack originalSlotStack) {
        this.slotId = slotId;
        this.splitAmount = splitAmount;
        this.originalSlotStackTag = originalSlotStack.save(new CompoundTag());
        this.originalCarriedStackTag = originalCarriedStack.save(new CompoundTag());

    }

    public SplitStackPacket(FriendlyByteBuf buffer) {
        this.slotId = buffer.readInt();
        this.splitAmount = buffer.readInt();
        this.originalSlotStackTag = buffer.readNbt();
        this.originalCarriedStackTag = buffer.readNbt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(slotId);
        buffer.writeInt(splitAmount);
        buffer.writeNbt(originalSlotStackTag);
        buffer.writeNbt(originalCarriedStackTag);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu != null) {
                ItemStack originalCarriedStack = ItemStack.of(originalCarriedStackTag);
                ItemStack originalSlotStack = ItemStack.of(originalSlotStackTag);

                if (!originalCarriedStackTag.isEmpty() && !originalSlotStackTag.isEmpty() && splitAmount > 0 && splitAmount <= (originalSlotStack.getCount() + originalCarriedStack.getCount())) {
                    ItemStack splitStack = originalCarriedStack.copy();
                    splitStack.setCount(splitAmount);
                    originalSlotStack.setCount(originalSlotStack.getCount() + originalCarriedStack.getCount() - splitAmount);
                    Slot slot = player.containerMenu.getSlot(slotId);
                    slot.set(originalSlotStack);
                    player.containerMenu.setCarried(splitStack);
                    player.containerMenu.broadcastChanges();
                }
            }
        });
        context.setPacketHandled(true);
    }
}

