package ra.presentation;

import ra.business.ContactManager;
import ra.entity.Contact;

import java.util.Scanner;

public class ContactApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ContactManager contactManager = new ContactManager();
        do {
            System.out.println("----------------------- Contact Book Menu --------------------");
            System.out.println("1. Hiển thị danh sách liên hệ");
            System.out.println("2. Thêm các liên hệ mới");
            System.out.println("3. Chỉnh sửa thông tin liên hệ");
            System.out.println("4. Xóa liên hệ");
            System.out.println("5. Tìm kiếm liên hệ ");
            System.out.println("6. Thống kê số lượng liên hệ theo mức độ quan trọng");
            System.out.println("7. Thống kê số lượng liên hệ theo giới tính");
            System.out.println("8. Sắp xếp liên hệ theo tên(a-z / z-a)");
            System.out.println("9. Thoát chương trình");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("Nhập lựa chọn:");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice){
                case 1:
                    contactManager.displayList();
                    break;
                case 2:
                    contactManager.addContact(sc);
                    break;
                case 3:
                    contactManager.updateContact(sc);
                    break;
                case 4:
                    contactManager.deleteContact(sc);
                    break;
                case 5:
                    contactManager.findContactByName(sc);
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    System.out.println("Hãy chọn cách sắp xếp bạn muốn");
                    System.out.println("1. Sắp xếp tên từ A - Z");
                    System.out.println("2. Sắp xếp tên từ Z - A");
                    int select = Integer.parseInt(sc.nextLine());
                    if(select == 1){
                        System.out.println("Sắp xếp tên từ A - Z:");
                        contactManager.sortContactByNameAtoZ();
                    }else if(select == 2){
                        System.out.println("Sắp xếp tên từ Z - A:");
                        contactManager.sortContactByNameZtoA();
                    }else{
                        System.err.println("Lựa chọn không phù hợp");
                    }
                    break;
                case 9:
                    System.err.println("Thoát chương trình");
                    System.exit(0);
                default:
                    System.err.println("Lựa chọn không phù hợp");
            }
        }while (true);
    }

}
