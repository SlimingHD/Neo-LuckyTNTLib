package luckytntlib.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Similar to {@link DeferredItem} and {@link DeferredBlock}, but holds an EntityType
 * I don't know why this doesn't exist already, as it's a very common use case
 * @param <T> Entity
 */
public class DeferredEntity<T extends EntityType<?>> extends DeferredHolder<EntityType<?>, T>{

	protected DeferredEntity(ResourceKey<EntityType<?>> key) {
		super(key);
	}
	
	/**
	 * Casts this DeferredEntity to a {@link DeferredHolder}, if that is required
	 * @return
	 */
	public DeferredHolder<EntityType<?>, T> asHolder() {
		return (DeferredHolder<EntityType<?>, T>)this;
	}
}
