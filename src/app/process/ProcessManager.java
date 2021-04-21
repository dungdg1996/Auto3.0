package app.process;

import app.Conts;
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

    public static final String FLAG_BODY = "1";
    public static final String FLAG_BREAK = "2";

    public int sdtLimit = 0;

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
        ConfigDao configDao = new ConfigDao(Conts.Path.ANH_HD_MAU + "\\" + user.getTenFileMau() + "\\config.xlsx");
        configDao.getListImg().forEach(img -> {
            ArrayList<ConfigData> configs = configDao.getData(img);
            this.processes.add(new FormProcess(configs, Conts.Path.ANH_HD_MAU + "\\" + user.getTenFileMau() + "\\" + img));
        });
        this.sdtLimit = this.processes.stream().map(FormProcess::getSl).min(Integer::compare).orElse(0);
    }

    public void run() {
        List<Map<String, String>> listData = processData();
        if (this.controller != null) {
            this.controller.setDone(0.);
            this.controller.setMax(listData.size() + 1);
        }

        for (Map<String, String> data : listData) {

            if (data.get("$dataFlag$").equals(FLAG_BODY)) {
                for (FormProcess formProcess : this.processes) {
                    formProcess.process(data);
                }
            }
            if (data.get("$dataFlag$").equals(FLAG_BREAK)) {
                for (FormProcess formProcess : this.processes) {
                    final String currPath = path + data.get("$maHinh4$");
                    final int soLuongSdt = Integer.parseInt(data.get("$soLuongSdt$"));
                    formProcess.checkWrite(sdtLimit, soLuongSdt);
                    formProcess.saveForm(currPath);
                    formProcess.clear();
                }
                final String currPath = path + data.get("$maHinh4$");
                if (zip) ZipUtils.toArchive(currPath);
            }
            if (controller != null) controller.upToProcess();
        }
        if (excel) {
            ExcelUtils.createFileWithTemplate(Conts.File.XLS_CCBS, listData, path);
        } else {
            ExcelUtils.createFileWithTemplate(Conts.File.XLSX_CCBS, listData, path);
        }
        controller.upToProcess();
    }

    private List<Map<String, String>> processData() {

        List<Map<String, String>> listData = new ArrayList<>();
        Map<String, String> data = new HashMap<>();

        int index = 0;
        Customer customer = customers.get(index);
        Sim sim = sims.get(index);
        String curr = customers.get(0).getSoGiayTo();
        while (index < sims.size() && index < customers.size()) {
            customer.setMaHD(maHd);
            AppUtils.merge(data, customer);
            AppUtils.merge(data, user);
            AppUtils.setSysTime(data);

            int i = 0;
            while (curr.equals(customer.getSoGiayTo())) {
                AppUtils.merge(data, sim);
                data.put("$sdtIndex$", String.valueOf(++i));
                data.put("$customerIndex$", String.valueOf(index));
                data.put("$dataFlag$", FLAG_BODY);
                listData.add(AppUtils.clone(data));

                index++;
                if (index == customers.size() || index == sims.size()) break;
                customer = customers.get(index);
                sim = sims.get(index);
            }

            data.put("$dataFlag$", FLAG_BREAK);
            data.put("$soLuongSdt$", String.valueOf(i));
            listData.add(AppUtils.clone(data));

            // them 1 ma hop dong
            maHd = String.valueOf(Integer.parseInt(maHd) + 1);
            while (maHd.length() < 6) maHd = "0" + maHd;
            curr = customer.getSoGiayTo();
        }
        int i = 0;
        for (Map<String, String> listDatum : listData) {
            System.out.println(i + " : " + listDatum.get("$dataFlag$") + ": " + listDatum);
        }
        return listData;
    }
}
