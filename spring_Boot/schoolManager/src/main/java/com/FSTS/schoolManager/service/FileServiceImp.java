package com.FSTS.schoolManager.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImp implements FileService{

    @Override
    public String uploadFile(String path, MultipartFile file,String id) throws IOException {
        //get name of the file

        String fileName=id+file.getOriginalFilename();

        //get the file path
        String filePath=path+ File.separator+fileName;

        //create file object
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }
        //copy the file or upload file to the path
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return fileName ;
    }
    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {

        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
//            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        String filePath=path+ File.separator+fileName;
        return new FileInputStream(filePath);
    }
}
