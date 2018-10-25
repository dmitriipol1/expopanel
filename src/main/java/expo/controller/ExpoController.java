package expo.controller;

import expo.model.Mashine;
import expo.service.MashineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExpoController {
    private Mashine from = new Mashine("localhost");
    private boolean showOnlyOnline = false;
//    private List<Mashine> mashinesList;

    private final MashineServiceImpl mashineService;

    @Autowired
    public ExpoController(MashineServiceImpl mashineService) {
        this.mashineService = mashineService;
    }

    @RequestMapping(value = {"getAllMashines", "/"})
    public ModelAndView getAllMashines(Model model) {
        model.addAttribute("target", from.getName());
        model.addAttribute("isPinging", mashineService.getIsPinging());
        return new ModelAndView("index", "mashinesList", mashineService.getAllMashines(showOnlyOnline));
    }

    @RequestMapping("changeTarget")
    public ModelAndView changeTarget() {
        return new ModelAndView("targetForm", "target", from);
    }

    @RequestMapping("saveTarget")
    public ModelAndView saveTarget(@RequestParam("name") String name) {
        from = new Mashine(name);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("addNewServer")
    public ModelAndView addNewServer() {
        return new ModelAndView("serverForm", "server", new Mashine());
    }

    @RequestMapping("saveNewServer")
    public ModelAndView saveNewServer(@RequestParam String name, @RequestParam(required = false) boolean kinect) {
            mashineService.addNewServer(new Mashine(name, kinect));
        return new ModelAndView("listSrv", "listSrv", mashineService.getAllMashines(false));
    }

    @RequestMapping("showOnline")
    public ModelAndView showOnline() {
        showOnlyOnline = !showOnlyOnline;
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadModules")
    public ModelAndView uploadModules(@RequestParam String name) {
        mashineService.uploadModules(from, name);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadContent")
    public ModelAndView uploadContent(@RequestParam String name) {
        mashineService.uploadContent(from, name);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadVVVV")
    public ModelAndView uploadVVVV(@RequestParam String name) {
        mashineService.uploadVVVV(from, name);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadAll")
    public ModelAndView uploadAll(@RequestParam String name) {
        mashineService.uploadAll(from, name);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("backup")
    public ModelAndView backup() {
        String report = mashineService.backup(from);
        String viewname;
        viewname = report.length() > 0 ? "backupReport" : "/";
        return new ModelAndView(viewname, "report", report);
    }

    @RequestMapping("listSrv")
    public ModelAndView listSrv() {
        return new ModelAndView("listSrv", "listSrv", mashineService.getAllMashines(false));
    }

    @RequestMapping("deleteSrv")
    public ModelAndView deleteSrv(@RequestParam("name") String name) {
        mashineService.deleteSrv(name);
        return new ModelAndView("listSrv", "listSrv", mashineService.getAllMashines(false));
    }

    @RequestMapping("setKinect")
    public ModelAndView setKinect(@RequestParam("name") String name) {
        mashineService.setKinect(name);
        return new ModelAndView("listSrv", "listSrv", mashineService.getAllMashines(false));
    }

    @RequestMapping("kill")
    public ModelAndView kill() {
        mashineService.kill();
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("ping")
    public ModelAndView ping() {
        mashineService.ping();
        return new ModelAndView("redirect:/");
    }
}
