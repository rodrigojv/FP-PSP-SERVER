package py.org.fundacionparaguaya.pspserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application")
public class ApplicationProperties {

    private Aws aws = new Aws();

    public Aws getAws() {
        return this.aws;
    }

    public void setAws(Aws aws) {
        this.aws = aws;
    }

    public static class Aws {
        private String strRegion;
        private String bucketName;

        private String orgsImageDirectory;
        private String hubsImageDirectory;
        private String familiesImageDirectory;


        public String getStrRegion() {
            return strRegion;
        }

        public void setStrRegion(String strRegion) {
            this.strRegion = strRegion;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getOrgsImageDirectory() {
            return orgsImageDirectory;
        }

        public void setOrgsImageDirectory(String orgsImageDirectory) {
            this.orgsImageDirectory = orgsImageDirectory;
        }

        public String getHubsImageDirectory() {
            return hubsImageDirectory;
        }

        public void setHubsImageDirectory(String hubsImageDirectory) {
            this.hubsImageDirectory = hubsImageDirectory;
        }

        public String getFamiliesImageDirectory() {
            return familiesImageDirectory;
        }

        public void setFamiliesImageDirectory(String familiesImageDirectory) {
            this.familiesImageDirectory = familiesImageDirectory;
        }
    }

    private Client client = new Client();

    private Templates templates = new Templates();

    private Sender sender = new Sender();

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Templates getTemplates() {
        return templates;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public static class Client {
        private String loginUrl;

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }

    }

    public static class Templates {
        private String resetMail;

        public String getResetMail() {
            return resetMail;
        }

        public void setResetMail(String resetMail) {
            this.resetMail = resetMail;
        }

    }

    public static class Sender {
        private String from;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

    }
}