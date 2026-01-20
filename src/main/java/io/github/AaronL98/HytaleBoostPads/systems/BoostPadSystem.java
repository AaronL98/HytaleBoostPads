package io.github.AaronL98.HytaleBoostPads.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.modules.splitvelocity.VelocityConfig;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jspecify.annotations.NonNull;

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
        EntityStore entityStore = world.getEntityStore();

        world.execute(() -> {
            Transform playerTransform = player.getTransform();

            Vector3d pos = playerTransform.getPosition();

            int bx = (int) Math.floor(pos.getX());
            int by = (int) Math.floor(pos.getY());
            int bz = (int) Math.floor(pos.getZ());

            BlockType blockType = world.getBlockType(bx, by, bz);

            int blockRotation = world.getBlockRotationIndex(bx, by, bz);

            if (blockType != null && blockType.getId().equals(BOOST_PAD_BLOCK_ID)) {

                Vector3d newVelocity = getVector3d(blockRotation, 40.0);
                Velocity playerVelocity = chunk.getComponent(index, Velocity.getComponentType());

                if (playerVelocity == null) {
                    return;
                }

                VelocityConfig velocityConfig = new VelocityConfig();

                playerVelocity.addInstruction(newVelocity, velocityConfig, ChangeVelocityType.Set);
                int soundIndex = SoundEvent.getAssetMap().getIndex("SFX_Avatar_Powers_Disable_Local");
                SoundUtil.playSoundEvent3dToPlayer(player.getReference(), soundIndex, SoundCategory.SFX, pos, entityStore.getStore());
            }
        });

    }

    private static @NonNull Vector3d getVector3d(int blockRotation, double scale) {
        Vector3d dir = switch (blockRotation) {
            case 0 -> // north
                    new Vector3d(0, 0, -1);
            case 1 -> // west
                    new Vector3d(-1, 0, 0);
            case 2 -> // south
                    new Vector3d(0, 0, 1);
            case 3 -> // east
                    new Vector3d(1, 0, 0);
            default -> new Vector3d(0, 0, 0);
        };

        return dir.scale(scale);
    }

    @Override
    @Nullable
    public Query<EntityStore> getQuery() {
        return Query.and(
                PlayerRef.getComponentType()//, NPCEntity.getComponentType()
        );
    }


}
