package br.edu.ifpb.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.ifpb.entity.User;

@Controller
public class UserWebController {

	@GetMapping("/user/add")
	public String home(Model model) {
		model.addAttribute("user", new User());
		return "userForm";
	}
	
	@GetMapping("/user/success")
	public String success(Model model) {
		model.addAttribute("message", "Salvo com sucesso");
		return home(model);
	}

	@PostMapping("/user/save")
    public String saveUser(@ModelAttribute User user) {
        // Salvar no banco de dados ou processar
        System.out.println("User saved: " + user);
        return "redirect:/user/success";
    }
}