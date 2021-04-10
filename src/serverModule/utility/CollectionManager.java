package serverModule.utility;

import common.data.SpaceMarine;
import common.data.Weapon;
import common.exceptions.DatabaseManagerException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private TreeMap<Integer, SpaceMarine> marines = new TreeMap<>();
    private LocalDateTime lastInitTime;
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection();
    }

    /**
     * @return The collection itself.
     */
    public TreeMap<Integer, SpaceMarine> getCollection() {
        return marines;
    }

    /**
     * Adds a new marine to collection.
     * @param marine A marine to add.
     */
    public void addToCollection(int key, SpaceMarine marine) {
        marines.put(key, marine);
    }

    /**
     * Generates next ID. It will be (the bigger one + 1).
     * @return Next ID.
     */
    public int generateId() {
        if (marines.isEmpty()) return 1;
        int lastId = 0;
        for (SpaceMarine marine : marines.values()) {
            lastId = Math.max(lastId, marine.getId());
        }
        return lastId + 1;
    }


    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return marines.getClass().getName();
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        marines.clear();
    }

    /**
     * @param marineToCompare The marine used to compare with others.
     */
    public List<SpaceMarine> getGreater(SpaceMarine marineToCompare) {
        return marines.values().stream().filter(spaceMarine -> spaceMarine.compareTo(marineToCompare) > 0).collect(Collectors.toList());
    }

    /**
     * @param keyToCompare The key used to take the all marines' keys, which are smaller than key in parameters.
     */
    public List<SpaceMarine> getLowerKey(int keyToCompare) {
        return marines.entrySet().stream().filter(entry -> entry.getKey() < keyToCompare).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public List<SpaceMarine> getAllByWeaponType(Weapon weapon) {
        return marines.values().stream().filter(spaceMarine -> spaceMarine.getWeaponType().equals(weapon)).collect(Collectors.toList());
    }

    public void removeByValue(SpaceMarine marine) {
        marines.entrySet().removeIf(entry -> entry.getValue().equals(marine));
    }

    /**
     * Removes a marine from collection.
     * @param key A key of marine.
     */
    public void removeFromCollection(int key) {
        marines.remove(key);
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return marines.size();
    }

    /**
     * @param key The key used to take the marine.
     * @return A marine's key.
     */
    public SpaceMarine getFromCollection(int key) {
        return marines.get(key);
    }

    /**
     * @param id ID, by which the key is found.
     * @return A marine's key.
     */
    public Integer getKeyById(int id) {
        return marines.entrySet().stream().filter(entry -> entry.getValue().getId() == id).map(Map.Entry::getKey).findFirst().get();
    }

    /**
     * @return Sum of all marines' health or 0 if collection is empty.
     */
    public Integer getSumOfHealth() {
        return marines.values().stream().reduce(0, (sum, p) -> sum += p.getHealth(), Integer::sum);
    }

    /**
     * @return Average of healthCount.
     */
    public double averageOfHeartCount() {
        double average = (double) marines.values().stream().reduce(0, (sum, p) -> sum += p.getHeartCount(), Integer::sum);
        if (average == 0) return 0;
        return average / marines.size();
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public String showCollection() {
        if (marines.isEmpty()) return "Коллекция пуста!";
        return marines.values().stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    public void loadCollection() {
        marines = databaseCollectionManager.getCollection();
        lastInitTime = LocalDateTime.now();
        System.out.println("Коллекция загружена");
    }
}
