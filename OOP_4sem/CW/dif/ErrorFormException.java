package com.company.dif;

import com.company.ui.UI;

import javax.swing.*;
//для исключений
public class ErrorFormException extends Exception {
    public ErrorFormException(String message) {
        super(message);
        UI.logger.error("new Error Form Exception");
    }

    public void showErrorOnScreen() {
        JOptionPane.showMessageDialog(null, super.getMessage());
    }
}
