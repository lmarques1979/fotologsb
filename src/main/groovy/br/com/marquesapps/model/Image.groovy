package br.com.marquesapps.model;

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.OrderBy

import org.hibernate.annotations.Where

import br.com.marquesapps.security.model.User

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators

@Entity
@Table(name="tb_image")
public class Image implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "fullpermited", nullable = false)
	private boolean fullpermited=false;
	
	@Column(name = "active", nullable = false)
	private boolean active=true;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="album_id")
	@JsonIgnore
	private Album album;
	
	@OneToOne
	@JoinColumn(name="user_id")
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy="image",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
	@Where(clause="active=1")
	@OrderBy("datemessage DESC")
	private Set<Message> messages;

	protected Image() {}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
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


	public Album getAlbum() {
		return album;
	}


	public void setAlbum(Album album) {
		this.album = album;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Set<Message> getMessages() {
		
		return messages;
	}


	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
}
