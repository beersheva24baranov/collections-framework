package util;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Collection<T> extends Iterable<T> {
    boolean add(T obj);

    boolean remove(T pattern);

    int size();

    boolean contains(T pattern);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<T> parallel() {
        return StreamSupport.stream(spliterator(), true);
    }

    default boolean removeIf (Predicate<T> predicate) {
        int oldSize = size();
        Iterator<T> it = iterator();
        T obj = null;
        while (it.hasNext()) {
            obj = it.next();
            if (predicate.test(obj)) {
                it.remove();
            }
        }
        return size() < oldSize;
    }

    default void clear() {
        removeIf(n -> true);
    }

    default boolean isEmpty() {
        return size() == 0;
    }
}