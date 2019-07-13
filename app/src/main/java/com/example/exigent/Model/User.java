package com.example.exigent.Model;

/**
 * Pojo user used for Firebase database
 */
public class User {


    String region;
    String name;
    String email;
    String id;
    String phone;
    private String eName1,eName2,eRelationship1,erelationship2;
    private String ePhone1,ePhone2;

    public User() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    public String geteName1() {
        return eName1;
    }

    public void seteName1(String eName1) {
        this.eName1 = eName1;
    }

    public String geteName2() {
        return eName2;
    }

    public void seteName2(String eName2) {
        this.eName2 = eName2;
    }

    public String geteRelationship1() {
        return eRelationship1;
    }

    public void seteRelationship1(String eRelationship1) {
        this.eRelationship1 = eRelationship1;
    }

    public String getErelationship2() {
        return erelationship2;
    }

    public void setErelationship2(String erelationship2) {
        this.erelationship2 = erelationship2;
    }

    public String getePhone1() {
        return ePhone1;
    }

    public void setePhone1(String ePhone1) {
        this.ePhone1 = ePhone1;
    }

    public String getePhone2() {
        return ePhone2;
    }

    public void setePhone2(String ePhone2) {
        this.ePhone2 = ePhone2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    String idno;

}
