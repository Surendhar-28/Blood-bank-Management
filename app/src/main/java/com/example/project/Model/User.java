package com.example.project.Model;

public class User {
    String name,bloodGroup,id,Email,IdNumber,PhoneNumber,profilepictureurl,Search,type;

    public User(){
    }

    public User(String name, String bloodGroup, String id, String email, String idNumber, String phoneNumber, String profilepictureurl, String search, String type) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.id = id;
        Email = email;
        IdNumber = idNumber;
        PhoneNumber = phoneNumber;
        this.profilepictureurl = profilepictureurl;
        Search = search;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
