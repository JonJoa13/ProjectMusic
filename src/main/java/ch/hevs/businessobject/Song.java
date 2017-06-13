package ch.hevs.businessobject;

import javax.persistence.*;

@Entity
@Table(name="Song")
public class Song {

	@Id
	private String compositeKey;
	@Column(name="title")
	private String title;
	@Column(name="style")
	private String style;

	/**
	 * Relations
	 */

	//Many songs can be in a an Album or not if it's a single.
	//on effectue la jointure sql, dès que l'on récupère l'objet et donc initialise la collection
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "album_id", referencedColumnName = "id")
	private Album album;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "singerBand_id", referencedColumnName = "id")
	private Singer_Band singerBand;
	

	//Getter and Setter for Album
	public Album getAlbum() {
		return album;
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}

	public Singer_Band getSingerBand() {
		return singerBand;
	}
	
	public void setSingerBand(Singer_Band singerBand) {
		this.singerBand = singerBand;
	}
	
	/**
	 * Getter and Setter
	 */

	//id
	public String getCompositeKey() {
		return compositeKey;
	}

	public void setCompositeKey(String compositeKey) {
		this.compositeKey = compositeKey;
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

	public Song()
	{

	}

	public Song(String title, String style)
	{
		this.title = title;
		this.style = style;
	}







}