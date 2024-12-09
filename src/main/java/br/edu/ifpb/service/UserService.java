package br.edu.ifpb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.edu.ifpb.entity.User;
import br.edu.ifpb.entity.dto.UserDTO;
import br.edu.ifpb.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User save(User u) {
		return userRepository.save(u);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public void deleteById(Long id) {
		if(!userRepository.existsById(id)) {
			throw new RuntimeException("User not found");
		}
		userRepository.deleteById(id);
	}

	public Page<User> findAll(PageRequest page) {
		return userRepository.findAll(page);
	}

	public Page<UserDTO> findAllDto(PageRequest page) {
		Page<User> userPage = userRepository.findAll(page);

		 // Converter para DTO
        Page<UserDTO> userDTOPage = userPage.map(user -> 
            new UserDTO(user.getId(), user.getFirstName(), user.getLastName())
        );

        return userDTOPage;
	}
}
