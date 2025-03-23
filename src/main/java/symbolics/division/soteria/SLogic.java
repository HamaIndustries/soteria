package symbolics.division.soteria;

import java.util.Collection;
import java.util.function.Predicate;

public final class SLogic {
    public static <T> boolean all(Collection<Predicate<T>> predicates, T v) {
        for (var p : predicates) if (!p.test(v)) return false;
        return true;
    }

    public static <T> boolean any(Collection<Predicate<T>> predicates, T v) {
        for (var p : predicates) if (p.test(v)) return true;
        return false;
    }
}
