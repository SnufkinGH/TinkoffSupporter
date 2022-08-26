package ru.khokhlov.tinkoffsupport.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.tinkoffsupport.dao.NodeDao;
import ru.khokhlov.tinkoffsupport.model.Branch;
import ru.khokhlov.tinkoffsupport.model.NewNode;
import ru.khokhlov.tinkoffsupport.model.Node;

import javax.validation.Valid;

@Controller
@RequestMapping("/problem")
public class MainController {

    private final NodeDao nodeDao;

    @Autowired
    public MainController(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @GetMapping()
    public String mainPage(){
        return "problem/main";
    }

    @GetMapping("/questions")
    public String index(Model model){
        model.addAttribute("nodes", nodeDao.index());
        return "problem/index";
    }

    @GetMapping("/questions/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("node", nodeDao.show(id));
        return "problem/show";
    }

    @GetMapping("/questions/new")
    public String newQuestion(@ModelAttribute("newNode") NewNode newNode){
        return "problem/new";
    }

    @PostMapping("/questions")
    public String create(@ModelAttribute("newNode") @Valid NewNode newNode,
                         BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "problem/new";

        nodeDao.add(newNode);

        return "redirect:/problem/questions";
    }

    @DeleteMapping("/questions/{id}")
    public String delete(@PathVariable("id") int id){
        nodeDao.delete(id);
        return "redirect:/problem/questions";
    }

    @GetMapping("/questions/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("node", nodeDao.show(id));
        return "problem/edit";
    }

    @PatchMapping("/questions/{id}")
    public String update(@ModelAttribute("node") @Valid Node node, BindingResult bindingResult,
                         @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "problem/edit";
        nodeDao.update(id, node);
        return "redirect:/problem/questions";
    }

    @GetMapping("/answers/{id}/edit")
    public String editBranch(@PathVariable("id") int id, Model model){
        model.addAttribute("branch", nodeDao.showBranch(id));
        return "problem/edit-branch";
    }

    @PatchMapping("/answers/{id}")
    public String updateBranch(@ModelAttribute("branch") @Valid Branch branch, BindingResult bindingResult,
                               @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "problem/edit-branch";
        nodeDao.updateBranch(id, branch);
        return "redirect:/problem/questions";
    }

    @DeleteMapping("/answers/{id}")
    public String deleteBranch(@PathVariable("id") int id){
        nodeDao.deleteBranch(id);
        return "redirect:/problem/questions";
    }
}
