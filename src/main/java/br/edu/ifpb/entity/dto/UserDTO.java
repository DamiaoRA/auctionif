package br.edu.ifpb.entity.dto;

import br.edu.ifpb.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

	private Long id;
	private String firstName;
	private String lastName;
	
	public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName());
    }
}
