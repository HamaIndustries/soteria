package symbolics.division.soteria.api;

public interface Unliving {
    default boolean soteria$unliving() {
        return false;
    }

    default void soteria$disperse() {
    }

    default boolean soteria$ephemeral() {
        return false;
    }

    default void soteria$materialize() {
    }

    default float soteria$residue() {
        return 0;
    }
}
