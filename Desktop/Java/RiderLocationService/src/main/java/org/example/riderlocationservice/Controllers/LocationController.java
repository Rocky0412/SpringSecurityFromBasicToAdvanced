package org.example.riderlocationservice.Controllers;

import org.example.riderlocationservice.DTOs.LocationDTO;
import org.example.riderlocationservice.DTOs.SaveDriverLocationDto;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController
{
    private final RedisTemplate<String, String> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String LOCATION_KEY = "Driver:location";
    public LocationController(RedisTemplate<String, String> redisTemplate,
                              StringRedisTemplate stringRedisTemplate)
    {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostMapping("/driver")
    public ResponseEntity<Boolean> saveDriveLocation(@RequestBody SaveDriverLocationDto saveDriverLocationDto){

        try {

            stringRedisTemplate.opsForGeo().add(LOCATION_KEY,new Point(saveDriverLocationDto.getLongitude(),
                    saveDriverLocationDto.getLatitude()),saveDriverLocationDto.getDetectionId().toString());

            return  ResponseEntity.status(HttpStatus.CREATED).body(Boolean.TRUE);

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
        }
    }

    @PostMapping("/nearbyDriver")
    public ResponseEntity<List<String>> getNearbyDriverLocations(@RequestBody LocationDTO locationDTO){

        Circle area = new Circle(
                new Point(locationDTO.getLongitude(), locationDTO.getLatitude()),
                new Distance(50, Metrics.KILOMETERS)
        );

        GeoResults<RedisGeoCommands.GeoLocation<String>> results=stringRedisTemplate.opsForGeo()
                .radius(LOCATION_KEY, area);

        List<String> driverIds = new ArrayList<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results){
            driverIds.add(result.getContent().getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(driverIds);

    }
}
