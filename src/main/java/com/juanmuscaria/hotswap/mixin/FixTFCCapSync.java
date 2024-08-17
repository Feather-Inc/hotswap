package com.juanmuscaria.hotswap.mixin;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.capabilities.ItemStackCapabilitySync;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: Remove once https://github.com/TerraFirmaCraft/TerraFirmaCraft/issues/2753 is closed
public class FixTFCCapSync {
    private static final String CAP_PREFIX = TerraFirmaCraft.MOD_ID + ":";

    @Mixin(value = ItemStackCapabilitySync.class, remap = false)
    public static abstract class ItemStackCapabilitySyncMixin {
        /**
         * @author juanmuscaria
         * @reason Temp fix, see <a href="https://github.com/TerraFirmaCraft/TerraFirmaCraft/issues/2753" /a>
         */
        @Overwrite
        public static void writeToNetwork(ItemStack stack, FriendlyByteBuf buffer) {
            // NO-OP: Effectively remove TFC's original hook
        }

        /**
         * @author juanmuscaria
         * @reason Temp fix, see <a href="https://github.com/TerraFirmaCraft/TerraFirmaCraft/issues/2753" /a>
         */
        @Overwrite
        public static void readFromNetwork(ItemStack stack, FriendlyByteBuf buffer) {
            // Effectively replaces TFC's original hook
            hotswap$readFromNetwork(FoodCapability.NETWORK_CAPABILITY, stack, buffer);
            hotswap$readFromNetwork(HeatCapability.NETWORK_CAPABILITY, stack, buffer);
        }

        @Unique
        private static void hotswap$readFromNetwork(Capability<? extends INBTSerializable<CompoundTag>> capability, ItemStack stack, FriendlyByteBuf buffer) {
            final CompoundTag tag = stack.getTagElement(CAP_PREFIX + capability.getName());
            if (tag != null) {
                stack.removeTagKey(capability.getName());
                stack.getCapability(capability).ifPresent(cap -> cap.deserializeNBT(tag));
            }
        }
    }

    @Mixin(FriendlyByteBuf.class)
    public static abstract class FriendlyByteBufMixin {
        @Unique
        private static void hotswap$writeToNetwork(Capability<? extends INBTSerializable<CompoundTag>> capability, ItemStack stack, CompoundTag compoundTag) {
            stack.getCapability(capability)
                .map(INBTSerializable::serializeNBT)
                .ifPresent(nbt -> compoundTag.put(CAP_PREFIX + capability.getName(), nbt));
        }

        @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
        @Redirect(method = "writeItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeNbt(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/network/FriendlyByteBuf;"), remap = false)
        private FriendlyByteBuf addCapData(FriendlyByteBuf instance, CompoundTag compoundTag, ItemStack stack, boolean limitedTag) {
            synchronized (stack) {
                if (ItemStackCapabilitySync.hasSyncableCapability(stack)) {
                    if (compoundTag == null) {
                        // Safe to create a blank tag and inject data there
                        compoundTag = new CompoundTag();
                    } else {
                        // We need to copy the original tag to avoid mutating the item stack with injected capability data!
                        compoundTag = compoundTag.copy();
                    }
                    hotswap$writeToNetwork(FoodCapability.NETWORK_CAPABILITY, stack, compoundTag);
                    hotswap$writeToNetwork(HeatCapability.NETWORK_CAPABILITY, stack, compoundTag);
                }
            }

            return instance.writeNbt(compoundTag);
        }
    }
}
