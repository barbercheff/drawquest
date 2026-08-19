package com.drawquest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "drawquest.upload")
public class UploadProperties {

    private String drawingsDir = "uploads/drawings";

    private String drawingsPublicPath = "/uploads/drawings";

    public String getDrawingsDir() {
        return drawingsDir;
    }

    public void setDrawingsDir(String drawingsDir) {
        this.drawingsDir = drawingsDir;
    }

    public String getDrawingsPublicPath() {
        return drawingsPublicPath;
    }

    public void setDrawingsPublicPath(String drawingsPublicPath) {
        this.drawingsPublicPath = drawingsPublicPath;
    }
}
