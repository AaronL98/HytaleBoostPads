package io.github.AaronL98.HytaleBoostPads.events;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;


public class BoostPadSystem extends EntityTickingSystem<EntityStore> {

    static final String BOOST_PAD_BLOCK_ID = "Boost_Pad";

    @Override
    public void tick(float dt, int index, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store, CommandBuffer<EntityStore> cmd) {

        PlayerRef player = chunk.getComponent(index, PlayerRef.getComponentType());

        if (player == null) {
            return;
        }

        World world = cmd.getExternalData().getWorld();

        world.execute(() -> {
            Transform playerTransform = player.getTransform();

            Vector3d pos = playerTransform.getPosition();

            int bx = (int) Math.floor(pos.getX());
            int by = (int) Math.floor(pos.getY());
            int bz = (int) Math.floor(pos.getZ());

            BlockType blockType = world.getBlockType(bx, by, bz);

            if (blockType != null && blockType.getId().equals(BOOST_PAD_BLOCK_ID)) {
                player.sendMessage(Message.raw("You're standing on a Boost Pad"));
            }
        });

    }

    @Override
    @Nullable
    public Query<EntityStore> getQuery() {
        return Query.and(
                PlayerRef.getComponentType()//, NPCEntity.getComponentType()
        );
    }


}
