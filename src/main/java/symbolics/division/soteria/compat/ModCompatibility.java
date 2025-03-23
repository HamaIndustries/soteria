package symbolics.division.soteria.compat;

import net.fabricmc.loader.api.FabricLoader;
import symbolics.division.soteria.Soteria;

import java.lang.reflect.InvocationTargetException;

public interface ModCompatibility {

    void initialize(String modid, boolean inDev);

    String COMPAT_PACKAGE_PREFIX = "symbolics.division.soteria.compat.";

    private static void tryInit(String compatClass, String modRef) {
        var loader = FabricLoader.getInstance();
        if (!loader.isModLoaded(modRef)) {
            Soteria.LOGGER.debug("Did not find mod " + modRef + ", skipping compat check");
            return;
        }

        Soteria.LOGGER.debug("Loading compat for mod " + modRef);

        String className = COMPAT_PACKAGE_PREFIX + compatClass;
        ModCompatibility compat;
        try {
            compat = (ModCompatibility) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException err) {
            Soteria.LOGGER.error("Unable to find compatibility class: " + className);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                throw new RuntimeException(err);
            }
            return;
        } catch (NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException |
                 IllegalAccessException | ClassCastException err) {
            // you can tell from the number of exceptions we handle here that our code
            // is robust and sensible
            Soteria.LOGGER.error("Failed to run constructor for: " + className);
            if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                throw new RuntimeException(err);
            }
            return;
        }
        compat.initialize(modRef, FabricLoader.getInstance().isDevelopmentEnvironment());
    }

    static void warnCompatBroken(String modid) {
        Soteria.LOGGER.warn(Soteria.MOD_ID + " + " + modid + " integration failed. If you " +
                "are using the latest version of both, please notify the dev of " + Soteria.MOD_ID + ".");
    }

    static void alertRuntimeCompatBroken(String modid) {
        Soteria.LOGGER.error("Mod " + modid + " probably had an API change not caught " +
                "in the compatibility loading step. If you are using the latest version of both, " +
                "please report to the author of " + Soteria.MOD_ID + ".");
    }

    static void init() {
        Soteria.LOGGER.debug("Loading " + Soteria.MOD_ID + " mod compatibilities");
        tryInit("MarksmanCompat", "marksman");
    }
}

