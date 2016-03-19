package br.com.marquesapps.security.model;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="tb_rule")
public class Rule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "description", nullable = false)
	@NotNull
	@Size(min=2, max=50)
	private String description;
	
	@Column(name="active", nullable=false)
	private boolean active;
	
	@OneToMany(mappedBy="rule",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<UserRule> userrules;
	
	public Rule() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<UserRule> getUserrules() {
		return userrules;
	}

	public void setUserrules(Set<UserRule> userrules) {
		this.userrules = userrules;
	}
}