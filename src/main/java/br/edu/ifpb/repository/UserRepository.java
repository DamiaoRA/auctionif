package br.edu.ifpb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}