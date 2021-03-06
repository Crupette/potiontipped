package com.github.Crupette.potiontipped.item;

import com.github.Crupette.potiontipped.PotionTipped;
import com.github.Crupette.potiontipped.util.TippedItemUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class TippedSwordItem extends SwordItem implements TippedTool{
    private final SwordItem parent;
    private final TippedItemUtil.TippedSide type;

    public TippedSwordItem(SwordItem parent, TippedItemUtil.TippedSide type) {
        super(parent.getMaterial(),
                (int) (parent.getAttackDamage() - parent.getMaterial().getAttackDamage()),
                (float) parent.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED).toArray(new EntityAttributeModifier[] {})[0].getValue(),
                new Settings().maxDamage(parent.getMaxDamage()));

        this.parent = parent;
        this.type = type;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if(group.equals(PotionTipped.TIPPED_GROUP)){
            Registry.POTION.forEach((potion -> {
                if(!potion.getEffects().isEmpty() && !potion.hasInstantEffect()){
                    TippedItemUtil.expandStacks(this, potion, stacks, this.type);
                }
            }));
        }
    }

    @Override
    public String getTranslationKey() {
        return this.parent.getTranslationKey();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        TippedItemUtil.appendTooltips(stack, tooltip);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        TippedItemUtil.postHit(stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(selected && entity instanceof LivingEntity){
            TippedItemUtil.inventoryTick(stack, (LivingEntity) entity, slot);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return parent.getMiningSpeedMultiplier(stack, state);
    }

    @Override
    public boolean isEffectiveOn(BlockState state) {
        return parent.isEffectiveOn(state);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        parent.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return parent.canMine(state, world, pos, miner);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return parent.useOnBlock(context);
    }

    @Override
    public Item getParent() {
        return this.parent;
    }

    @Override
    public TippedItemUtil.TippedSide getType() {
        return this.type;
    }
}
