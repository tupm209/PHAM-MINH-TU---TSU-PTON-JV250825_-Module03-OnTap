package ra.business;

import Database.ConnectionDB;
import ra.entity.Contact;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactMng {
    private final List<Contact> contacts = new ArrayList<>();
    public void displayList(){
        contacts.clear();
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall("{call all_list()}");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()){
                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("sex"),
                        rs.getString("address"),
                        rs.getInt("rating"),
                        rs.getString("note")
                );
                contacts.add(contact);
            }
            if(contacts.isEmpty()){
                System.out.println("Danh sách rỗng");
            }else {
                contacts.forEach(Contact::displayData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContact(Scanner sc){
        Contact newContact = new Contact();
        newContact.inputData(sc);
        try (Connection conn = ConnectionDB.openConnection()){
            conn.setAutoCommit(false);
            CallableStatement callableStatement = conn.prepareCall("{call add_contact(?,?,?,?,?,?,?)}");
            callableStatement.setString(1, newContact.getName());
            callableStatement.setString(2, newContact.getEmail());
            callableStatement.setString(3, newContact.getPhone());
            callableStatement.setBoolean(4, newContact.isSex());
            callableStatement.setString(5, newContact.getAddress());
            callableStatement.setInt(6, newContact.getRating());
            callableStatement.setString(7, newContact.getNote());
            boolean rs = callableStatement.executeUpdate() > 0;
            if(rs){
                conn.commit();
                System.out.println("Thêm mới thành công");
            }else{
                conn.rollback();
                System.out.println("Thêm mới thất bại");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Contact findContactById(int id){
        Contact foundContact = null;
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall("{call find_contact_by_id(?)}");
            callableStatement.setInt(1,id);
            ResultSet rs = callableStatement.executeQuery();
            if (rs.next()){
                foundContact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("sex"),
                        rs.getString("address"),
                        rs.getInt("rating"),
                        rs.getString("note")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundContact;
    }

    public void updateContact(Scanner sc){
        System.out.println("Nhập mã liên lạc muốn chỉnh sửa");
        int inputId = Integer.parseInt(sc.nextLine());
        if(findContactById(inputId) == null){
            System.out.println("Không tìm thấy liên hệ");
        }else{
            Contact foundContact = findContactById(inputId);
            System.out.println("Thông tin cũ của liên hệ có mã là: " + inputId);
            foundContact.displayData();
            System.out.println("Nhập thông tin chỉnh sửa:");
            foundContact.inputData(sc);
            try (Connection conn = ConnectionDB.openConnection()){
                conn.setAutoCommit(false);
                CallableStatement callableStatement = conn.prepareCall("{call update_contact(?,?,?,?,?,?,?,?)}");
                callableStatement.setInt(1,foundContact.getId());
                callableStatement.setString(2, foundContact.getName());
                callableStatement.setString(3, foundContact.getEmail());
                callableStatement.setString(4, foundContact.getPhone());
                callableStatement.setBoolean(5, foundContact.isSex());
                callableStatement.setString(6, foundContact.getAddress());
                callableStatement.setInt(7, foundContact.getRating());
                callableStatement.setString(8, foundContact.getNote());
                boolean rs = callableStatement.executeUpdate() > 0;
                if(rs){
                    conn.commit();
                    System.out.println("Cập nhật thành công");
                }else{
                    conn.rollback();
                    System.out.println("Cập nhật thất bại");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteContact(Scanner sc){
        System.out.println("Nhập mã liên lạc muốn xóa");
        int inputId = Integer.parseInt(sc.nextLine());
        if(findContactById(inputId) == null){
            System.out.println("Không tìm thấy liên hệ");
        }else{
            System.out.println("Bạn có chắc chắn muốn xóa không? (Y/N)");
            String choice = sc.nextLine();
            if(choice.equalsIgnoreCase("y")){
                Contact foundContact = findContactById(inputId);
                try (Connection conn = ConnectionDB.openConnection()){
                    conn.setAutoCommit(false);
                    CallableStatement callableStatement = conn.prepareCall("{call delete_contact(?)}");
                    callableStatement.setInt(1,foundContact.getId());
                    boolean rs = callableStatement.executeUpdate() > 0;
                    if(rs){
                        conn.commit();
                        System.out.println("xóa thành công");
                    }else{
                        conn.rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("Hủy thao tác xóa");
            }
        }
    }

    public void findContactByName(Scanner sc){
        System.out.println("Nhập tên liên hệ muốn tìm");
        String inputName = sc.nextLine();
        List<Contact> foundContactByName = new ArrayList<>();
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall("{call find_contact_by_name(?)}");
            callableStatement.setString(1,inputName);
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()){
                Contact foundContact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("sex"),
                        rs.getString("address"),
                        rs.getInt("rating"),
                        rs.getString("note")
                );
                foundContactByName.add(foundContact);
            }
            if(foundContactByName.isEmpty()){
                System.out.println("Không thấy liên hệ");
            }else{
                foundContactByName.forEach(Contact::displayData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sortContactByNameAtoZ(){
        contacts.clear();
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall("{call sort_name_from_a_to_z()}");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()){
                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("sex"),
                        rs.getString("address"),
                        rs.getInt("rating"),
                        rs.getString("note")
                );
                contacts.add(contact);
            }
            if(contacts.isEmpty()){
                System.out.println("Danh sách rỗng");
            }else {
                System.out.println("Danh sách sắp xếp tên từ A - Z");
                contacts.forEach(Contact::displayData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sortContactByNameZtoA(){
        contacts.clear();
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall("{call sort_name_from_z_to_a()}");
            ResultSet rs = callableStatement.executeQuery();
            while (rs.next()){
                Contact contact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("sex"),
                        rs.getString("address"),
                        rs.getInt("rating"),
                        rs.getString("note")
                );
                contacts.add(contact);
            }
            if(contacts.isEmpty()){
                System.out.println("Danh sách rỗng");
            }else {
                System.out.println("Danh sách sắp xếp tên từ Z - A");
                contacts.forEach(Contact::displayData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void thongKeSoLuongTheoRating(){
        String sql = "SELECT rating as 'Mức độ quan trọng',COUNT(*) as 'Số lượng'FROM contactbook GROUP BY rating ORDER BY  rating ASC;";
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            System.out.println("Rating | Số lượng");
            while (rs.next()){
                int rating = rs.getInt("Mức độ quan trọng");
                int count = rs.getInt("Số lượng");
                System.out.printf("%-7d| %d\n",rating,count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void thongKeQuaGioiTinh(){
        String sql = "SELECT sex as 'Giới tính', count(*) as 'Số lượng' FROM contactBook group by sex ORDER BY sex DESC ;";
        try (Connection conn = ConnectionDB.openConnection()){
            CallableStatement callableStatement = conn.prepareCall(sql);
            ResultSet rs = callableStatement.executeQuery();
            System.out.println("Giới tính | Số lượng");
            while (rs.next()){
                boolean sex = rs.getBoolean("Giới tính");
                int count = rs.getInt("Số lượng");
                System.out.printf("%-10s| %d\n",sex ? "Nam" : "Nữ",count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
