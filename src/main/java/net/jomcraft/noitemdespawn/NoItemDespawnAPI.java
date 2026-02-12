package net.jomcraft.noitemdespawn;

public class NoItemDespawnAPI {

    //TODO: Maybe remove!
    public static boolean changeDSTime(Integer value, boolean runtime) {

        Integer current = NoItemDespawn.instance().despawnTime;
        if (runtime) {

            if ((current == Integer.MAX_VALUE && value > 0) || (current > 0 && value < 0))
                return false;
            try {

                if (value < 0)
                    value = Integer.MAX_VALUE;
                NoItemDespawn.instance().despawnTime = value;
                NoItemDespawn.getConfig().getInstance().get("Main", "Despawn-cooldown", 6000, "The custom despawn-cooldown of dropped items.").set(value);
                NoItemDespawn.getConfig().getInstance().save();
                NoItemDespawn.getConfig().syncConfiguration();
                return true;

            } catch (Exception e) {

            }

        } else {
            try {

                NoItemDespawn.getConfig().getInstance().get("Main", "Despawn-cooldown", 6000, "The custom despawn-cooldown of dropped items.").set(value);
                NoItemDespawn.getConfig().getInstance().save();
                return true;

            } catch (Exception e) {

            }

        }

        return false;

    }

    //TODO: Maybe remove!
    public static Integer currentCooldown() {
        Integer cooldown = NoItemDespawn.instance().despawnTime;
        if (cooldown == Integer.MAX_VALUE)
            cooldown = -1;
        //This maybe needs some investigation, due to not being perfect and abuseable.
        return cooldown;
    }

}
