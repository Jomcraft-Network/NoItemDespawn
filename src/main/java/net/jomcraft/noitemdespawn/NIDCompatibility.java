package net.jomcraft.noitemdespawn;

public class NIDCompatibility {

    private static boolean isCauldron = false;

    public static boolean isCauldronServer() {

        if (!isCauldron) {
            try {
                if (Class.forName("thermos.ThermosClassTransformer") != null) {

                    isCauldron = true;
                    return true;

                }
            } catch (ClassNotFoundException e) {

            }
            return false;
        }
        return isCauldron == true;
    }

}
