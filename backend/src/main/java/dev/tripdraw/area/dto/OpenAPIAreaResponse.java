package dev.tripdraw.area.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenAPIAreaResponse(
        @JsonProperty("cd")
        String code,

        @JsonProperty("addr_name")
        String address,

        @JsonProperty("full_addr")
        String fullAddress,

        @JsonProperty("x_coor")
        String longitude,

        @JsonProperty("y_coor")
        String latitude,

        @JsonProperty("pg")
        String page
) {
}
