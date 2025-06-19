package makeasquare;

import java.awt.Color;
import javax.swing.JFrame;

public class makeasquare {

    public static void main(String[] args) {
        openForm(new numOfPieces());

    }

    // open any form
    public static void openForm(JFrame form) {
        form.setLocationRelativeTo(null);
        form.setDefaultCloseOperation(2);
        form.getContentPane().setBackground(Color.black);
        form.pack();
        form.setVisible(true);
    }

}
