package com.note.portal.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "catalog")
public class CatalogProperties {
    private String fileNginxUrl;

    public String getFileNginxUrl() {
        return fileNginxUrl;
    }

    public void setFileNginxUrl(String fileNginxUrl) {
        this.fileNginxUrl = fileNginxUrl;
    }
}
