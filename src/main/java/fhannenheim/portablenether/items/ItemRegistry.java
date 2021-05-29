package fhannenheim.portablenether.items;

import fhannenheim.portablenether.PortableNether;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final Item NETHER_TELEPORTER = new NetherTeleporter((new FabricItemSettings().group(ItemGroup.MISC)));
    public static final Item NETHER_ARROW = new NetherArrow((new FabricItemSettings().group(ItemGroup.MISC)));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(PortableNether.MOD_ID, "nether_teleporter"), NETHER_TELEPORTER);
        Registry.register(Registry.ITEM, new Identifier(PortableNether.MOD_ID, "nether_arrow"), NETHER_ARROW);
    }
}
