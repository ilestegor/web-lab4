package ilestegor.lab4.dto;

public class JwtResponseDto {
    private long tokenExpirationDate;
    private String tokenType;

    public JwtResponseDto(long tokenExpirationDate, String tokenType) {
        this.tokenExpirationDate = tokenExpirationDate;
        this.tokenType = tokenType;
    }

    public long getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(long tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
