package br.edu.ifpb.restcontroller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.entity.User;
import br.edu.ifpb.entity.dto.UserDTO;
import br.edu.ifpb.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

	@Autowired
	protected UserService userService;

	@PostMapping("save")
    public ResponseEntity<?> save(@RequestBody User user) {
		try {
	        // Salvar no banco de dados ou processar
			userService.save(user);
			return new ResponseEntity<User>(user, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

	@PutMapping("update/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO userdto){
		try {
			userdto.setId(id);

			//Verifica se o usuário existe
			User user = userService.findById(id);
			//Copia as propriedades do DTO para a entidade
			BeanUtils.copyProperties(userdto, user);
			//Edita usuário
			userService.save(user);

			return ResponseEntity.ok(userdto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage()); 
		}
	}

	@GetMapping("list")
	public ResponseEntity<List<User>> list() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		try {
			userService.deleteById(id);
			
			/*O código 204 No Content de uma resposta HTTP significa 
			que o servidor processou com sucesso a requisição, mas 
			não está retornando nenhum conteúdo no corpo da resposta.*/
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage()); 
		}
	}

	@GetMapping("find")
	public ResponseEntity<?> find(
			@RequestParam(value="firstname", required = false) String firstName,
			@RequestParam(value="lastname", required = false) String lastName,
			@RequestParam(required = false) Long id){

		try {
			User filter = new User();
			filter.setId(id);
			filter.setFirstName(firstName);
			filter.setLastName(lastName);

			List<UserDTO> dtos = userService.findByFilter(filter);

			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage()); 
		}
	}

	@GetMapping("find/like")
	public ResponseEntity<?> findUsers(
			@RequestParam(value="firstname", required = false) String firstName,
			@RequestParam(value="lastname", required = false) String lastName){

		try {
			User filter = new User();
			filter.setFirstName(firstName);
			filter.setLastName(lastName);

			List<UserDTO> dtos = userService.findByExampleMatch(filter);
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("find/page")
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userService.findAllDto(pageable);
    }

	//localhost:8080/api/user/find/filter/page?firstName=a&lastName=a&page=0&size=5&sort=lastName,asc
	@GetMapping("find/filter/page")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable) {

		User filter = new User();
		filter.setFirstName(firstName);
		filter.setLastName(lastName);

		Page<UserDTO> page = userService.findByExampleMatch(filter, pageable);
		return ResponseEntity.ok(page);
	}
}