package com.note.portal.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.note.portal.po.BASE64DecodedMultipartFile;
import com.note.portal.po.Catalog;
import com.note.portal.po.NoteContent;
import com.note.portal.service.CatalogService;
import com.note.portal.vo.EasyUITreeNode;
import com.note.portal.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    MongoTemplate mongoTemplate;

    @Override
    public ResponseResult addFolder(String father_id, String catalog_name, Boolean isFolder) {
        //插入该文件
        Catalog catalog = new Catalog(catalog_name,isFolder,father_id);
        catalog = mongoTemplate.insert(catalog);
        //向父文件中添加该子文件
        Update update = new Update().push("children_id", catalog.getCatalog_id());
        mongoTemplate.updateFirst(query(where("catalog_id").is(father_id)), update, Catalog.class);
        //如果是文件，初始化文件内容为null
        if(!isFolder) {
            NoteContent noteContent = new NoteContent(catalog.getCatalog_id(),null);
            mongoTemplate.insert(noteContent);
        }
        return new ResponseResult(true,catalog.getCatalog_id());
    }

    @Override
    public ResponseResult removeFolder(String father_id, String catalog_id) {
        //查询该文件夹及其子文件
        List<String> catalogIdList = new ArrayList<>();
        catalogIdList.add(catalog_id);
        EasyUITreeNode folder = getFolder(catalog_id);
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
            getCatalogIdList(catalogIdList,folder);
        }
    }

    @Override
    public ResponseResult renameFolder(String catalog_id, String catalog_name) {
        Update update = new Update().set("catalog_name", catalog_name);
        mongoTemplate.updateFirst(query(where("catalog_id").is(catalog_id)), update, Catalog.class);
        return new ResponseResult(true,"修改成功");
    }

    @Override
    public EasyUITreeNode getFolder(String father_id) {
        List<Catalog> list = mongoTemplate.findAll(Catalog.class);
        EasyUITreeNode easyUITreeNode = null;
        for(Catalog catalog : list) {
            if(father_id.equals(catalog.getFather_id())) {
                easyUITreeNode  = new EasyUITreeNode(catalog.getCatalog_id(),catalog.getCatalog_name(),catalog.getFolder() ? "closed" : "open",catalog.getFolder(),"0");
                getChildren(easyUITreeNode,list);
                break;
            }
        }
        return easyUITreeNode;
    }
    private void getChildren(EasyUITreeNode easyUITreeNode, List<Catalog> list) {
        for(Catalog catalog : list) {
            if(easyUITreeNode.getId().equals(catalog.getFather_id())) {
                EasyUITreeNode easyUITreeNode02  = new EasyUITreeNode(catalog.getCatalog_id(),catalog.getCatalog_name(),catalog.getFolder() ? "closed" : "open",catalog.getFolder(),catalog.getFather_id());
                easyUITreeNode.getChildren().add(easyUITreeNode02);
                getChildren(easyUITreeNode02,list);
            }
        }
    }

    @Override
    public ResponseResult uploadFile(String base64) throws IOException {
        if (base64 != null && !"".equals(base64)) {
            MultipartFile file = BASE64DecodedMultipartFile.base64ToMultipartFile(base64);
            file.transferTo(new File("/home/wy/Pictures/files/" + System.currentTimeMillis()));
            return new ResponseResult(true,"![](http://pic.dqxfz.site/22.png)");
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
}
