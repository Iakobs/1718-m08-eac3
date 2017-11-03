package ibanez.jacob.cat.xtec.ioc.gallery.model;

/**
 * Enumerated class for distinct types of {@link MultimediaElement}s
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public enum MultimediaElementType {

    /**
     * Picture multimedia element type.
     */
    PICTURE(0),
    /**
     * Video multimedia element type.
     */
    VIDEO(1);

    private int type;

    MultimediaElementType(int type) {
        this.type = type;
    }

    /**
     * Gets an int representation of the type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }
}
