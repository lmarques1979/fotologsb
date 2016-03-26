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
import javax.persistence.OrderBy
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.ObjectIdGenerators

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
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
	
	@OneToMany(mappedBy="image",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
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

	public Set<Message> getMessages() {
		
		return messages;
	}


	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}
}
