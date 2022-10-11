/**
 * <p>
 * Employee class - This is POJO or Model class For Qualification
 * </p>
 * @author Mohammed Ammar
 * @version 1.0 
 * @since 12/08/2022
 *
 **/
package com.ideas2it.employee.models;

import javax.persistence.*;

@Entity
@Table(name = "qualification")
public class Qualification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="qualification_id")
    private int qualificationId;

    @Column(name = "description")
    private String description;

    public Qualification() {
    }

    /**
    * <p> 
    * This is Constructor for Qualification class
    * </p>
    * @param description - qualification like, BCA or BSC etc
    **/
    public Qualification(String description) {
	this.description = description;
    }

    public int getQualificationId() {
	return qualificationId;
    }

    public void setQualificationId(int qualificationId) {
	this.qualificationId = qualificationId;
    }  

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    } 

    @Override
    public String toString() {
	return description;
    }
	
}