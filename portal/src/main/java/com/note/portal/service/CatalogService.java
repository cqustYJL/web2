package com.note.portal.service;

import com.note.portal.vo.EasyUITreeNode;
import com.note.portal.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CatalogService {
    ResponseResult addFolder(String father_id, String catalog_name, Integer catalog_type, String catalog_content);

    ResponseResult removeFolder(String father_id, String catalog_id);

    ResponseResult renameFolder(String catalog_id, String catalog_name);

    ResponseResult uploadImage(String base64) throws IOException;

    ResponseResult getNoteContent(String catalog_id);

    ResponseResult modifyContent(String catalog_id, String catalog_content);

    ResponseResult uploadFile(String father_id, MultipartFile note_file) throws IOException;

    EasyUITreeNode getAllFolder();

    void downloadFile(String catalog_id, HttpServletResponse response) throws IOException;
}
