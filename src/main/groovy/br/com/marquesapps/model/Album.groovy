package br.com.marquesapps.model;

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

import org.springframework.format.annotation.DateTimeFormat

import br.com.marquesapps.security.model.User

import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name="tb_album")
public class Album implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "dateini", nullable = true)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dateini;
	
	@Column(name = "datefin", nullable = true)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date datefin;
	
	@Column(name = "fullpermited", nullable = false)
	private boolean fullpermited=false;
	
	@Column(name = "active", nullable = false)
	private boolean active=true;
	
	@OneToOne
	@JoinColumn(name="user_id")
	@JsonIgnore
	public User user;

	@OneToMany(mappedBy="album",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<Image> images;

	protected Album() {}

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

	public Date getDateini() {
		return dateini;
	}

	public void setDateini(Date dateini) {
		this.dateini = dateini;
	}

	public Date getDatefin() {
		return datefin;
	}

	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	public boolean isFullpermited() {
		return fullpermited;
	}

	public void setFullpermited(boolean fullpermited) {
		this.fullpermited = fullpermited;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
}
