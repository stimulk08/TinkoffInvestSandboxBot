package models.Asset;

public class AssetInfo {

    private final String fullName;
    private final String figi;

    public AssetInfo(String fullName, String figi) {
        this.fullName = fullName;
        this.figi = figi;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFigi() {
        return figi;
    }
}
