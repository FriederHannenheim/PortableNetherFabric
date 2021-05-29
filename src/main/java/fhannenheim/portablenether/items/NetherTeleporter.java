package fhannenheim.portablenether.items;

import fhannenheim.portablenether.util.Teleporter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class NetherTeleporter extends Item {

    public NetherTeleporter(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Teleporter.teleport(user);
        stack.decrement(1);
        user.getItemCooldownManager().set(this, 20);
        return TypedActionResult.consume(stack);
    }
}
