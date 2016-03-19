package br.com.marquesapps.model;

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

import br.com.marquesapps.security.model.User

@Entity
@Table(name="tb_configuration")
public class Configuration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "itensperpage", nullable = true)
	private int itensperpage;
	
	@Column(name = "heightthumbs", nullable = true)
	private int heightthumbs;
	
	@Column(name = "widththumbs", nullable = true)
	private int widththumbs;
	
	@Column(name = "heightimg", nullable = true)
	private int heightimg;
	
	@Column(name = "widthimg", nullable = true)
	private int widthimg;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;

	protected Configuration() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getItensperpage() {
		return itensperpage;
	}

	public void setItensperpage(int itensperpage) {
		this.itensperpage = itensperpage;
	}

	public int getHeightthumbs() {
		return heightthumbs;
	}

	public void setHeightthumbs(int heightthumbs) {
		this.heightthumbs = heightthumbs;
	}

	public int getWidththumbs() {
		return widththumbs;
	}

	public void setWidththumbs(int widththumbs) {
		this.widththumbs = widththumbs;
	}

	public int getHeightimg() {
		return heightimg;
	}

	public void setHeightimg(int heightimg) {
		this.heightimg = heightimg;
	}

	public int getWidthimg() {
		return widthimg;
	}

	public void setWidthimg(int widthimg) {
		this.widthimg = widthimg;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
