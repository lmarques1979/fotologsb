package br.com.marquesapps.security.model;

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.hibernate.validator.constraints.Email

import br.com.marquesapps.model.Configuration

@Entity
@Table(name="tb_user")
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "firstname", nullable = false)
	@NotNull
	@Size(min=2, max=50)
	private String firstname;
	
	@Column(name = "surname", nullable = true,length=30)
	private String surname;
	
	@Column(name = "username", nullable = false, unique = true)
	@NotNull
	@Size(min=5, max=20)
	private String username;
	
	@Column(name = "password", nullable = false , length=255)
	@NotNull
	private String password;
	
	@Column(name = "email",nullable=false,unique = true)
	@NotNull
	@Size(min=5, max=70)
	@Email(message="{emailinvalido}")
	private String email;
	
	@Column(name = "image", nullable = true, length=255)
	private String image;
	
	@Column(name="active", nullable=false)
	private boolean active;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<UserRule> rules;
	
	@OneToOne(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Configuration configuration;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<UserRule> getRules() {
		return rules;
	}

	public void setRules(Set<UserRule> rules) {
		this.rules = rules;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}