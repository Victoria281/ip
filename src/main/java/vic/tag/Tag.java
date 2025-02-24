package vic.tag;

/**
 * Represents a tag class
 */
public class Tag {
    private String name;

    /**
     * Constructor for Tag class.
     *
     * @param name The name of the tag.
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the tag.
     *
     * @return The name of the tag.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     *
     * @param name The name to set for the tag.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
