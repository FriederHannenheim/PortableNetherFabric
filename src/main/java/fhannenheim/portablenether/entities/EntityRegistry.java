package fhannenheim.portablenether.entities;

import fhannenheim.portablenether.PortableNether;
import fhannenheim.portablenether.items.NetherArrow;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.beans.PersistenceDelegate;

public class EntityRegistry {
    public static final EntityType<NetherArrowEntity> NETHER_ARROW_ENTITY = FabricEntityTypeBuilder.<NetherArrowEntity>create(SpawnGroup.MISC, NetherArrowEntity::new)
            .dimensions(EntityDimensions.fixed(0.2f,0.2f))
            .trackRangeBlocks(4).trackedUpdateRate(10)
            .build();

    public static void register(){
        Registry.register(Registry.ENTITY_TYPE,new Identifier(PortableNether.MOD_ID,"nether_arrow_entity"),NETHER_ARROW_ENTITY);
    }

}
