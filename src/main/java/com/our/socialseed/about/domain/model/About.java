package com.our.socialseed.about.domain.model;

public class About {
    private String version;
    private String seed;
    private String name;
    private String icono_url;
    private String description;

    public About(String version, String seed, String name, String icono_url, String description) {
        this.version = version;
        this.seed = seed;
        this.name = name;
        this.icono_url = icono_url;
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcono_url() {
        return icono_url;
    }

    public void setIcono_url(String icono_url) {
        this.icono_url = icono_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
