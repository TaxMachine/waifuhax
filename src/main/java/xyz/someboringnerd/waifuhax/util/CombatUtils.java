package xyz.someboringnerd.waifuhax.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class CombatUtils {

    private static final MinecraftClient mc = MinecraftClient.getInstance();


    public static double LowestHealth(Entity entity) {
        return ((LivingEntity) entity).getHealth();
    }

    public static double Closest(Entity entity) {
        return mc.player.squaredDistanceTo(entity);
    }

    public static double ClosestCrosshair(Entity entity) {
        return MathUtils.Angle(mc.player.getPos(), entity.getPos());
    }

    @Nullable
    public static Entity GetTarget(Iterable<? extends Entity> entities, Function<Entity, Boolean> validFunction, Function<Entity, Double> scoreFunction, List<Entity> avoid) {
        Entity best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        for (Entity entity : entities) {
            if (mc.player == entity) continue;
            if (!validFunction.apply(entity)) continue;
            double score = scoreFunction.apply(entity);
            if (score < bestScore) {
                bestScore = score;
                best = entity;
            }
        }
        return best;
    }

    @Nullable
    public static LivingEntity GetLivingTarget(Function<Entity, Double> scoreFunction, double range) {
        return GetLivingTarget(scoreFunction, List.of(), range);
    }

    @Nullable
    public static LivingEntity GetLivingTarget(Function<Entity, Double> scoreFunction, List<Entity> avoid, double range) {
        return (LivingEntity) GetTarget(mc.world.getEntities(),
                entity -> entity.isLiving() &&
                        entity.isAlive() &&
                        !((LivingEntity) entity).isDead() &&
                        mc.player.squaredDistanceTo(entity) <= range * range &&
                        !avoid.contains(entity),
                scoreFunction, avoid);
    }

    @Nullable
    public static PlayerEntity GetPlayerTarget(Function<Entity, Double> scoreFunction, double range) {
        return GetPlayerTarget(scoreFunction, null, List.of(), range);
    }

    @Nullable
    public static PlayerEntity GetPlayerTarget(Function<Entity, Double> scoreFunction, List<Entity> avoid, double range) {
        return GetPlayerTarget(scoreFunction, null, avoid, range);
    }

    @Nullable
    public static PlayerEntity GetPlayerTarget(Function<Entity, Double> scoreFunction, Function<PlayerEntity, Boolean> validFunction, List<Entity> avoid, double range) {
        return (PlayerEntity) GetTarget(mc.world.getPlayers(),
                entity -> entity.isLiving() &&
                        entity.isAlive() &&
                        !((LivingEntity) entity).isDead() &&
                        mc.player.squaredDistanceTo(entity) <= range * range &&
                        !avoid.contains(entity) &&
                        (validFunction == null || validFunction.apply((PlayerEntity) entity)),
                scoreFunction, avoid);
    }

    public static boolean validDamages(float selfDamage, float damage, float maxSelf, float min, boolean noSuicide) {
        if (noSuicide) return damage >= min && selfDamage <= maxSelf && mc.player.getHealth() > selfDamage;
        else return damage >= min;
    }

    public static float ExplosionDamage(Vec3d pos, Entity entity, float power) {
        if (mc.world.getDifficulty() == Difficulty.PEACEFUL) return 0;

        double w = pos.distanceTo(entity.getPos()) / (power * 2f);
        if (w > 1f) return 0;

        float exposure = Explosion.getExposure(pos, entity);
        float damage = (float) ((1f - w) * exposure);
        damage = (float) ((int) ((damage * damage + damage) / 2f * 7f * power * 2f + 1f));

        switch (mc.world.getDifficulty()) {
            case EASY -> damage = Math.min(damage / 2f + 1f, damage);
            case HARD -> damage = damage * 3f / 2f;
        }

        StatusEffectInstance effect = mc.player.getStatusEffect(StatusEffects.RESISTANCE);
        if (effect != null) {
            int resistance = (int) ((effect.getAmplifier() + 1f) * 5f);
            damage = Math.max(0f, (25f - resistance) * damage / 25f);
        }

        return damage;
    }
}
