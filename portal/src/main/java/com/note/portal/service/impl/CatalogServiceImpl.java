package com.note.portal.service.impl;

import com.note.portal.po.BASE64DecodedMultipartFile;
import com.note.portal.po.Catalog;
import com.note.portal.po.NoteContent;
import com.note.portal.properties.CatalogProperties;
import com.note.portal.service.CatalogService;
import com.note.portal.util.FastDFSClientUtil;
import com.note.portal.vo.EasyUITreeNode;
import com.note.portal.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月15日
 **/
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    CatalogProperties catalogProperties;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    FastDFSClientUtil fastDFSClientUtil;

    @Override
    public ResponseResult addFolder(String father_id, String catalog_name, Integer catalog_type, String catalog_content) {
        //插入该文件
        Catalog catalog = new Catalog(catalog_name,catalog_type,father_id);
        catalog = mongoTemplate.insert(catalog);
        //向父文件中添加该子文件
        Update update = new Update().push("children_id", catalog.getCatalog_id());
        mongoTemplate.updateFirst(query(where("catalog_id").is(father_id)), update, Catalog.class);
        //如果是笔记文件，初始化文件内容为null
        if(catalog_type != 0) {
            if (catalog_type == 1) {
                NoteContent noteContent = new NoteContent(catalog.getCatalog_id(), null);
                mongoTemplate.insert(noteContent);
            } else {
                NoteContent noteContent = new NoteContent(catalog.getCatalog_id(), catalog_content);
                mongoTemplate.insert(noteContent);
                return new ResponseResult(true,catalog);
            }
        }
        return new ResponseResult(true,catalog.getCatalog_id());
    }

    @Override
    public ResponseResult removeFolder(String father_id, String catalog_id) {
        //查询该文件夹及其子文件
        List<String> catalogIdList = new ArrayList<>();
        EasyUITreeNode folder = getRemoveFolder(catalog_id,father_id);
        if(folder != null) {
            getCatalogIdList(catalogIdList, folder);
        }
        //删除文件目录
        mongoTemplate.remove(query(where("catalog_id").in(catalogIdList)), Catalog.class);
        //删除对应文件的内容
        mongoTemplate.remove(query(where("catalog_id").in(catalogIdList)), NoteContent.class);
        //从父文件中删除子文件
        Update update = new Update().pull("children_id", catalog_id);
        mongoTemplate.updateFirst(query(where("catalog_id").is(father_id)), update, Catalog.class);
        return new ResponseResult(true,"删除成功");
    }

    private void getCatalogIdList(List<String> catalogIdList, EasyUITreeNode folder) {
        catalogIdList.add(folder.getId());
        for(EasyUITreeNode child : folder.getChildren()) {
            getCatalogIdList(catalogIdList,child);
        }
    }

    @Override
    public ResponseResult renameFolder(String catalog_id, String catalog_name) {
        Update update = new Update().set("catalog_name", catalog_name);
        mongoTemplate.updateFirst(query(where("catalog_id").is(catalog_id)), update, Catalog.class);
        return new ResponseResult(true,"修改成功");
    }

    public EasyUITreeNode getRemoveFolder(String catalog_id, String father_id) {
        List<Catalog> list = mongoTemplate.findAll(Catalog.class);
        EasyUITreeNode easyUITreeNode = null;
        for(Catalog catalog : list) {
            if(catalog_id.equals(catalog.getCatalog_id())) {
                easyUITreeNode  = new EasyUITreeNode(catalog_id,catalog.getCatalog_name(),catalog.getCatalog_type() == 0 ? "closed" : "open",catalog.getCatalog_type(),father_id);
                getChildren(easyUITreeNode,list,true);
                deleteUploadFile(catalog.getCatalog_id(),catalog.getCatalog_type());
                break;
            }
        }
        return easyUITreeNode;
    }

    private void deleteUploadFile(String catalog_id, Integer catalog_type) {
        if(catalog_type == 2 || catalog_type == 3) {

            NoteContent noteContent = mongoTemplate.findById(catalog_id, NoteContent.class);
            String content = noteContent.getContent();
            if(catalog_type == 2) {
                content = content.substring(content.indexOf("http://"),content.length() - 1);
            }
            String fileNginxUrl = catalogProperties.getFileNginxUrl();
            String fileUrl = content.substring(content.indexOf(fileNginxUrl) + fileNginxUrl.length() + 1);
            fastDFSClientUtil.deleteFile(fileUrl);
        }
    }

    private void getChildren(EasyUITreeNode easyUITreeNode, List<Catalog> list, boolean remove) {
        for(Catalog catalog : list) {
            if(easyUITreeNode.getId().equals(catalog.getFather_id())) {
                EasyUITreeNode easyUITreeNode02  = new EasyUITreeNode(catalog.getCatalog_id(),catalog.getCatalog_name(),catalog.getCatalog_type() == 0 ? "closed" : "open",catalog.getCatalog_type(),catalog.getFather_id());
                easyUITreeNode.getChildren().add(easyUITreeNode02);
                getChildren(easyUITreeNode02,list, remove);
                if(remove) {
                    deleteUploadFile(catalog.getCatalog_id(), catalog.getCatalog_type());
                }
            }
        }
    }

    @Override
    public ResponseResult uploadImage(String base64) throws IOException {
        if (base64 != null && !"".equals(base64)) {
            MultipartFile file = BASE64DecodedMultipartFile.base64ToMultipartFile(base64);
            String url = fastDFSClientUtil.uploadFile(file);
            return new ResponseResult(true,"![](" + url + ")");
        } else {
            return new ResponseResult(false,"图片上传失败");
        }
    }

    @Override
    public ResponseResult getNoteContent(String catalog_id) {
        NoteContent noteContent = mongoTemplate.findOne(query(where("catalog_id").is(catalog_id)), NoteContent.class);
        return new ResponseResult(true,noteContent != null ? noteContent.getContent() : "");
    }

    @Override
    public ResponseResult modifyContent(String catalog_id, String catalog_content) {
        Update update = new Update().set("content", catalog_content);
        mongoTemplate.upsert(query(where("catalog_id").is(catalog_id)), update, NoteContent.class);
        return new ResponseResult(true,"保存成功");
    }

    @Override
    public ResponseResult uploadFile(String father_id, MultipartFile note_file) throws IOException {
        if(note_file.isEmpty())
            return new ResponseResult(false,"上传文件为空");
        String url = fastDFSClientUtil.uploadFile(note_file);
        Integer catalog_type = note_file.getContentType().indexOf("image") == -1 ? 3 : 2 ;
        if(catalog_type == 2) {
            url = "![](" + url + ")";
        }
        ResponseResult responseResult = addFolder(father_id, note_file.getOriginalFilename(), catalog_type, url);
        return responseResult;
    }

    @Override
    public EasyUITreeNode getAllFolder() {
        List<Catalog> list = mongoTemplate.findAll(Catalog.class);
        EasyUITreeNode easyUITreeNode = null;
        for(Catalog catalog : list) {
            if("0".equals(catalog.getFather_id())) {
                easyUITreeNode  = new EasyUITreeNode(catalog.getCatalog_id(),catalog.getCatalog_name(),catalog.getCatalog_type() == 0 ? "closed" : "open",catalog.getCatalog_type(),"0");
                getChildren(easyUITreeNode,list, false);
                break;
            }
        }
        return easyUITreeNode;
    }

    @Override
    public void downloadFile(String catalog_id, HttpServletResponse response) throws IOException {
        Catalog catalog = mongoTemplate.findById(catalog_id, Catalog.class);
        NoteContent noteContent = mongoTemplate.findById(catalog_id, NoteContent.class);
        String content = noteContent.getContent();
        byte[] bytes = new byte[0];
        if(catalog.getCatalog_type() == 3) {
            String fileNginxUrl = catalogProperties.getFileNginxUrl();
            String fileUrl = content.substring(content.indexOf(fileNginxUrl) + fileNginxUrl.length() + 1);
            bytes = fastDFSClientUtil.downloadFile(fileUrl);
        } else {
            catalog.setCatalog_name(catalog.getCatalog_name() + ".md");
            bytes = content.getBytes();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(catalog.getCatalog_name(), "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
