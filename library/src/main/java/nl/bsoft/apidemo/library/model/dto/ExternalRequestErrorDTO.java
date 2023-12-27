package nl.bsoft.apidemo.library.model.dto;

public record ExternalRequestErrorDTO(
        String type, String title, Integer status, String detail, String instance) {
}