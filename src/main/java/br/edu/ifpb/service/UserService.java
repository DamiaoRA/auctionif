package br.edu.ifpb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.entity.Role;
import br.edu.ifpb.entity.User;
import br.edu.ifpb.entity.dto.UserDTO;
import br.edu.ifpb.mapper.UserMapper;
import br.edu.ifpb.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	protected UserMapper userMapper;

	public User save(User u) {
//		// Buscar todas as roles informadas no banco de dados
//        List<Role> validRoles = u.getRoles().stream()
//            .map(role -> roleRepository.findByRole(role.getRole())
//                    .orElseThrow(() -> new IllegalArgumentException("Role inválida: " + role.getRole())))
//            .collect(Collectors.toList());

		return userRepository.save(u);
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();

		 // Converter para DTO
        List<UserDTO> usersDto = users.stream().map(
        	user -> UserDTO.fromEntity(user)
        ).collect(Collectors.toList());

        return usersDto;
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
        Page<UserDTO> userDTOPage = userPage.map(
        	user -> UserDTO.fromEntity(user)
        );

        return userDTOPage;
	}

	public Page<UserDTO> findAllDto(Pageable pageable) {
		return userRepository.findAll(pageable)
				.map(user -> UserDTO.fromEntity(user));
	}

	public List<UserDTO> findByFilter(User filter) {
		Example<User> example = Example.of(filter);
		List<User> list = userRepository.findAll(example);
		return userMapper.toDTOList(list);
	}

	public List<UserDTO> findByExampleMatch(User filter) {
	    // Cria o exemplo
	    Example<User> example = createExample(filter);

	    // Busca as entidades correspondentes
	    List<User> list = userRepository.findAll(example);

	    return userMapper.toDTOList(list);
	}

	public Page<UserDTO> findByExampleMatch(User filter, Pageable pageable) {
		Example<User> example = createExample(filter);

	    Page<User> userPage = userRepository.findAll(example, pageable);

	    // Converter para DTO
        Page<UserDTO> userDTOPage = userPage.map(
        	user -> UserDTO.fromEntity(user)
        );
	    
	    return userDTOPage;
	}
	
	protected Example<User> createExample(User filter) {
		// Configura o ExampleMatcher para correspondência parcial e ignorar maiúsculas/minúsculas
	    ExampleMatcher matcher = ExampleMatcher.matching()
	            .withIgnoreCase() // Ignorar maiúsculas/minúsculas
	            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // Correspondência parcial

	    // Cria o exemplo
	    Example<User> example = Example.of(filter, matcher);
	    return example;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}
	
	public UserDTO findDtoByEmail(String email) {
		UserDTO user = userRepository.findDtoByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		List<Role> roles = userRepository.listRole(user.getId());
		user.setRoles(roles);
		return user;
	}

	public int updatenorules(User user) {
		return userRepository.updatenorules(
				user.getId(), 
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPassword());
	}
	
}
