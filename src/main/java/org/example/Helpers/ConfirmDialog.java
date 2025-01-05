package org.example.Helpers;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog {


    /*

    ERROR_MESSAGE
    INFORMATION_MESSAGE
    WARNING_MESSAGE
    QUESTION_MESSAGE
    PLAIN_MESSAGE

    */

    public static int dialog(String message, int messageType) {
        String title = "Bilgilendirme";
        int optionType = JOptionPane.YES_NO_OPTION;

        if(messageType == 0) {
            messageType = JOptionPane.INFORMATION_MESSAGE;
            title = "Bilgilendirme";
        } else if(messageType == 1) {
            messageType = JOptionPane.WARNING_MESSAGE;
            title = "Uyarı";
        } else if(messageType == 2) {
            messageType = JOptionPane.ERROR_MESSAGE;
            title = "Hata";
            optionType = JOptionPane.DEFAULT_OPTION;
        }
        return JOptionPane.showConfirmDialog(getParent(), message,title, optionType, messageType);
    }

    private static JFrame getParent() {
        Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        if(activeWindow instanceof JFrame) {
            return (JFrame) activeWindow;
        }
        return null;
    }
}
