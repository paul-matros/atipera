package pl.matros.dto;

public record ErrorResponseDto(int status,
                               String message) {
};