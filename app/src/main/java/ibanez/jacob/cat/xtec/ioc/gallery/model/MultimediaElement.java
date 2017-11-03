package ibanez.jacob.cat.xtec.ioc.gallery.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class representing a single {@link MultimediaElement}.
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class MultimediaElement implements Serializable {

    private static final long serialVersionUID = -4930370737572534232L;

    private long id;
    private String name;
    private String path;
    private int type;
    private LatLng latLng;

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets path.
     *
     * @param path the path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets lat lng.
     *
     * @return the lat lng
     */
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * Sets lat lng.
     *
     * @param latLng the lat lng
     */
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultimediaElement that = (MultimediaElement) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(path, that.path) &&
                Objects.equals(type, that.type) &&
                Objects.equals(latLng, that.latLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, type, latLng);
    }

    @Override
    public String toString() {
        return "MultimediaElement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
