package org.example.etc;

import org.example.Entity.Character;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SocialString {

    private static final String algorithm = "AES";
    private static final String key = "1234567890123456";


    public String getString(Character character) throws Exception {
        String string = character.getId()+","+character.getAnimal()+","+character.getFun()+","+character.getHealth()+","+
                character.getHungry()+","+character.getLevel()+","+character.getMax_score_1()+","+character.getMax_score_2()+","+
                character.getMax_score_3()+","+character.getMax_score_4()+","+character.getMoney()+","+character.getName()+","+
                character.getThirst()+","+character.getBackId()+","+character.getChairId()+","+character.getTableId()+","+character.getXp();
        return encrypt(string);
    }

    private String encrypt(String plainText) throws Exception{
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static Character createCharacterFromEncryptedString(String encryptedString) throws Exception {
        String decryptedString = decrypt(encryptedString);
        String[] parts = decryptedString.split(",");

        if (parts.length != 17) {
            throw new IllegalArgumentException("Invalid encrypted string format");
        }

        Character character = new Character();
        character.setId(Long.parseLong(parts[0]));
        character.setAnimal(parts[1]);
        character.setFun(Integer.parseInt(parts[2]));
        character.setHealth(Integer.parseInt(parts[3]));
        character.setHungry(Integer.parseInt(parts[4]));
        character.setLevel(Integer.parseInt(parts[5]));
        character.setMax_score_1(Integer.parseInt(parts[6]));
        character.setMax_score_2(Integer.parseInt(parts[7]));
        character.setMax_score_3(Integer.parseInt(parts[8]));
        character.setMax_score_4(Integer.parseInt(parts[9]));
        character.setMoney(Integer.parseInt(parts[10]));
        character.setName(parts[11]);
        character.setThirst(Integer.parseInt(parts[12]));
        character.setBackId(Long.parseLong(parts[13]));
        character.setChairId(Long.parseLong(parts[14]));
        character.setTableId(Long.parseLong(parts[15]));
        character.setXp(Integer.parseInt(parts[16]));

        return character;
    }


}
