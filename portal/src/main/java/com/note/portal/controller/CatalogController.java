package com.note.portal.controller;

import com.note.portal.po.Catalog;
import com.note.portal.vo.EasyUITreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月11日
 **/
@RestController
@RequestMapping("/catalog")
public class CatalogController {
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("/addFolder")
    public String addFolder(String father_id, String catalog_name, Boolean isFolder){
        //插入该文件
        Catalog catalog = new Catalog(catalog_name,isFolder,father_id);
        catalog = mongoTemplate.insert(catalog);
        //向父文件中添加该子文件
        Update update = new Update().push("children_id", catalog.getCatalog_id());
        mongoTemplate.updateFirst(query(where("catalog_id").is(father_id)), update, Catalog.class);
        return catalog.getCatalog_id();
    }
    @RequestMapping("/removeFolder")
    public void removeFolder(String father_id, String catalog_id){
        //删除该文件
        mongoTemplate.remove(query(where("catalog_id").is(catalog_id)), Catalog.class);
        //从父文件中删除子文件
        Update update = new Update().pull("children_id", catalog_id);
        mongoTemplate.updateFirst(query(where("catalog_id").is(father_id)), update, Catalog.class);
    }
    @RequestMapping("/renameFolder")
    public void renameFolder(String catalog_id,String catalog_name){
        Update update = new Update().set("catalog_name", catalog_name);
        mongoTemplate.updateFirst(query(where("catalog_id").is(catalog_id)), update, Catalog.class);
    }
    @RequestMapping("/getFolder")
    public EasyUITreeNode getFolder(){
        List<Catalog> list = mongoTemplate.findAll(Catalog.class);
        EasyUITreeNode easyUITreeNode = null;
        for(Catalog catalog : list) {
            if("0".equals(catalog.getFather_id())) {//主文件夹
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
}
