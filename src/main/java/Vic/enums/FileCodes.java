package main.java.Vic.enums;

public enum FileCodes {
    T("T"),
    D("D"),
    E("E"),
    N("n");

    private final String fileCodeText;

    FileCodes(String fileCodeText) {
        this.fileCodeText = fileCodeText;
    }

    public String getCommandText() {
        return this.fileCodeText;
    }

    public static FileCodes convertText(String text) {
        for (FileCodes c :FileCodes.values()) {
            if (c.fileCodeText.equalsIgnoreCase(text)) {
                return c;
            }
        }
        return N;
    }
}
