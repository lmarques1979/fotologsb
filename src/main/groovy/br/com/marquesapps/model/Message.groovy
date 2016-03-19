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

@Entity
@Table(name="tb_message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "message", nullable = false)
	private String message;
	
	@Column(name = "remetente", nullable = false)
	private String remetente;
	
	@Column(name = "emailremetente", nullable = false)
	private String emailremetente;
	
	@Column(name = "datemessage", nullable = false)
	private Date datemessage;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="image_id")
	private Image image;
	
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


	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public Date getDatemessage() {
		return datemessage;
	}


	public void setDatemessage(Date datemessage) {
		this.datemessage = datemessage;
	}


	public String getRemetente() {
		return remetente;
	}


	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}


	public String getEmailremetente() {
		return emailremetente;
	}


	public void setEmailremetente(String emailremetente) {
		this.emailremetente = emailremetente;
	}
}
