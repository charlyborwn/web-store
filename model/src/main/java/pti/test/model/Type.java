package pti.test.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * This is the reflection of <code>Type</code> entity in DB
 * on the java class.
 *
 * @author Syrotyuk R.
 */
@Entity(name = "type")
public class Type implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "category")
    private String category;

    public Type() {
    }

    public Type(String type, String category) {
        this.type = type;
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", subtype='" + category + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return id == type.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
