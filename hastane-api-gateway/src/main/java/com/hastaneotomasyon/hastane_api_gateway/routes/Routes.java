package com.hastaneotomasyon.hastane_api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.RequestPredicates;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> authServiceLoginRoute() {
        return route("auth_service")
                .route(RequestPredicates.path("/api/auth/login"), HandlerFunctions.http())
                // Apply a "before" filter that rewrites the target URI
                .before(BeforeFilterFunctions.uri("http://auth-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> authServiceLogoutRoute(){
        return route("auth_service")
                .route(RequestPredicates.path("/api/auth/logout"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://auth-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> patientServiceRandevuAl(){
        return route("patient-service")
                .route(RequestPredicates.path("/api/hasta/randevual"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://patient-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> patientServiceHastaGoruntule(){
        return route("patient-service")
                .route(RequestPredicates.path("/api/hasta/hastagoruntule/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://patient-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> patientServiceRandevuGoruntule(){
        return route("patient-service")
                .route(RequestPredicates.path("/api/hasta/randevugoruntule/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://patient-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> patientServiceRandevuIptal(){
        return route("patient-service")
                .route(RequestPredicates.path("/api/hasta/randevuiptal"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://patient-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceRandevuGoruntuleKod(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/randevugoruntule/kod/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceRandevuGoruntuleId(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/randevugoruntule/id/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceGunlukRandevuGoruntule(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/gunlukrandevugoruntule"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceMuayeneBaslangici(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/muayenebaslangici"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceRandevuyaPublicNotEkleOrGuncelle(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/randevupublicnotekleme/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceRandevuyaPrivateNotEkleOrGuncelle(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/randevuprivatenotekleme/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> doctorServiceRandevuyaTestEkleme(){
        return route("doctor-service")
                .route(RequestPredicates.path("/api/doktor/randevutestekleme/**"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://doctor-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> labServiceAuthDeneme(){
        return route("lab_service")
                .route(RequestPredicates.path("/api/laboratuvar/gunluktestgoruntule"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://lab-service:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> labServiceTestSonucuEkle(){
        return route("lab_service")
                .route(RequestPredicates.path("/api/laboratuvar/testsonucuekle"), HandlerFunctions.http())
                .before(BeforeFilterFunctions.uri("http://lab-service:8080"))
                .build();
    }
}

