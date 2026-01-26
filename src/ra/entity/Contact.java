package ra.entity;

import ra.validation.Validator;

import java.util.Scanner;

public class Contact implements IContact {
    private int id;
    private String name;
    private String email;
    private String phone;
    private boolean sex;
    private String address;
    private int rating;
    private String note;

    public Contact() {
    }

    public Contact(int id, String name, String email, String phone, boolean sex, String address, int rating, String note) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.address = address;
        this.rating = rating;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void inputData(Scanner sc){
        // nhập tên
        do {
            name = Validator.getString(sc, "Nhập họ và tên:");
            if(name.length() > 50){
                System.out.println("Tên không quá 50 ký tự");
            }
        }while (name.length() > 50 || name.isEmpty());

        // nhập email
        boolean flagEmail;
        do {
            String regexEmail = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            email = Validator.getString(sc, "Nhập email:");
            if(!email.matches(regexEmail)){
                System.out.println("Không đúng định dạng email");
                flagEmail = false;
            }else{
                flagEmail = true;
            }
        }while (!flagEmail);

        //nhập phone
        boolean flagPhone;
        do {
            String regexPhone = "^0[0-9]{9,10}$";
            phone = Validator.getString(sc, "Nhập số điện thoại:");
            if(!phone.matches(regexPhone)){
                System.out.println("Không đúng định dạng số điện thoại");
                flagPhone = false;
            }else{
                flagPhone = true;
            }
        }while (!flagPhone);

        sex = Boolean.parseBoolean(Validator.getString(sc, "Nhập giới tính:"));

        System.out.println("Nhập địa chỉ:");
        address = sc.nextLine();

        do {
            rating = Integer.parseInt(Validator.getString(sc, "Nhập mức độ quan trọng (1-5):"));
        }while (rating < 1 || rating > 5);

        do {
            System.out.println("Nhập ghi chú:");
            note = sc.nextLine();
            if(note.length() > 100){
                System.out.println("Tối đa 100 ký tự");
            }
        }while (note.length() > 100);

    }

    @Override
    public void displayData(){
        System.out.printf("Mã liên hệ: %-3d | Tên: %-20s | Email: %-20s | Số điện thoại: %-13s | Giới tính: %-5s | Địa chỉ: %-20s | Rank: %-3d | Ghi chú: %-20s\n",
                id, name, email, phone, sex ? "Nam" : "Nữ", address, rating, note);
    }
}
