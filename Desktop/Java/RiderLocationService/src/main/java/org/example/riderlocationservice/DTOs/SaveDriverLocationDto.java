package org.example.riderlocationservice.DTOs;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveDriverLocationDto {
    Long detectionId;
    double latitude;
    double longitude;
}
