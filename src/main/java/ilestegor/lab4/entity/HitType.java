package ilestegor.lab4.entity;

public enum HitType {
    SECOND_AREA("2️⃣"),
    THIRD_AREA("3️⃣"),
    FOURTH_AREA("4️⃣"),
    MISS("🤬"),
    WRONG_DATA("Not valid data received");
    private final String hitArea;

    HitType(String hitArea) {
        this.hitArea = hitArea;
    }

    public String getHitArea() {
        return hitArea;
    }
}
