package com.epam

import com.epam.repository.FarmRepository
import com.epam.service.Implementation.Crud.FarmJavaService
import com.epam.service.InitFarmService
import org.springframework.boot.{CommandLineRunner, SpringApplication}
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean

@SpringBootApplication
class WeatherReport

object ReportsApplication extends App {

  val context: ConfigurableApplicationContext = SpringApplication.run(classOf[WeatherReport])




}
