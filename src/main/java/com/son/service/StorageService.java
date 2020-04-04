package com.son.service;

import com.son.util.common.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final ServletContext servletContext;

    public String upload(MultipartFile file) throws IOException {
        return upload(file, createStorageFolder());
    }

    private String upload(MultipartFile file, File directory) throws IOException {
        String originalFilename = file.getOriginalFilename();

        if ("".equals(originalFilename)) {
            return null;
        }

        String fileName = FileUtil.rename(originalFilename);

        String fileUrl = directory.getAbsolutePath() + File.separator + fileName;

        file.transferTo(new File(fileUrl));

        log.info("Uploaded 1 file: " + fileUrl);

        return fileName;
    }

    private File createStorageFolder() {
        final String DIR_PATH = servletContext.getRealPath("") + "upload";

        File uploadDirectory = new File(DIR_PATH);

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        return uploadDirectory;
    }
}
