package app.processor;

import app.controller.CcbsController;
import app.dao.ConfigDao;
import app.entity.ConfigData;
import app.model.Customer;
import app.model.Sim;
import app.model.User;
import app.utils.AppUtils;
import app.utils.ExcelUtils;
import app.utils.ZipUtils;

import java.util.*;

public class ProcessManager {

    public static int LIM = 0;

    protected List<Sim> sims;
    protected List<Customer> customers;
    protected int simPerCustomer;
    protected boolean auto;
    protected boolean excel;
    protected boolean zip;
    protected boolean jpg;

    protected String fileMau;
    protected String path;
    protected String maHd;
    protected User user;

    protected CcbsController controller;
    private final List<FormProcess> processes = new ArrayList<>();

    protected Finder finder = Finder.getInstance();

    public ProcessManager(ProcessBuilder builder) {
        this.sims = builder.getSims();
        this.customers = builder.getCustomers();
        this.simPerCustomer = builder.getSimPerCustomer();
        this.auto = builder.isAuto();
        this.excel = builder.isExcel();
        this.zip = builder.isZip();
        this.jpg = builder.isJpg();
        this.path = builder.getPath();
        this.maHd = builder.getMaHd();
        this.user = builder.getUser();
        this.controller = builder.getController();
        this.fileMau = user.getPath();
        this.initConfig();
    }


    public CcbsController getController() {
        return controller;
    }

    public void setController(CcbsController controller) {
        this.controller = controller;
    }

    public void initConfig() {
        ConfigDao configDao = new ConfigDao(user.getPath() + "\\config.xlsx");
        configDao.getListImg().forEach(img -> {
            ArrayList<ConfigData> configs = configDao.getData(img);
            this.processes.add(new FormProcess(configs, user.getPath() + "\\" + img));
        });

        LIM = this.processes.stream().map(FormProcess::getSl).min(Integer::compare).orElse(0);
    }

    public void run() {
        List<Map<String, String>> listData = new ArrayList<>();
        List<Map<String, String>> listTemp = new ArrayList<>();

        int simIndex = 0;
        int customerIndex = 0;
        String curr;
        Map<String, String> data = new HashMap<>();

        Customer customer = customers.get(customerIndex);
        Sim sim = sims.get(simIndex);

        while (simIndex < sims.size() && customerIndex < customers.size()) {

            // init
            customer.setMaHD(maHd);
            curr = customer.getSoGiayTo();

            AppUtils.merge(data, customer);
            AppUtils.merge(data, user);

            AppUtils.setSysTime(data);

            int i = 0;
            while (curr.equals(customer.getSoGiayTo())) {

                AppUtils.merge(data, sim);
                data.put("$sdtIndex$", String.valueOf(++i));
                listTemp.add(AppUtils.clone(data));

                customerIndex++;
                simIndex++;
                if (customerIndex == customers.size()) break;
                if (simIndex == sims.size()) break;
                customer = customers.get(customerIndex);
                sim = sims.get(simIndex);
            }


            for (Map<String, String> map : listTemp) {
                map.put("$soLuongSdt$", String.valueOf(i));
                for (FormProcess formProcess : this.processes) {
                    formProcess.process(map);
                }
            }
            for (FormProcess formProcess : this.processes) {
                formProcess.saveForm(path + data.get("$maHinh4$"));
                formProcess.clear();
            }

            if (zip) ZipUtils.archiveFile(path + data.get("$maHinh4$"));

            listData.addAll(listTemp);
            update(customers.get(customerIndex - 1), listTemp.size());

            // them 1 ma hop dong
            maHd = String.valueOf(Integer.parseInt(maHd) + 1);
            while (maHd.length() < 6) maHd = "0" + maHd;

            System.out.println(Collections.unmodifiableList(listTemp));
            listTemp.clear();
            data.clear();
        }

        update(customers.get(customerIndex - 1), 1);

        if (excel) ExcelUtils.CcbsExcel.creat(listData, path);
    }

    private void update(Customer customer, int n) {
        if (controller != null) {
            if (!finder.haveResult(customer.getSoGiayTo()))
                controller.addLog(customer.getHoVaTen() + " : " + customer.getMaHinh());
            controller.updateTable(customer);
            for (int i = 0; i < n; i++) {
                controller.upToProcess();
            }
        }
    }
}
