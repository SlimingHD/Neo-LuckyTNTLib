package luckytntlib.registry;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Similar to {@link Blocks} and {@link Items}, but returns {@link DeferredEntity} upon registering
 * I don't know why this doesn't exist already, as it's a very common use case
 */
public class DeferredEntityRegister extends DeferredRegister<EntityType<?>> {

	protected DeferredEntityRegister(String modid) {
		super(Registries.ENTITY_TYPE, modid);
	}

	/**
	 * Creates a new DeferredEntityRegister, which can be used to register entities
	 * @param modid The namespace of this registry
	 * @return A {@link DeferredEntityRegister} to be used 
	 */
	public static DeferredEntityRegister createEntityRegistry(String modid) {
		return new DeferredEntityRegister(modid);
	}
	
    /**
     * Adds a new entity to the list of entries to be registered and returns a {@link DeferredEntity} that will be populated with the created entity automatically.
     *
     * @param name The new entitie's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param entity The Supplier for the entity to create a {@link DeferredEntity} for.
     * @return A {@link DeferredEntity} that will track updates from the registry for this entity.
     * @see #register(String, Supplier)
     */
	@SuppressWarnings("unchecked")
	public <T extends EntityType<?>> DeferredEntity<T> registerEntity(String name, Supplier<? extends T> entity) {
		return (DeferredEntity<T>)super.register(name, key -> entity.get());
	}
}
