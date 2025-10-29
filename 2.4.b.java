import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb";
        String user = "root";
        String password = "yourpassword";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            con.setAutoCommit(false);
            Scanner sc = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n=== Product Management ===");
                System.out.println("1. Add Product");
                System.out.println("2. View All Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter ProductID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Product Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();

                        PreparedStatement ps1 = con.prepareStatement("INSERT INTO Product VALUES (?, ?, ?, ?)");
                        ps1.setInt(1, id);
                        ps1.setString(2, name);
                        ps1.setDouble(3, price);
                        ps1.setInt(4, qty);
                        ps1.executeUpdate();
                        con.commit();
                        System.out.println("✅ Product added successfully!");
                        break;

                    case 2:
                        Statement st = con.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM Product");
                        System.out.println("ProductID | Name | Price | Quantity");
                        while (rs.next()) {
                            System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getDouble(3) + " | " + rs.getInt(4));
                        }
                        break;

                    case 3:
                        System.out.print("Enter ProductID to update: ");
                        int pid = sc.nextInt();
                        System.out.print("Enter new Price: ");
                        double newPrice = sc.nextDouble();

                        PreparedStatement ps2 = con.prepareStatement("UPDATE Product SET Price = ? WHERE ProductID = ?");
                        ps2.setDouble(1, newPrice);
                        ps2.setInt(2, pid);
                        ps2.executeUpdate();
                        con.commit();
                        System.out.println("✅ Product updated successfully!");
                        break;

                    case 4:
                        System.out.print("Enter ProductID to delete: ");
                        int delid = sc.nextInt();

                        PreparedStatement ps3 = con.prepareStatement("DELETE FROM Product WHERE ProductID = ?");
                        ps3.setInt(1, delid);
                        ps3.executeUpdate();
                        con.commit();
                        System.out.println("✅ Product deleted successfully!");
                        break;
                }
            } while (choice != 5);

            sc.close();
            System.out.println("Exiting...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
