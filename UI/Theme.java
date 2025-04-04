package UI;

import javax.swing.*;
import java.awt.*;

public class Theme {
    public static final Color BACKGROUND = new Color(245, 245, 245);         // พื้นหลังหลัก (เทาอ่อน)
    public static final Color FOREGROUND = new Color(33, 33, 33);             // สีข้อความหลัก (เทาเข้ม)
    public static final Color PRIMARY = new Color(100, 149, 237);            // สีหลัก (ฟ้า cornflower blue)
    public static final Color CARD = new Color(255, 255, 255);               // กล่อง/การ์ด (ขาว)
    public static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);   // ฟอนต์หลัก มินิมอล
    public static final Color DELETE_BUTTON_COLOR = new Color(200, 50, 40);  // สีแดงสำหรับปุ่มลบ (แดงสด)

    public static void apply(Component component) {
        component.setFont(FONT);
        component.setForeground(FOREGROUND);

        if (component instanceof JPanel || component instanceof JFrame) {
            component.setBackground(BACKGROUND);
        }

        if (component instanceof JButton) {
            // หากเป็นปุ่มลบ (Delete Task) ให้ใช้สีแดง
            if (((JButton) component).getText().equals("Delete Task")) {
                component.setBackground(DELETE_BUTTON_COLOR);  // ปุ่มลบเป็นสีแดง
                component.setForeground(Color.WHITE);  // ข้อความในปุ่มเป็นสีขาว
            } else {
                component.setBackground(PRIMARY);  // ปุ่มอื่นๆ ใช้สีหลัก
                component.setForeground(Color.WHITE);
            }
            ((JButton) component).setFocusPainted(false);
        }

        if (component instanceof JTable) {
            component.setBackground(CARD);
            component.setForeground(FOREGROUND);
        }

        if (component instanceof JScrollPane) {
            component.setBackground(CARD);
        }

        if (component instanceof JTextField || component instanceof JTextArea) {
            component.setBackground(Color.WHITE);
            component.setForeground(FOREGROUND);
            component.setFont(FONT);
        }

        if (component instanceof JLabel) {
            component.setFont(FONT.deriveFont(Font.BOLD, 15f));
        }
    }

    public static void applyRecursively(Component component) {
        apply(component);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyRecursively(child);
            }
        }
    }
}
