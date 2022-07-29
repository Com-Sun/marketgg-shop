package com.nhnacademy.marketgg.server.utils;

import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.entity.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageFileHandler {

    private static final String dir = System.getProperty("user.home");

    public static List<Image> parseImages(List<MultipartFile> multipartFiles, Asset asset)
        throws IOException {

        List<Image> images = new ArrayList<>();

        if (!CollectionUtils.isEmpty(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                File dest = new File(dir, Objects.requireNonNull(multipartFile.getOriginalFilename()));
                multipartFile.transferTo(dest);

                Image image = new Image(asset, String.valueOf(dest));
                images.add(image);
            }
        }

        return images;
    }
}
