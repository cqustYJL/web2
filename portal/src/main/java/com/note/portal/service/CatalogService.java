package com.note.portal.service;

import com.note.portal.vo.EasyUITreeNode;
import com.note.portal.vo.ResponseResult;

import java.io.IOException;

public interface CatalogService {
    ResponseResult addFolder(String father_id, String catalog_name, Boolean isFolder);

    ResponseResult removeFolder(String father_id, String catalog_id);

    ResponseResult renameFolder(String catalog_id, String catalog_name);

    EasyUITreeNode getFolder(String father_id);

    ResponseResult uploadFile(String base64) throws IOException;

    ResponseResult getNoteContent(String catalog_id);

    ResponseResult modifyContent(String catalog_id, String catalog_content);
}
