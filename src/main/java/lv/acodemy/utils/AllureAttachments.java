package lv.acodemy.utils;

import io.qameta.allure.Attachment;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class AllureAttachments {

    @Attachment(value = "Видео прохождения теста", type = "video/avi")
    public static byte[] attachVideo(String path) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(path));
            System.out.println("📎 Размер видео: " + data.length + " байт");
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

