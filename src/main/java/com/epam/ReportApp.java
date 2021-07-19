package com.epam;

import com.epam.model.FarmJava;
import com.epam.model.SensitivityJava;
import com.epam.service.Implementation.FarmJavaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;
import java.util.List;
@SpringBootApplication
@Slf4j
public class ReportApp {
    public static void main(String[] args) {
        SpringApplication.run(ReportApp.class, args);
    }

    @Bean
    public CommandLineRunner setup(FarmJavaService farmService) {
        return (args) -> {
            log.info("Generating sample data");
            farmService.deleteAllFarms();
            List<String> reservations = Arrays.asList("Aseed");
            reservations.forEach(reservation ->
                    farmService.saveFarm(FarmJava.builder()
                            .name(reservation)
                            .lastname("Iraqi")
                            .crops("mango")
                            .id(12)
                            .location("Tira")
                            .sensitivity(SensitivityJava.builder().cold(4).dry(6).heat(7).build())
                            .stationId(8)
                            .email("iraqiAseed")
                            .build()) );

            farmService.getAllFarms().forEach(reservation ->
                    log.info("RESERVATION --> " + reservation.getName() + " ID: " + reservation.getId()));
        };
    }

}
