package dev.journey.movieapi.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile multipartFile) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        String filePath = path + File.separator + filename;

        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdir();
        }

        Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
        return filename;
    }

    @Override
    public InputStream getResourceFile(String path, String filename) throws FileNotFoundException {
        String filePath = path + File.separator + filename;
        return new FileInputStream(filePath);
    }
}
