package com.epam.config

import com.epam.repository.FarmRepository
import com.epam.service.InitFarmService
import org.apache.spark.sql.SparkSession
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class ConfiguratorForSpringBoot {

  @Bean
  def sparkSessionDev: SparkSession = SparkSession.builder.master("local[*]").appName("Weather-agro Project")
    .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/test.farms")
    .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/test.farms")
    .getOrCreate

}
