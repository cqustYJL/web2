package com.note.portal.util;

import java.io.IOException;
import java.io.InputStream;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.note.portal.properties.CatalogProperties;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Component
public class FastDFSClientUtil {
    @Autowired
    CatalogProperties catalogProperties;
    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile((InputStream)file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return getResAccessUrl(storePath);
    }

    // 封装文件完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = "http://" + catalogProperties.getFileNginxUrl() + "/" + storePath.getFullPath();
        return fileUrl;
    }
    public byte[] downloadFile(String fileUrl) throws IOException {
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes = storageClient.downloadFile(group, path, downloadByteArray);
        return bytes;
    }
}
