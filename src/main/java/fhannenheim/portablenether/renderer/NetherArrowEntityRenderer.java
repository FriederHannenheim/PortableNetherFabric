package fhannenheim.portablenether.renderer;

import fhannenheim.portablenether.PortableNether;
import fhannenheim.portablenether.entities.NetherArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class NetherArrowEntityRenderer extends ProjectileEntityRenderer<NetherArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(PortableNether.MOD_ID,"textures/entity/nether_arrow_entity.png");

    public NetherArrowEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public Identifier getTexture(NetherArrowEntity entity) {
        return TEXTURE;
    }
}