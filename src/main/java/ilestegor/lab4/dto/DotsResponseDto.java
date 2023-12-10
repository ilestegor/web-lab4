package ilestegor.lab4.dto;

public record DotsResponseDto(
        double x,
        double y,
        double r,
        String curRequestTime,
        long executionTime,
        String hitType
) {
}
