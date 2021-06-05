package fhannenheim.portablenether.entities;

import com.sun.jna.platform.win32.WinDef;
import fhannenheim.portablenether.PortableNether;
import fhannenheim.portablenether.items.ItemRegistry;
import fhannenheim.portablenether.networking.EntitySpawnPacket;
import fhannenheim.portablenether.util.Teleporter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class NetherArrowEntity extends PersistentProjectileEntity {
    public NetherArrowEntity(EntityType<NetherArrowEntity> netherArrowEntityEntityType, World world) {
        super(EntityRegistry.NETHER_ARROW_ENTITY, world);
        setProperties();
    }

    public NetherArrowEntity(World world, LivingEntity owner) {
        super(EntityRegistry.NETHER_ARROW_ENTITY, owner, world);
        setProperties();
    }

    public NetherArrowEntity(World world) {
        super(EntityRegistry.NETHER_ARROW_ENTITY, world);
        setProperties();
    }

    public NetherArrowEntity(World world, double x, double y, double z) {
        super(EntityRegistry.NETHER_ARROW_ENTITY, x, y, z, world);
        setProperties();
    }

    void setProperties() {
        this.setDamage(0);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ItemRegistry.NETHER_ARROW);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround) {
            if (this.inGroundTime % 5 == 0) {
                this.spawnParticles(1);
            }
        } else {
            this.spawnParticles(2);
        }
    }

    private void spawnParticles(int i) {
        for (int k = 0; k < i; ++k) {
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0.353, 0.035, 0.642);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!entityHitResult.getEntity().world.isClient && entityHitResult.getEntity() instanceof PlayerEntity)
            // I have to teleport the entities at the end of the tick otherwise a IllegalStateException: Removing entity while ticking! would be thrown
            PortableNether.to_teleport.add((PlayerEntity)entityHitResult.getEntity());
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        if (!target.world.isClient)
            // I have to teleport the entities at the end of the tick otherwise a IllegalStateException: Removing entity while ticking! would be thrown
            PortableNether.to_teleport.add(target);
    }

    @Override
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, PortableNether.SPAWN_PACKET_ID);
    }
}
