package gamer.sass;

/**
 * Created by gamer on 8/26/2017.
 */

public class Userdetails {

    private String DisplayName, phone, age, email, password, photo;

    public Userdetails() {
    }

    public Userdetails(String DisplayName, String phone, String age, String email, String password, String photo) {
        this.DisplayName = DisplayName;
        this.phone = phone;
        this.age = age;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
