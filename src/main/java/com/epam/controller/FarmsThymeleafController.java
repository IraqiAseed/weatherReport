package com.epam.controller;



import com.epam.model.FarmJava;
import com.epam.service.Implementation.FarmJavaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import javax.validation.Valid;
import java.util.Optional;
@Controller

public class FarmsThymeleafController {
    private FarmJavaService farmService;

    @Autowired
    public FarmsThymeleafController(FarmJavaService farmService){
        this.farmService = farmService;
    }

    @GetMapping("/farms-ui")
    public String farms(Model model){
        model.addAttribute("farms",farmService.getAllFarms());
        return "farms";
    }

    @GetMapping("/delete-FarmJava/{id}")
    public String removeFarmJava(@PathVariable("id") Integer id, Model model) {
        farmService.deleteFarmById(id);
        model.addAttribute("FarmJavas", farmService.getAllFarms());
        return "farms";
    }

    @GetMapping(value = {"/edit-add-farm/{id}", "/edit-add-farm"})
    public String editFarmJava(@PathVariable("id") Optional<Integer> id, Model model) {
        FarmJava FarmJava = id.isPresent() ?
                farmService.findFarmById(id.get()).get() : new FarmJava();
        model.addAttribute("FarmJava", FarmJava);
        return "add-edit";
    }

    @PostMapping("/save-FarmJava")
    public String editFarmJava(@ModelAttribute("FarmJava") @Valid FarmJava FarmJava,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-edit";
        }
        farmService.saveFarm(FarmJava);
        return "redirect:FarmJavas-ui";
    }
}
