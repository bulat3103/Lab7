package common.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Chapter with marines.
 */
public class Chapter implements Serializable {
    private String name;
    private String parentLegion;

    public Chapter(String name, String parentLegion) {
        this.name = name;
        this.parentLegion = parentLegion;
    }

    /**
     * @return Name of the chapter.
     */
    public String getName() {
        return name;
    }

    /**
     * @return ParentLegion of the chapter.
     */
    public String getParentLegion() {
        return parentLegion;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", parentLegion='" + parentLegion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(name, chapter.name) &&
                Objects.equals(parentLegion, chapter.parentLegion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parentLegion);
    }
}
