package br.edu.ifpb.entity.dto;

import java.util.List;

import br.edu.ifpb.entity.Role;
import br.edu.ifpb.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private List<Role> roles;
	
	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public static UserDTO fromEntity(User user) {
        return new UserDTO(
        		user.getId(), 
        		user.getFirstName(), 
        		user.getLastName(),
        		user.getEmail(),
        		user.getRoles());
    }
	
	public User fromDTO() {
		User u = new User();
		u.setId(id);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setEmail(email);
		u.setRoles(roles);
		return u;
	}
}
