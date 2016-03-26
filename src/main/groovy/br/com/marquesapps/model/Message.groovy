package br.com.marquesapps.model;

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

import org.springframework.format.annotation.DateTimeFormat

import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name="tb_message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "message", nullable = false)
	private String message;
	
	@Column(name = "fromuser", nullable = false)
	private String fromuser;
	
	@Column(name = "fromemail", nullable = false)
	private String fromemail;
	
	@Column(name = "datemessage", nullable = true)
	@DateTimeFormat(pattern="dd/MM/yyyy hh:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datemessage;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="image_id")
	//@JsonIgnore
	public Image image;
	
	@Column(name = "active", nullable = false)
	private boolean active=false;
	 
	protected Message() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromuser() {
		return fromuser;
	}

	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}

	public String getFromemail() {
		return fromemail;
	}

	public void setFromemail(String fromemail) {
		this.fromemail = fromemail;
	}

	public Date getDatemessage() {
		return datemessage;
	}

	public void setDatemessage(Date datemessage) {
		this.datemessage = datemessage;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
