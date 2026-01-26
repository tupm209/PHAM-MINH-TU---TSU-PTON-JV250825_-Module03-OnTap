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

public class ContactManager {
    private final List<Contact> contacts = new ArrayList<>();

    private Contact creatNewContact(ResultSet rs) throws SQLException {
        return new Contact(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getBoolean("sex"),
                rs.getString("address"),
                rs.getInt("rating"),
                rs.getString("note")
        );
    }

    public void displayList(){
        contacts.clear();
        try (Connection conn = ConnectionDB.openConnection()){
            if (conn != null){
                CallableStatement callableStatement = conn.prepareCall("{call all_list()}");
                ResultSet rs = callableStatement.executeQuery();
                while (rs.next()){
                    contacts.add(creatNewContact(rs));
                }

                if (contacts.isEmpty()){
                    System.err.println("Danh sách trống");
                }else{
                    System.out.println("Danh sách liên hệ:");
                    contacts.forEach(Contact::displayData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContact(Scanner sc){
        Contact contact = new Contact();
        contact.inputData(sc);
        try (Connection conn = ConnectionDB.openConnection()){
            if (conn != null){
                conn.setAutoCommit(false);
                CallableStatement callableStatement = conn.prepareCall("{call add_contact(?,?,?,?,?,?,?)}");
                callableStatement.setString(1, contact.getName());
                callableStatement.setString(2,contact.getEmail());
                callableStatement.setString(3,contact.getPhone());
                callableStatement.setBoolean(4,contact.isSex());
                callableStatement.setString(5,contact.getAddress());
                callableStatement.setInt(6,contact.getRating());
                callableStatement.setString(7,contact.getNote());
                boolean rs = callableStatement.executeUpdate() > 0;
                if(rs){
                    conn.commit();
                    System.out.println("Thêm mới thành công");
                }else{
                    conn.rollback();
                    System.out.println("Thêm mới thất bại");
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public Contact findContactById(int id){
        try (Connection conn = ConnectionDB.openConnection()){
            if (conn != null){
                CallableStatement callableStatement = conn.prepareCall("{call find_contact_by_id(?)}");
                callableStatement.setInt(1,id);
                ResultSet rs = callableStatement.executeQuery();
                if (rs.next()){
                    creatNewContact(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateContact(Scanner sc){
        System.out.println("Nhập id liên hệ muốn chỉnh sửa:");
        int inputId = Integer.parseInt(sc.nextLine());
        Contact foundContact = findContactById(inputId);

        if(foundContact == null){
            System.out.println("Không tìm thấy liên hệ có mã là: " + inputId);
        }else{
            System.out.println("Thông tin liên hệ có mã là: " + inputId);
            foundContact.displayData();
            System.out.println("Nhập thông tin muốn cập nhật: ");
            foundContact.inputData(sc);

            try (Connection conn = ConnectionDB.openConnection()){
                if (conn != null){
                    conn.setAutoCommit(false);
                    CallableStatement callableStatement = conn.prepareCall("{call update_contact(?,?,?,?,?,?,?,?)}");
                    callableStatement.setInt(1,foundContact.getId());
                    callableStatement.setString(2, foundContact.getName());
                    callableStatement.setString(3,foundContact.getEmail());
                    callableStatement.setString(4,foundContact.getPhone());
                    callableStatement.setBoolean(5,foundContact.isSex());
                    callableStatement.setString(6,foundContact.getAddress());
                    callableStatement.setInt(7,foundContact.getRating());
                    callableStatement.setString(8,foundContact.getNote());
                    boolean rs = callableStatement.executeUpdate() > 0;
                    if(rs){
                        conn.commit();
                        System.out.println("Cập nhật thành công");
                    }else{
                        conn.rollback();
                        System.out.println("Cập nhật thất bại");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteContact(Scanner sc){
        System.out.println("Nhập mã liên hệ muốn xóa:");
        int inputId = Integer.parseInt(sc.nextLine());
        Contact foundContact = findContactById(inputId);
        if(foundContact == null){
            System.out.println("Không tìm thấy liên hệ có mã là: " + inputId);
        }else{
            try (Connection conn = ConnectionDB.openConnection()){
                if (conn != null){
                    conn.setAutoCommit(false);
                    CallableStatement callableStatement = conn.prepareCall("{call delete_contact(?)}");
                    callableStatement.setInt(1,foundContact.getId());
                    System.out.println("Bạn có chắc chắn muốn xóa không? (Y/N)");
                    String choice = sc.nextLine();
                    if(choice.equalsIgnoreCase("y")){
                        callableStatement.executeUpdate();
                        conn.commit();
                        System.out.println("Xóa thành công");
                    }else{
                        conn.rollback();
                        System.out.println("Xóa thất bại");
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void findContactByName(Scanner sc){
        System.out.println("Hãy nhập tên của liên hệ bạn muốn tìm:");
        String inputName = sc.nextLine();
        try (Connection conn = ConnectionDB.openConnection()){
            if (conn != null){
                CallableStatement callableStatement = conn.prepareCall("{call find_contact_by_name(?)}");
                callableStatement.setString(1,inputName);
                ResultSet rs = callableStatement.executeQuery();
                if (rs.next()){
                    Contact foundContact = creatNewContact(rs);
                    foundContact.displayData();
                }else{
                    System.out.println("Không có thông tin liên hệ cần tìm");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sortContactByNameAtoZ(){
        contacts.clear();
        try (Connection conn = ConnectionDB.openConnection()){
            if (conn != null){
                CallableStatement callableStatement = conn.prepareCall("{call sort_name_from_a_to_z()}");
                ResultSet rs = callableStatement.executeQuery();
                while (rs.next()){
                    contacts.add(creatNewContact(rs));
                }
                if (contacts.isEmpty()){
                    System.err.println("Danh sách trống");
                }else{
                    System.out.println("Danh sách liên hệ:");
                    contacts.forEach(Contact::displayData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sortContactByNameZtoA(){
        contacts.clear();
        try (Connection conn = ConnectionDB.openConnection()){
            if (conn != null){
                CallableStatement callableStatement = conn.prepareCall("{call sort_name_from_z_to_a()}");
                ResultSet rs = callableStatement.executeQuery();
                while (rs.next()){
                    contacts.add(creatNewContact(rs));
                }
                if (contacts.isEmpty()){
                    System.err.println("Danh sách trống");
                }else{
                    System.out.println("Danh sách liên hệ:");
                    contacts.forEach(Contact::displayData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
