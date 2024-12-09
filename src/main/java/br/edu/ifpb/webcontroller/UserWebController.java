package br.edu.ifpb.webcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifpb.entity.User;
import br.edu.ifpb.service.UserService;

@Controller
public class UserWebController {

	@Autowired
	protected UserService userService;

	@GetMapping("/user/add")
	public String home(Model model) {
		model.addAttribute("user", new User());
		return "userForm";
	}

	@PostMapping("/user/save")
    public String saveUser(@ModelAttribute User user, Model model) {
        // Salvar no banco de dados ou processar
		String ret = "userForm";
		if(user.getId() != null)
			ret = "redirect:/user/list";
			
		userService.save(user);

        model.addAttribute("successMessage", "Salvo com sucesso");
        model.addAttribute("user", new User());
        return ret;
    }

	@GetMapping("/user/list")
	public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();

        // Adiciona a lista ao modelo
        model.addAttribute("users", users);

        return "userList"; // Nome do template
	}

	@GetMapping("/user/edit/{id}")
	public String editUser(@PathVariable Long id, Model model) {
		User user = userService.findById(id);
        model.addAttribute("user", user);
        return "userForm";
	}

	@GetMapping("/user/delete/{id}")
	public String removeUser(@PathVariable Long id, Model model) {
		System.out.println("UserWebController.removeUser() " + id);
		userService.deleteById(id);
		return "redirect:/user/list";
	}
	
	@GetMapping("/user/list/page")
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<User> userPage = userService.findAll(PageRequest.of(page, size));
        model.addAttribute("userPage", userPage);

        return "userListPage";
    }
}