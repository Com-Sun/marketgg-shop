package com.nhnacademy.marketgg.server.utils;

import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.entity.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageFileHandler {

    private static final String DIR = System.getProperty("user.home");

    public static List<Image> parseImages(List<MultipartFile> multipartFiles, Asset asset)
        throws IOException {

        List<Image> images = new ArrayList<>();

        if (!CollectionUtils.isEmpty(multipartFiles)) {
            String dir = String.valueOf(Files.createDirectories(returnDir()));
            Integer sequence = 1;

            for (MultipartFile multipartFile : multipartFiles) {
                String filename = uuidFilename(multipartFile.getOriginalFilename());

                File dest = new File(dir, Objects.requireNonNull(filename));
                multipartFile.transferTo(dest);

                Image image = new Image(asset, String.valueOf(dest));
                image.setImageSequence(sequence);
                images.add(image);
                sequence++;
            }
        }

        return images;
    }

    private static Path returnDir() {
        String format = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        return Paths.get(DIR, format);
    }

    private static String uuidFilename(String filename) {
        return UUID.randomUUID() + "_" + filename;
    }
}
