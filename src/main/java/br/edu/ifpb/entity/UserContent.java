package br.edu.ifpb.entity;

import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="TB_USER_CONTENT")
public class UserContent {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
    	generator = "userc_seq_gen")
	@SequenceGenerator(name = "userc_seq_gen", 
		sequenceName = "tb_userc_seq", 
		allocationSize = 1)
    private Long id;

	@Lob
	private byte[] photo;
	
//	@OneToOne(fetch = FetchType.EAGER, mappedBy = "content")
//	private User user;
//
//	@Override
//	public String toString() {
//		return "UserContent [id=" + id + ", photo=" + Arrays.toString(photo) + ", user=" + user.getId() + "]";
//	}
}