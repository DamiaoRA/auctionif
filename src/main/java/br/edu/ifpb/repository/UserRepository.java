package br.edu.ifpb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.entity.User;
import br.edu.ifpb.entity.dto.UserDTO;
import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
//	@Query("SELECT u FROM User u WHERE u.email = ?1")
//	public User findByEmail(String email);

	Optional<User> findByEmail(String email);

	@Query("SELECT new br.edu.ifpb.entity.dto.UserDTO("
			+ "u.id, u.firstName, "
			+ "u.lastName, "
			+ "u.email) FROM User u WHERE u.email = :email")
    Optional<UserDTO> findDtoByEmail(String email);

	@Modifying
    @Transactional
    @Query("UPDATE User u SET "
    		+ "u.firstName = :firstName, "
    		+ "u.lastName = :lastName, "
    		+ "u.email = :email, "
    		+ "u.password = :password "
    		+ "WHERE u.id = :id")
    int updatenorules(
    		@Param("id") Long id, 
    		@Param("firstName") String firstName,
    		@Param("lastName") String lastName, 
    		@Param("email") String email,
    		@Param("password") String password);
}