package serverModule.utility;

import common.data.Chapter;
import common.data.SpaceMarine;
import common.data.Weapon;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Operates the collection itself.
 */
public class CollectionManager {
    private TreeMap<Integer, SpaceMarine> marines = new TreeMap<>();
    private LocalDateTime lastInitTime;
    private FileManager fileManager;

    public CollectionManager(FileManager fileManager) {
        this.lastInitTime = null;
        this.fileManager = fileManager;
    }

    /**
     * @return The collection itself.
     */
    public TreeMap<Integer, SpaceMarine> getCollection() {
        return marines;
    }

    /**
     * Loads the collection from file.
     */
    public void loadCollection(String fileName) {
        marines = fileManager.readCollection(fileName);
        lastInitTime = LocalDateTime.now();
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
     * Saves the collection to file.
     */
    public void saveCollection() {
        fileManager.writeCollection(marines);
    }

    /**
     * @param marineToCompare The marine used to compare with others.
     */
    public void removeGreater(SpaceMarine marineToCompare) {
        marines.values().removeIf(marine -> marine.compareTo(marineToCompare) > 0);
    }

    /**
     * @param keyToCompare The key used to take the all marines' keys, which are smaller than key in parameters.
     */
    public void removeLowerKey(int keyToCompare) {
        marines.entrySet().removeIf(entry -> entry.getKey() < keyToCompare);
    }

    /**
     * @param weapon The weapon used to take the marines' keys.
     */
    public void removeAllByWeaponType(Weapon weapon) {
        marines.values().removeIf(spaceMarine -> spaceMarine.getWeaponType().equals(weapon));
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
}
