package app.dto;

public class DeckItemDto {
    private final String source;
    private final String targetSg;
    private final String targetPl;
    private final String color;

    public DeckItemDto(String source, String targetSg, String targetPl, String color) {
        this.source = source;
        this.targetSg = targetSg;
        this.targetPl = targetPl;
        this.color = color;
    }

    public String getSource() {
        return source;
    }

    public String getTargetSg() {
        return targetSg;
    }

    public String getTargetPl() {
        return targetPl;
    }

    public String getColor() {
        return color;
    }
}
