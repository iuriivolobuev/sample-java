package app.dto;

public class DeckItemDto {
    private String left;
    private String right;

    public DeckItemDto(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }
}
