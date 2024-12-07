package br.edu.ifpb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}