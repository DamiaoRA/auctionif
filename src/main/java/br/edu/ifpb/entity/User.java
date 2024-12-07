package br.edu.ifpb.entity;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name="TB_USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, 
		    generator = "user_seq_gen")
	@SequenceGenerator(name = "user_seq_gen", 
				sequenceName = "tb_user_seq", 
				allocationSize = 1)
	private Long id;
	
	private String firstName;
	
	private String lastName;

	@OneToMany(fetch = FetchType.EAGER, 
			cascade = {CascadeType.ALL}, 
			mappedBy = "owner")
	private List<Book> books;

	@OneToOne(cascade = CascadeType.ALL, 
			fetch = FetchType.LAZY, 
			orphanRemoval = true)
	@JoinColumn(name = "ID_USER_CONTENT_FK")
	private UserContent content;

	@Enumerated(EnumType.STRING)
	private GenderType gender;
	
	@Column(name="date_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	
	@Transient
	private Integer age;

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		calcAge();
	}
	
	private void calcAge() {
		Calendar dob = Calendar.getInstance();
		dob.setTime(dateOfBirth);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        // Ajustar a diferença se a data inicial ainda não chegou no mesmo ano da data final
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        this.age = age;
	}
}