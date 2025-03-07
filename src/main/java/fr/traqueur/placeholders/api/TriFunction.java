package fr.traqueur.placeholders.api;


/**
 * Represents a function that accepts three arguments and produces a result.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <V> the type of the third argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first argument
     * @param u the second argument
     * @param v the third argument
     * @return the function result
     */
    R apply(T t, U u, V v);

}