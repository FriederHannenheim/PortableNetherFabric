package fhannenheim.portablenether;

import fhannenheim.portablenether.entities.EntityRegistry;
import fhannenheim.portablenether.entities.NetherArrowEntity;
import fhannenheim.portablenether.items.ItemRegistry;
import fhannenheim.portablenether.items.NetherArrow;
import fhannenheim.portablenether.networking.EntitySpawnPacket;
import fhannenheim.portablenether.renderer.NetherArrowEntityRenderer;
import fhannenheim.portablenether.util.Teleporter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PortableNether implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "portablenether";
    public static final Identifier SPAWN_PACKET_ID = new Identifier(MOD_ID, "spawn_packet");
    public static Logger LOGGER;
    public static final List<LivingEntity> to_teleport = new ArrayList<>();

    @Override
    public void onInitialize() {
        LOGGER = LogManager.getLogger(getClass().getName());

        ItemRegistry.register();
        EntityRegistry.register();

        // Teleports entities at the end of the tick
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            while (!to_teleport.isEmpty()){
                Teleporter.teleport(to_teleport.get(0));
                to_teleport.remove(0);
            }
        });

        DispenserBlock.registerBehavior(ItemRegistry.NETHER_ARROW, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return new NetherArrowEntity(world, position.getX(), position.getY(), position.getZ());
            }
        });
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.NETHER_ARROW_ENTITY, (dispatcher, context) -> new NetherArrowEntityRenderer(dispatcher));

        ClientSidePacketRegistry.INSTANCE.register(SPAWN_PACKET_ID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.pitch = pitch;
                e.yaw = yaw;
                e.setEntityId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}
