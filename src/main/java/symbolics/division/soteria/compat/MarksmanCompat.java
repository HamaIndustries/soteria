package symbolics.division.soteria.compat;

import archives.tater.marksman.Ricoshottable;
import symbolics.division.soteria.entity.PoiseSpark;

public class MarksmanCompat implements ModCompatibility {
    @Override
    public void initialize(String modid, boolean inDev) {
        // entityhitcallback: handle behavior on entity hit
        // if enttity instance of richoshottable,
        PoiseSpark.registerPredicate(Ricoshottable::canBeRicoshotted);
    }
}
