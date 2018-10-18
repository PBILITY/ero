package com.eormega.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by okomo
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
