package xyz.someboringnerd.waifuhax.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class BlockUtils {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static final List<Block> ORES = new ArrayList<>();

    static {
        ORES.add(Blocks.COAL_ORE);
        ORES.add(Blocks.COPPER_ORE);
        ORES.add(Blocks.IRON_ORE);
        ORES.add(Blocks.LAPIS_ORE);
        ORES.add(Blocks.GOLD_ORE);
        ORES.add(Blocks.REDSTONE_ORE);
        ORES.add(Blocks.DIAMOND_ORE);
        ORES.add(Blocks.EMERALD_ORE);
        ORES.add(Blocks.DEEPSLATE_COAL_ORE);
        ORES.add(Blocks.DEEPSLATE_COPPER_ORE);
        ORES.add(Blocks.DEEPSLATE_IRON_ORE);
        ORES.add(Blocks.DEEPSLATE_LAPIS_ORE);
        ORES.add(Blocks.DEEPSLATE_GOLD_ORE);
        ORES.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        ORES.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        ORES.add(Blocks.DEEPSLATE_EMERALD_ORE);
    }

    public static final List<Block> LEAVES = new ArrayList<>();

    static {
        LEAVES.add(Blocks.AZALEA_LEAVES);
        LEAVES.add(Blocks.FLOWERING_AZALEA_LEAVES);
        LEAVES.add(Blocks.OAK_LEAVES);
        LEAVES.add(Blocks.BIRCH_LEAVES);
        LEAVES.add(Blocks.SPRUCE_LEAVES);
        LEAVES.add(Blocks.DARK_OAK_LEAVES);
        LEAVES.add(Blocks.JUNGLE_LEAVES);
        LEAVES.add(Blocks.ACACIA_LEAVES);
        LEAVES.add(Blocks.MANGROVE_LEAVES);
        LEAVES.add(Blocks.CHERRY_LEAVES);
    }

    public static final List<Block> LOGS = new ArrayList<>();

    static {
        LOGS.add(Blocks.OAK_LOG);
        LOGS.add(Blocks.BIRCH_LOG);
        LOGS.add(Blocks.SPRUCE_LOG);
        LOGS.add(Blocks.DARK_OAK_LOG);
        LOGS.add(Blocks.JUNGLE_LOG);
        LOGS.add(Blocks.ACACIA_LOG);
        LOGS.add(Blocks.MANGROVE_LOG);
        LOGS.add(Blocks.CHERRY_LOG);
    }

    @Nullable
    public static BlockPos FindBlockPos(Function<BlockPos, Boolean> valid, Function<BlockPos, Float> getScore, BlockPos origin, float radius) {
        List<BlockPos> valids = new ArrayList<>();
        int r = (int) Math.ceil(radius);
        for (int y = -r; y <= r; y++) {
            for (int z = -r; z <= r; z++) {
                for (int x = -r; x <= r; x++) {
                    BlockPos pos = origin.add(x, y, z);
                    if (valid.apply(pos)) {
                        if (getScore == null) return pos;
                        valids.add(pos);
                    }
                }
            }
        }

        BlockPos best = null;
        float bestScore = Float.POSITIVE_INFINITY;
        for (BlockPos pos : valids) {
            float score = getScore.apply(pos);
            if (score < bestScore) {
                best = pos;
                bestScore = score;
            }
        }
        return best;
    }

    @Nullable
    public static BlockPos findFirstSafeSpaceOnTopOfPlayer() {
        ClientPlayerEntity self = MinecraftClient.getInstance().player;
        World world = self.getWorld();

        BlockPos tmp = self.getBlockPos();

        for (int y = self.getBlockY(); y < 317; y++) {
            tmp = tmp.add(0, 1, 0);

            if (TpUtils.cannotGo(tmp) && TpUtils.canGo(tmp.add(0, 1, 0)) && TpUtils.canGo(tmp.add(0, 2, 0))) {
                return tmp;
            }
        }

        return null;
    }

    @Nullable
    public static BlockPos findFirstSafeSpaceUnderOfPlayer() {
        ClientPlayerEntity self = MinecraftClient.getInstance().player;
        World world = self.getWorld();

        BlockPos tmp = self.getBlockPos();
        tmp = tmp.add(0, -2, 0);
        for (int y = self.getBlockY(); y > -64; y--) {
            tmp = tmp.add(0, -1, 0);

            if (TpUtils.cannotGo(tmp) && TpUtils.canGo(tmp.add(0, 1, 0)) && TpUtils.canGo(tmp.add(0, 2, 0))) {
                return tmp;
            }
        }

        return null;
    }

    public static boolean isBlockShape(BlockPos blockPos) {
        VoxelShape shape = mc.world.getBlockState(blockPos).getCollisionShape(mc.world, blockPos);
        return shape.equals(VoxelShapes.fullCube());
    }

    @Nullable
    public static Block fromName(String name) {
        AtomicReference<Block> found = new AtomicReference<>();
        for (Field field : Blocks.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                try {
                    Block block = (Block) field.get(null);
                    if (name.equalsIgnoreCase(block.getName().getString().replaceAll("\\s", "")) ||
                            name.equalsIgnoreCase(block.getTranslationKey().substring(6))) {
                        found.set(block);
                        break;
                    }
                } catch (IllegalAccessException ignored) {
                } // its static
            }
        }
        return found.get();
    }

    public static Pair<BlockPos, BlockPos> findMinable(BlockPos origin, Block blockType, float radius) {
        Pair<BlockPos, BlockPos> result = new Pair<>(null, null);
        result.setLeft(BlockUtils.FindBlockPos(blockPos -> {
            if (TpUtils.canGo(blockPos) &&
                    TpUtils.canGo(blockPos.up()) &&
                    !TpUtils.canGo(blockPos.down()) && BlockUtils.isBlockShape(blockPos.down())) {

                BlockPos pos = blockPos.up(2);
                if (mc.world.getBlockState(pos).getBlock() == blockType && isSafeToMine(pos)) {
                    result.setRight(pos);
                    return true;
                }
                for (int i = 0; i <= 1; i++) {
                    for (Direction dir : MathUtils.HORIZONTAL_DIR) {
                        pos = blockPos.offset(dir).up(i);
                        if (mc.world.getBlockState(pos).getBlock() == blockType && isSafeToMine(pos)) {
                            result.setRight(pos);
                            return true;
                        }
                    }
                }
            }
            return false;
        }, blockPos -> (float) origin.getSquaredDistance(blockPos), origin, radius));
        return result;
    }

    public static boolean isSafeToMine(BlockPos pos) {
        if (mc.world.getBlockState(pos).getFluidState().isIn(FluidTags.WATER)) return false;
        for (Direction dir : Direction.values()) {
            if (!mc.world.getBlockState(pos.offset(dir)).getFluidState().isEmpty()) return false;
        }
        return true;
    }
}
