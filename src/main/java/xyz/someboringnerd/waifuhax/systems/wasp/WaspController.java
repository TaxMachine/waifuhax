package xyz.someboringnerd.waifuhax.systems.wasp;

import imgui.ImGui;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.RenderImGuiEvent;
import xyz.someboringnerd.waifuhax.events.TickEvent;
import xyz.someboringnerd.waifuhax.util.PlayerUtils;
import xyz.someboringnerd.waifuhax.util.TpUtils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor()
public class WaspController {

    @Getter(AccessLevel.PUBLIC)
    private static WaspController instance;

    private int currentWaypoint = 0, tick = 0;

    private final int waypointAmount, minDistance, mapSize;

    private final Set<BlockPos> blockPosSet = new HashSet<>();

    private BlockPos current;

    private MinecraftClient client;

    @Getter(AccessLevel.PUBLIC)
    private boolean running = false;

    /**
     * Create a new Wasp instance
     *
     * @param waypointAmount number of points to create
     * @param minDistance    minimum distance between waypoints (in blocks)
     */
    public static void init(int waypointAmount, int minDistance, int mapSize) {
        instance = new WaspController(waypointAmount, minDistance, mapSize);

        instance.generateWaypoints();

        instance.client = MinecraftClient.getInstance();
    }

    protected void generateWaypoints() {
        blockPosSet.add(getBestNextCoordinate(null));

        for (int i = 0; i < waypointAmount; i++) {
            blockPosSet.add(getBestNextCoordinate(blockPosSet.stream().toList().get(i)));
        }

        current = blockPosSet.stream().toList().get(0);
        WaifuHax.EVENT_BUS.subscribe(this);
        running = true;
    }

    public void stop() {
        stop(false);
    }

    public void stop(boolean keep) {
        running = false;
        WaifuHax.EVENT_BUS.unsubscribe(this);
        instance = null;
    }

    private BlockPos getBestNextCoordinate(@Nullable BlockPos previous) {


        int min = -(mapSize / 2);
        int max = (mapSize / 2);

        int x = new SecureRandom().nextInt(min, max);
        int z = new SecureRandom().nextInt(min, max);

        BlockPos next = new BlockPos(x, 321, z);

        // reroll the blockpos recursively if it is too close from the previous one (assuming it is not null)
        if (previous != null && (next.getSquaredDistance(previous.toCenterPos()) < minDistance)) {
            next = getBestNextCoordinate(previous);
        }

        return next;
    }

    @EventHandler
    private void onTick(TickEvent event) {

        // note by SBN :
        /*  My bots run Rusherhack with an anti fly kick but it is not silent
         *  this code allow to re-teleport the player above the max build limit
         *  so it dont get stuck
         */
        if (client.player.getPos().y < 320)
            TpUtils.Tp(client.player.getPos().add(0, 5, 0));

        if (tick >= 10) {
            PlayerUtils.LookAt(current.toCenterPos());
            tick = -1;
        }

        if (!client.options.forwardKey.isPressed()) {
            client.options.forwardKey.setPressed(true);
        }

        // if we are less than 10 blocks away from our target
        if (Math.sqrt(current.getSquaredDistance(client.player.getPos())) <= 10) {
            currentWaypoint++;
            current = blockPosSet.stream().toList().get(currentWaypoint);
        }

        tick++;
    }

    @EventHandler
    private void onImguiRender(RenderImGuiEvent event) {
        if (ImGui.begin("Wasp System")) {

            float distance = (float) (Math.sqrt(current.getSquaredDistance(client.player.getPos())));

            long estimatedTimeLeft = (long) (distance / getPlayerSpeed().horizontalLength());

            ImGui.text(String.format("Current waypoint : %s/%s", currentWaypoint, waypointAmount));
            ImGui.text(String.format("Distance : %s", distance));
            ImGui.text(String.format("Player speed : %s", getPlayerSpeed().horizontalLength()));
            ImGui.text(String.format("Destination coordinates : x: %s z: %s", current.getX(), current.getZ()));
            ImGui.text(String.format("Estimated time left : %s", new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis() + (estimatedTimeLeft * 1000)))));
            ImGui.text(String.format("Raw estimated time left : %s", estimatedTimeLeft));
        }
        ImGui.end();
    }

    private Vec3d getPlayerSpeed() {
        if (instance.client.player == null) return Vec3d.ZERO;

        double tX = instance.client.player.getX() - instance.client.player.prevX;
        double tY = instance.client.player.getY() - instance.client.player.prevY;
        double tZ = instance.client.player.getZ() - instance.client.player.prevZ;

        tX *= 20;
        tY *= 20;
        tZ *= 20;

        return new Vec3d(tX, tY, tZ);
    }
}
