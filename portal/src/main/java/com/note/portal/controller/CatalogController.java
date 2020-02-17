package com.note.portal.controller;

import com.note.portal.service.CatalogService;
import com.note.portal.vo.EasyUITreeNode;
import com.note.portal.vo.ResponseResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月11日
 **/
@RestController
@RequestMapping("/catalog")
public class CatalogController {
    Log log = LogFactory.getLog(CatalogController.class);
    @Autowired
    CatalogService catalogService;

    @RequestMapping("/addFolder")
    public ResponseResult addFolder(String father_id, String catalog_name, Boolean isFolder){
        try {
            return catalogService.addFolder(father_id, catalog_name, isFolder);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"添加失败");
    }
    @RequestMapping("/removeFolder")
    public ResponseResult removeFolder(String father_id, String catalog_id){
        try {
            return catalogService.removeFolder(father_id, catalog_id);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"删除失败");

    }
    @RequestMapping("/renameFolder")
    public ResponseResult renameFolder(String catalog_id,String catalog_name){
        try {
            return catalogService.renameFolder(catalog_id,catalog_name);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"修改失败");
    }
    @RequestMapping("/getFolder")
    public EasyUITreeNode getFolder(){
        try{
            return catalogService.getFolder("0");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
    @RequestMapping("/getNoteContent")
    public ResponseResult getNoteContent(String catalog_id){
        try {
            return catalogService.getNoteContent(catalog_id);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"查询失败");
    }

    @RequestMapping("/uploadFile")
    public ResponseResult uploadFile(String base64) {
        try {
            return catalogService.uploadFile(base64);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"图片上传失败");
    }
    @RequestMapping("/modifyContent")
    public ResponseResult modifyContent(String catalog_id, String catalog_content){
        try {
            return catalogService.modifyContent(catalog_id, catalog_content);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"保存失败");
    }

}
