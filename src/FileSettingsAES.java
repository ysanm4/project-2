import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

public class FileSettingsAES {
    private static final String SETTINGS_FILE = "FILE";
    private static final String ENCRYPTION_KEY = "S8e2aAZPFgz94Fs4";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1) Read from file.");
            System.out.println("2) Modify contents of file.");
            System.out.println("3) Exit.");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    readSettings();
                    break;
                case 2:
                    modifySettings(scanner);
                    break;
                case 3:
                    System.out.println("Exiting");
                    return;
                default:
                    System.out.println("Invalid please try again.");
            }
        }
    }

    private static void readSettings() {
        try {
            String decryptedData = decryptFile(SETTINGS_FILE, ENCRYPTION_KEY);
            System.out.println("Current contents of settings: " + decryptedData);
        } catch (Exception e) {
            System.out.println("Error reading contents of settings: " + e.getMessage());
        }
    }

    private static void modifySettings(Scanner scanner) {
        try {
            System.out.print("Enter string value: ");
            String newString = scanner.nextLine();
            System.out.print("Enter integer value: ");
            int newInt = scanner.nextInt();
            System.out.print("Enter float value: ");
            float newFloat = scanner.nextFloat();
            scanner.nextLine();

            String newSettings = newString + ", " + newInt + ", " + newFloat;
            encryptFile(SETTINGS_FILE, newSettings, ENCRYPTION_KEY);
            System.out.println("Settings updated!.");
        } catch (Exception e) {
            System.out.println("Error modifying contents of settings: " + e.getMessage());
        }
    }

    private static void encryptFile(String filePath, String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        String encodedData = Base64.getEncoder().encodeToString(encryptedData);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(encodedData);
        }
    }

    private static String decryptFile(String filePath, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        String encodedData;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            encodedData = reader.readLine();
        }

        byte[] encryptedData = Base64.getDecoder().decode(encodedData);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        return new String(decryptedData);
    }
}
