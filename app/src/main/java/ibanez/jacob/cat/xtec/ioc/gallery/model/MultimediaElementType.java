package ibanez.jacob.cat.xtec.ioc.gallery.model;

/**
 * Enumerated class for distinct types of {@link MultimediaElement}s
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public enum MultimediaElementType {

    PICTURE(0),
    VIDEO(1);

    private int type;

    MultimediaElementType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
