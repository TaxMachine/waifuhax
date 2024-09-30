package xyz.someboringnerd.waifuhax.systems.modules.impl.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import xyz.someboringnerd.waifuhax.events.TickEvent;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.AutoDisable;

import static java.lang.Math.abs;


/**
 * skidded from Mioclient
 * <br>
 *
 * @author : cattyngmd
 * @url : <a href="https://github.com/mioclient/hitbox-desync">...</a>
 */
@AutoDisable
public class HitboxDesync extends AbstractModule {
    private static final double MAGIC_OFFSET = .200009968835369999878673424677777777777761;

    public HitboxDesync() {
        super(CATEGORY.COMBAT);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.world == null) return;
        // take player direction
        Direction f = mc.player.getHorizontalFacing();

        // get bounding box
        Box bb = mc.player.getBoundingBox();

        // center of the block
        Vec3d center = bb.getCenter();
        Vec3d offset = new Vec3d(f.getUnitVector());

        // idk
        Vec3d fin = merge(Vec3d.of(BlockPos.ofFloored(center)).add(.5, 0, .5).add(offset.multiply(MAGIC_OFFSET)), f);
        mc.player.setPosition(fin.x == 0 ? mc.player.getX() : fin.x,
                mc.player.getY(),
                fin.z == 0 ? mc.player.getZ() : fin.z);
        toggle();
    }

    private Vec3d merge(Vec3d a, Direction facing) {
        return new Vec3d(a.x * abs(facing.getUnitVector().x()), a.y * abs(facing.getUnitVector().y()), a.z * abs(facing.getUnitVector().z()));
    }

    @Override
    public String getDescription() {
        return "\"Crashes chinese crystal auras\" - cattyngmd";
    }
}
