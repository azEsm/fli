package ml.fli;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(locations = "application.properties", ignoreUnknownFields = false, prefix = "app")
public class ApplicationParameters {
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
