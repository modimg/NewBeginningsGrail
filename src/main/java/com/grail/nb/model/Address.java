package com.grail.nb.model;

import javax.persistence.*;
import javax.servlet.http.Part;
import java.sql.Date;

@Entity
@Table(name = "ADDRESS")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ADDRESS_ID")
    private Long addressId;

    @Column(name="ADDR_LINE1")
    private String addrLine1;

    @Column(name="ADDR_LINE2")
    private String addrLine2;

    @Column(name="CITY")
    private String city;

    @Column(name="STATE")
    private String state;

    @Column(name="ZIP")
    private String zipCode;

    @Column(name="COUNTRY")
    private String country;

    public Address(){

    }

    public Address(String addrLine1, String addrLine2, String city, String state, String zipCode, String country){
        this.addrLine1 = addrLine1;
        this.addrLine2 = addrLine2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address {" +
                "address line1 ='" + addrLine1 + '\'' +
                ", address line2 ='" + addrLine2 + '\'' +
                ", city ='" + city + '\'' +
                ", state ='" + state + '\'' +
                ", zip ='" + zipCode + '\'' +
                ", country ='" + country  +
                '}';
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getAddressId() {
        return this.addressId;
    }

    public void setAddrLine1(String addrLine1){
        this.addrLine1 = addrLine1;
    }

    public void setAddrLine2(String addrLine2){
        this.addrLine2 = addrLine2;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setZipCode(String zip){
        this.zipCode = zip;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getAddrLine1(){
        return this.addrLine1;
    }

    public String getAddrLine2(){
        return this.addrLine2;
    }

    public String getCity(){
        return this.city;
    }

    public String getState(){
        return this.state;
    }

    public String getZipCode(){
        return this.zipCode;
    }

    public String getCountry(){
        return this.country;
    }

//    public Participant getParticipant(){
//        return this.participant;
//    }
//
//    public void setParticipant(Participant participant){
//        this.participant = participant;
//    }
}
