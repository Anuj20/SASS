package gamer.sass;

class ProductSingleton {

    String id, name, phone,prod_name, price, Description ,photo, email,category ;

    public ProductSingleton(){}

    public ProductSingleton(String id, String name, String phone, String prod_name, String price, String Description, String photo, String email, String category){

        this.id= id;
        this.name=name;
        this.phone= phone;
        this.prod_name=prod_name;
        this.price=price;
        this.Description= Description;
        this.photo= photo;
        this.email= email;
        this.category= category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

