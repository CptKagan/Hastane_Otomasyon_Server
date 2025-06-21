package com.hastaneotomasyon.doctor_service.model;

import com.hastaneotomasyon.doctor_service.dto.DoctorResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Doktor {
    @Id
    private String id; // keycloak UUID

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String department;


    public Doktor(DoctorResponse doctorResponse){
        this.id = doctorResponse.id();
        this.firstName = doctorResponse.firstName();
        this.lastName = doctorResponse.lastName();
        this.email = doctorResponse.email();
        this.department = doctorResponse.department();
        this.userName = doctorResponse.username();
    }
}
