package vic.enums;

/**
 * This enum represents different file codes used to identify various task types.
 */
public enum FileCodes {
    T("T"),
    D("D"),
    E("E"),
    N("n");

    private final String fileCodeText;

    FileCodes(String fileCodeText) {
        this.fileCodeText = fileCodeText;
    }

    /**
     * Converts the given text to the corresponding FileCodes enum value.
     * If the text does not match any known file code, it returns 'N' (unknown).
     *
     * @param text the text to convert
     * @return the corresponding FileCodes enum value
     */
    public static FileCodes convertText(String text) {
        for (FileCodes c :FileCodes.values()) {
            if (c.fileCodeText.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return N;
    }
}
