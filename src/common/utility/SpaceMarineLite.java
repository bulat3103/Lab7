package common.utility;

import common.data.Chapter;
import common.data.Coordinates;
import common.data.Weapon;

import java.io.Serializable;
import java.util.Objects;

public class SpaceMarineLite implements Serializable {
    private String name;
    private Coordinates coordinates;
    private int health;
    private Integer heartCount;
    private String achievements;
    private Weapon weaponType;
    private Chapter chapter;

    public SpaceMarineLite(String name, Coordinates coordinates, int health, Integer heartCount, String achievements, Weapon weaponType, Chapter chapter) {
        this.name = name;
        this.coordinates = coordinates;
        this.health = health;
        this.heartCount = heartCount;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getHealth() {
        return health;
    }

    public Integer getHeartCount() {
        return heartCount;
    }

    public String getAchievements() {
        return achievements;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceMarineLite that = (SpaceMarineLite) o;
        return health == that.health && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) && Objects.equals(heartCount, that.heartCount) && Objects.equals(achievements, that.achievements) && weaponType == that.weaponType && Objects.equals(chapter, that.chapter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, health, heartCount, achievements, weaponType, chapter);
    }

    @Override
    public String toString() {
        return "SpaceMarineLite{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", health=" + health +
                ", heartCount=" + heartCount +
                ", achievements='" + achievements + '\'' +
                ", weaponType=" + weaponType +
                ", chapter=" + chapter +
                '}';
    }
}
