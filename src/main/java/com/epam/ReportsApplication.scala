package com.epam

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class WeatherReport

object ReportsApplication extends App {
  val context: ConfigurableApplicationContext = SpringApplication.run(classOf[WeatherReport])


}
