
package com.gamezone.persistence;
import java.util.List;

public interface Repository <T> {
    void saveAll(List<T> items);
    List<T> loadAll();
}
