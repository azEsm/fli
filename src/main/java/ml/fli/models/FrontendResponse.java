package ml.fli.models;

import java.util.HashSet;
import java.util.Set;

public class FrontendResponse {
    private Set<Raw> result = new HashSet<>();

    public Set<Raw> getResult() {
        return result;
    }

    public void setResult(Set<Raw> result) {
        this.result = result;
    }

    /**
     * Представляет собой одну строку
     * таблицы результатов
     */
    public static class Raw {
        private String photoUrl;
        private String accountUrl;
        private String name;
        private double rate;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getAccountUrl() {
            return accountUrl;
        }

        public void setAccountUrl(String accountUrl) {
            this.accountUrl = accountUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }
}
