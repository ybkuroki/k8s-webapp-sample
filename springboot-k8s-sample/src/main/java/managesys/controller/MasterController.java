package managesys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import managesys.model.Category;
import managesys.model.Format;
import managesys.service.MasterService;

@RestController
@RequestMapping("/api/master")
public class MasterController {

    @Autowired
    MasterService service;

    @GetMapping("/category")
    @ResponseBody
    public List<Category> listCategory() {
        return service.findAllCategories();
    }

    @GetMapping("/format")
    @ResponseBody
    public List<Format> listFormat() {
        return service.findAllFormats();
    }

}