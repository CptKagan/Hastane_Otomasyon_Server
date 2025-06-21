package com.hastaneotomasyon.lab_service.config;

import com.hastaneotomasyon.lab_service.client.AppointmentClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${appointment.url}")
    private String appointmentServiceUrl;


    @Bean
    public AppointmentClient appointmentClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(appointmentServiceUrl) // Isteklerin gideceği ana URL
                .requestFactory(getClientRequestFactory())
                .build(); // Nesneyi oluştur
        var restClientAdapter = RestClientAdapter.create(restClient); // RestClient nesnesini HTTP interface sistemiyle uyumlu hale getirir. @GetExchange anotasyonlarla çalışabilir hale getirir.
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build(); // Interface tabanlı client'ların gerçek çalışır nesnelere çevirir. builderFor içine verilen restclientadapter, proxy'nin hangi client'ı kullandığını belirtir.
        return httpServiceProxyFactory.createClient(AppointmentClient.class); // InventoryClient.class tipi için bir HTP proxy instance oluşturur. Bu, @GetExchange gibi anotasyonlara göre gerçek HTTP istekleri atan bir sınıf olur.
    }

    private ClientHttpRequestFactory getClientRequestFactory(){
        return ClientHttpRequestFactoryBuilder
                .httpComponents()              // or .jdk(), .httpComponents(), .jetty(), .reactor()
                .withCustomizer(factory -> {
                    factory.setConnectTimeout(Duration.ofSeconds(3));
                    factory.setReadTimeout(Duration.ofSeconds(3));
                })
                .build();
    }

    @Bean
    public RestClient restClient() {return RestClient.create(); }
}
