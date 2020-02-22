package com.note.portal.controller;

import com.note.portal.service.CatalogService;
import com.note.portal.util.FastDFSClientUtil;
import com.note.portal.vo.EasyUITreeNode;
import com.note.portal.vo.ResponseResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

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
    @Autowired
    FastDFSClientUtil fastDFSClientUtil;

    @RequestMapping("/addFolder")
    public ResponseResult addFolder(String father_id, String catalog_name, Integer catalog_type){
        try {
            return catalogService.addFolder(father_id, catalog_name, catalog_type, null);
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
    public EasyUITreeNode getAllFolder(){
        try{
            return catalogService.getAllFolder();
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

    @RequestMapping("/uploadImage")
    public ResponseResult uploadImage(String base64) {
        try {
            return catalogService.uploadImage(base64);
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
    @RequestMapping("/uploadFile")
    public ResponseResult uploadFile(String father_id, @RequestParam("note_file")MultipartFile note_file){
        try {
            return catalogService.uploadFile(father_id, note_file);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return new ResponseResult(false,"上传失败");
    }
    @RequestMapping("/downloadFile")
    public void downloadFile(String catalog_id, HttpServletResponse response) {
        try{
            catalogService.downloadFile(catalog_id,response);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

    }
}
