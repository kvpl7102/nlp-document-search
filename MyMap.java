import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 * IFT2015 - TP2
 * 
 * A custom HashMap implementation. This Map put the entries of
 * Key-Value in an array. At each index, the entries are stored
 * in a LinkedList. If an insertion of an entry causes the load
 * factor to go above 0.75, the array of entries will be resized.
 * 
 * @author Le Kinh Vi Phung - 20178538
 * @since December 2nd, 2022
 * 
 */
public class MyMap<K, V> implements Map<K, V> {

    private MapEntry<K, V>[] entries;

    private double LOAD_FACTOR = 0.75;
    private int capacity = 16;
    private int size = 0;

    private Set<K> keySet;
    private Set<V> values;
    private Set<Entry<K, V>> entrySet;

    @SuppressWarnings("unchecked")
    public MyMap() {
        this.entries = new MapEntry[capacity];
        this.keySet = new HashSet<>();
        this.values = new HashSet<>();
        this.entrySet = new HashSet<>();

    }

    public int getHash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    public int getEntriesCapacity() {
        return entries.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return (keySet.contains(key)) ? true : false;
    }

    @Override
    public boolean containsValue(Object value) {
        return (values.contains(value)) ? true : false;
    }

    @Override
    public V get(Object key) {
        MapEntry<K, V> e = entries[getHash(key) % getEntriesCapacity()];

        while (e != null) {
            if (e.getKey().equals(key)) {
                return e.getValue();
            }
            e = e.next;
        }
        return null;
    }

    public MapEntry<K, V> getEntry(K key) {
        MapEntry<K, V> e = entries[getHash(key) % getEntriesCapacity()];

        while (e != null) {
            if (e.getKey().equals(key)) {
                return e;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        // Rehashing the table if load factor > 0.75
        if (size == LOAD_FACTOR * capacity) {
            MapEntry<K, V>[] oldEntries = entries;
            capacity = capacity * 2 + 1;
            size = 0;

            entries = new MapEntry[capacity];

            for (MapEntry<K, V> e : oldEntries) {
                while (e != null) {
                    put(e.getKey(), e.getValue());
                    e = e.next;
                }
            }
        }

        MapEntry<K, V> newEntry = new MapEntry<K, V>(key, value, null);
        int index = getHash(key) % getEntriesCapacity();
        MapEntry<K, V> e = entries[index];

        if (e == null) {
            entries[index] = newEntry;
            size++;

            entrySet.add(newEntry);
            keySet.add(newEntry.getKey());
            values.add(newEntry.getValue());

        } else {
            while (e.next != null) {
                if (e.getKey().equals(key)) {

                    entrySet.add(e);
                    keySet.add(e.getKey());
                    values.add(e.getValue());

                    return (V) e.setValue(value);
                }
                e = e.next;
            }

            if (e.getKey().equals(key)) {

                entrySet.add(e);
                keySet.add(e.getKey());
                values.add(e.getValue());

                return (V) e.setValue(value);

            } else {
                e.next = newEntry;
                size++;

                entrySet.add(newEntry);
                keySet.add(newEntry.getKey());
                values.add(newEntry.getValue());
            }
        }

        return null;
    }

    @Override
    public V remove(Object key) {
        MapEntry<K, V> e = entries[getHash(key) % getEntriesCapacity()];

        if (e.getKey().equals(key)) {
            entries[getHash(key) % getEntriesCapacity()] = e.next;
            e.next = null;

            entrySet.remove(e);
            keySet.remove(e.getKey());
            values.remove(e.getValue());

            return e.getValue();

        } else {
            while (e != null) {
                MapEntry<K, V> prev = e;
                e = e.next;

                if (e.getKey().equals(key)) {
                    prev.next = e.next;
                    e.next = null;

                    entrySet.remove(e);
                    keySet.remove(e.getKey());
                    values.remove(e.getValue());

                    return e.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < getEntriesCapacity(); i++) {
            if (entries[i] != null) {
                entries[i] = null;
            }
            entrySet.removeAll(entrySet);
            keySet.remove(keySet);
            values.removeAll(values);
            size = 0;
        }
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

}
