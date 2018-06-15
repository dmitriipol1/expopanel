package expo.controller;

import expo.model.Mashine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import expo.service.MashineServiceImpl;

import java.util.List;

@Controller
public class ExpoController {
    private Mashine target = new Mashine("region");
    private boolean showOnlyOnline = false;

    @Autowired
    private MashineServiceImpl mashineService;

    private List<Mashine> mashinesList;

    @RequestMapping(value = {"getAllMashines", "/"})
    public ModelAndView getAllMashines(Model model) {
        mashinesList = mashineService.getAllMashines(showOnlyOnline);
        model.addAttribute("target", target.getName());
        return new ModelAndView("index", "mashinesList", mashinesList);
    }

    @RequestMapping("changeTarget")
    public ModelAndView createBook(@ModelAttribute Mashine target) {
        return new ModelAndView("targetForm", "target", target);
    }

    @RequestMapping("saveTarget")
    public ModelAndView saveBook(@ModelAttribute Mashine target) {
        this.target = target;
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("showOnline")
    public ModelAndView showOnline() {
        showOnlyOnline = !showOnlyOnline;
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
