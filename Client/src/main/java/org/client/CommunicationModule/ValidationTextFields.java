package org.client.CommunicationModule;

import javafx.scene.control.TextField;

public class ValidationTextFields {
    public static void onlyLetters(TextField text) {
        String tempText = text.getText();

        String cleanedText = tempText.replaceAll("[^a-zA-Z ]", "");
        text.setText(cleanedText);

        text.positionCaret(cleanedText.length());
    }

    public static void withoutSpecialChar(TextField text) {
        String tempText = text.getText();

        String cleanedText = tempText.replaceAll("[^a-zA-Z0-9 ]", "");
        text.setText(cleanedText);

        text.positionCaret(cleanedText.length());
    }

    public static void onlyNumbers(TextField text, int maxDigits) {
        String tempText = text.getText();
        String cleanedText = tempText.replaceAll("[^0-9]", "");

        if (cleanedText.length() > maxDigits) {
            cleanedText = cleanedText.substring(0, maxDigits);
        }
        text.setText(cleanedText);
        text.positionCaret(cleanedText.length());
    }
}
