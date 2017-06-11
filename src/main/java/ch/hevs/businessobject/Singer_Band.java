package ch.hevs.businessobject;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="Singer_Band")
public class Singer_Band {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name="name")
	private String name;
	@Column(name="creation_date")
	private String creation_date;

	/**
	 * Constructors
	 */

	public Singer_Band(){
		this.albums = new ArrayList<Album>();
	}

	public Singer_Band(String name, String creation_date){
		this.name = name;
		this.creation_date = creation_date;
		this.albums = new ArrayList<Album>();

	}

	/**
	 * Methods
	 */
	//Add an Album
	public void addAlbum(Album a){
		a.setSinger_band(this);
		albums.add(a);
	}

	/**
	 * Relations
	 */

	//Can have many albums
	@OneToMany(mappedBy="singer_band", cascade = CascadeType.ALL)
	private List<Album> albums;

	//Getter and Setter List of Albums
	public List<Album> getAlbums() {
		return albums;
	}
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	//Creation_date
	public String getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}
}
