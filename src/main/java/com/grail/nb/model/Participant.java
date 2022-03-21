package com.grail.nb.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PARTICIPANT")
public class Participant {
    @Id
    @Column(name="PARTICIPANT_ID")
    private String participantId;

    @Column(name="FIRST_NAME")
    private String fname;

    @Column(name="MIDDLE_NAME")
    private String mname;

    @Column(name="LAST_NAME")
    private String lname;

    @Column(name="DOB")
    private LocalDate dob;

    @Column(name="CELL_PHONE_NO")
    private String cellphoneno;

    @Column(name="HOME_PHONE_NO")
    private String homephoneno;

    @Column(name="WORK_PHONE_NO")
    private String workphoneno;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    public Participant(){

    }

    public Participant(String id, String fName, String mName, String lName, LocalDate dob, String cellPhoneNo, String homePhoneNo, String workPhoneNo){
        this.participantId = id;
        this.fname = fName;
        this.mname = mName;
        this.lname = lName;
        this.dob = dob;
        this.cellphoneno = cellPhoneNo;
        this.homephoneno = homePhoneNo;
        this.workphoneno = workPhoneNo;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCellphoneno() {
        return cellphoneno;
    }

    public void setCellphoneno(String cellphoneno) {
        this.cellphoneno = cellphoneno;
    }

    public String getHomephoneno() {
        return homephoneno;
    }

    public void setHomephoneno(String homephoneno) {
        this.homephoneno = homephoneno;
    }

    public String getWorkphoneno() {
        return workphoneno;
    }

    public void setWorkphoneno(String workphoneno) {
        this.workphoneno = workphoneno;
    }



    public Address getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return "Participant {" +
                "id =" + participantId +
                ", first name ='" + fname + '\'' +
                ", middle name ='" + mname + '\'' +
                ", last name ='" + lname + '\'' +
                ", date of birth ='" + dob.toString() + '\'' +
                ", cell phone number ='" + cellphoneno + '\'' +
                ", home phone number ='" + homephoneno + '\'' +
                ", work phone number ='" + workphoneno + '\'' +
                ", address ='" + address.toString() + '\'' +
                "}";
    }

}
