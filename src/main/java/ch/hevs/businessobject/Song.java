package ch.hevs.businessobject;

import javax.persistence.*;

@Entity
@Table(name="Song")
public class Song {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name="title")
	private String title;
	@Column(name="style")
	private String style;

	/**
	 * Relations
	 */

	//Many songs can be in a an Album or not if it's a single.
	@ManyToOne(cascade = CascadeType.DETACH)
	private Album album;

	//Getter and Setter for Album
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}


	/**
	 * Getter and Setter
	 */

	//id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	//Name
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	//Style
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}


	/**
	 * Constructors
	 */

	public Song(){

	}

	public Song(String title, String style){
		this.title = title;
		this.style = style;
	}







}
