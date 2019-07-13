package com.example.exigent.Model;
/*this POJO object will store the 2 emergency contact*/

public class EmergencyContact {
    private String eName1;
    private String eName2;
    private String eRelationship1;
    private String eRelationship2;
    private int ePhone1,ePhone2;

    public String geteRelationship2() {
        return eRelationship2;
    }

    public void seteRelationship2(String eRelationship2) {
        this.eRelationship2 = eRelationship2;
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

    public int getePhone1() {
        return ePhone1;
    }

    public void setePhone1(int ePhone1) {
        this.ePhone1 = ePhone1;
    }

    public int getePhone2() {
        return ePhone2;
    }

    public void setePhone2(int ePhone2) {
        this.ePhone2 = ePhone2;
    }



}
