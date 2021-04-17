package app.processor;

import app.controller.CcbsController;
import app.model.Customer;
import app.model.Sim;
import app.model.User;

import java.util.List;

public class ProcessBuilder {
    private List<Sim> sims;
    private List<Customer> customers;
    private int simPerCustomer;
    private boolean auto;
    private boolean excel;
    private boolean zip;
    private boolean jpg;
    private String path;
    private String maHd;
    private User user;
    private String profile;
    private CcbsController controller;

    public ProcessBuilder sims(List<Sim> sims) {
        this.sims = sims;
        return this;
    }

    public ProcessBuilder customers(List<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public ProcessBuilder simPerCustomer(int simPerCustomer) {
        this.simPerCustomer = simPerCustomer;
        return this;
    }

    public ProcessBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ProcessBuilder maHd(String maHd) {
        this.maHd = maHd.isEmpty() ? "000000" : maHd;
        return this;
    }

    public ProcessBuilder user(User user) {
        this.user = user;
        return this;
    }

    public ProcessBuilder auto(boolean auto) {
        this.auto = auto;
        return this;
    }

    public ProcessBuilder excel(boolean excel) {
        this.excel = excel;
        return this;
    }

    public ProcessBuilder zip(boolean zip) {
        this.zip = zip;
        return this;
    }

    public ProcessBuilder jpg(boolean jpg) {
        this.jpg = jpg;
        return this;
    }

    public List<Sim> getSims() {
        return sims;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public int getSimPerCustomer() {
        return simPerCustomer;
    }

    public boolean isAuto() {
        return auto;
    }

    public boolean isExcel() {
        return excel;
    }

    public boolean isZip() {
        return zip;
    }

    public boolean isJpg() {
        return jpg;
    }

    public String getPath() {
        return path;
    }

    public String getMaHd() {
        return maHd;
    }

    public User getUser() {
        return user;
    }

    public CcbsController getController() {
        return controller;
    }

    public ProcessBuilder controller(CcbsController controller) {
        this.controller = controller;
        return this;
    }

    public String getProfile() {
        return profile;
    }

    public ProcessBuilder profile(String profile) {
        this.profile = profile;
        return this;
    }
}
