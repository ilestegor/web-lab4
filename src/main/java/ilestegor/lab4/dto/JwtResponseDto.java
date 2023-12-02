package ilestegor.lab4.dto;

public class JwtResponseDto {
    private final String accessToken;
    private String tokenType;

    public JwtResponseDto(String accessToke) {
        this.accessToken = accessToke;
    }

    public String getAccessToke() {
        return accessToken;
    }

    public String getTokeType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
