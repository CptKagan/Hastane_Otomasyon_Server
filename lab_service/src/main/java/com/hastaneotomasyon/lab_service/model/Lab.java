package com.hastaneotomasyon.lab_service.model;

import com.hastaneotomasyon.lab_service.dto.LabResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Lab {
    @Id
    private String labId;

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String department;

    public Lab(LabResponse labResponse){
        this.labId = labResponse.id();
        this.firstName = labResponse.firstName();
        this.lastName = labResponse.lastName();
        this.email = labResponse.email();
        this.department = labResponse.department();
        this.userName = labResponse.username();
    }
}
