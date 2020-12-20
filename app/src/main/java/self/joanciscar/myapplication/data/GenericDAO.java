package self.joanciscar.myapplication.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface GenericDAO<T,K> {
    T getDetails(K key);
    List<T> getAllDetails();
    //List<T> getDetails(List<K> key);
    int update(T entity);
    int delete(T entity);
    long insert(T entity);

    default int updateAll(List<T> entities) {
        int i = 0;
        for(int x = 0; x < entities.size(); x++) {
            i += update(entities.get(x));
        }
        return i;
    }
    default int deleteAll(List<T> entities) {
        int i = 0;
        for(int x = 0; x < entities.size(); x++) {
            this.delete(entities.get(x));
        }
        return i;
    }
    @Deprecated
    default void insertAll(List<T> entities) {
        int i = 0;
        for(int x = 0; x < entities.size(); x++) {
            this.insert(entities.get(x));
        }
    }
    default List<T> getDetails(List<K> keys) {
        List<T> list = new ArrayList<>();
        for (K key: keys) {
            T item = this.getDetails(key);
            if(item != null) {
                list.add(item);
            }
        }
        return list;
    }

    default boolean exists(K key) {
        return this.getDetails(key) != null;
    }
    List<T> getCachedEntities();
    Map<K,Integer> getCachedPairKeyIndexMap();
}
