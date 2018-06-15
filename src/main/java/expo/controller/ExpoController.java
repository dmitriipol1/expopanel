package expo.controller;

import expo.model.Mashine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import expo.service.MashineServiceImpl;

import java.util.List;

@Controller
public class ExpoController {
    private Mashine target = new Mashine("localhost");
    private boolean showOnlyOnline = false;
    private List<Mashine> mashinesList;

    private final MashineServiceImpl mashineService;

    @Autowired
    public ExpoController(MashineServiceImpl mashineService) {
        this.mashineService = mashineService;
    }

    @RequestMapping(value = {"getAllMashines", "/"})
    public ModelAndView getAllMashines(Model model) {
        mashinesList = mashineService.getAllMashines(showOnlyOnline);
        model.addAttribute("target", target.getName());
        return new ModelAndView("index", "mashinesList", mashinesList);
    }

    @RequestMapping("changeTarget")
    public ModelAndView changeTarget(@ModelAttribute Mashine target) {
        return new ModelAndView("targetForm", "target", target);
    }

    @RequestMapping("saveTarget")
    public ModelAndView saveTarget(@ModelAttribute Mashine target) {
        this.target = target;
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("addNewServer")
    public ModelAndView addNewServer() {
        return new ModelAndView("serverForm", "server", new Mashine());
    }

    @RequestMapping("saveNewServer")
    public ModelAndView saveNewServer(@ModelAttribute Mashine server, @RequestParam(required = false) boolean kinect) {
        server.setKinect(kinect);
        mashineService.addNewServer(server);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("showOnline")
    public ModelAndView showOnline() {
        showOnlyOnline = !showOnlyOnline;
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadModules")
    public ModelAndView uploadModules(@RequestParam String name) {
        Mashine server = mashinesList.stream().filter(m -> m.getName().equals(name)).findFirst().get();
        mashineService.uploadModules(target, server);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadContent")
    public ModelAndView uploadContent(@RequestParam String name) {
        Mashine server = mashinesList.stream().filter(m -> m.getName().equals(name)).findFirst().get();
        mashineService.uploadContent(target, server);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadVVVV")
    public ModelAndView uploadVVVV(@RequestParam String name) {
        Mashine server = mashinesList.stream().filter(m -> m.getName().equals(name)).findFirst().get();
        mashineService.uploadVVVV(target, server);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("uploadAll")
    public ModelAndView uploadAll(@RequestParam String name) {
        Mashine server = mashinesList.stream().filter(m -> m.getName().equals(name)).findFirst().get();
        mashineService.uploadAll(target, server);
        return new ModelAndView("redirect:/");
    }
//
//    @RequestMapping("searchBook")
//    public ModelAndView searchBook(@RequestParam("searchName") String searchName) {
//        List<Book> booksList = bookService.getAllBooks(searchName);
//        return new ModelAndView("index", "booksList", booksList);
//    }
//
//    @RequestMapping("readBook")
//    public ModelAndView readBook(@RequestParam long id, @ModelAttribute Book book) {
//        book = bookService.getBook(id);
//        book.setReadAlready(!book.isReadAlready());
//        bookService.updateBook(book);
//        return new ModelAndView("redirect:/");
//    }
//
//    @RequestMapping("createBook")
//    public ModelAndView createBook(@ModelAttribute Book book) {
//        return new ModelAndView("bookForm", "bookObject", book);
//    }
//
//    @RequestMapping("editBook")
//    public ModelAndView editBook(Model expo.model, @RequestParam long id, @ModelAttribute Book item) {
//        item = bookService.getBook(id);
//        expo.model.addAttribute("description", item.getDescription());
//        return new ModelAndView("bookForm", "bookObject", item);
//    }
//

//
//    @RequestMapping("deleteBook")
//    public ModelAndView deleteBook(@RequestParam long id) {
//        bookService.deleteBook(id);
//        return new ModelAndView("redirect:/");
//    }
}
