package ch.hevs.businessobject;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="Album")
public class Album
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name="album_name")
	private String album_name;
	@Column(name="album_year")
	private int album_year;

	/**
	 * Relations
	 */

	//Artist that own the album
	@ManyToOne(cascade = CascadeType.ALL)
	private Singer_Band singer_band;

	/**
	 * Constructors
	 */
	public Album(){
		this.songs = new ArrayList<Song>();
	}

	public Album(String album_name, int album_year){
		this.songs = new ArrayList<Song>();
		this.album_name = album_name;
		this.album_year = album_year;
	}

	/**
	 * Methods
	 */

	public void addSong(Song s){
		s.setAlbum(this);
		songs.add(s);
	}

	//Getter and Setter for Singer_Band
	public Singer_Band getSinger_band() {
		return singer_band;
	}
	public void setSinger_band(Singer_Band singer_band) {
		this.singer_band = singer_band;
	}

	//Many songs in one album
	@OneToMany(mappedBy = "album" , cascade = CascadeType.ALL)
	private List<Song> songs;

	//Getter and Singer List of Songs
	public List<Song> getSongs() {
		return songs;
	}
	public void setSongs(List<Song> songs) {
		this.songs = songs;
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

	//Album_name
	public String getAlbum_name() {
		return album_name;
	}
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	//Album_year
	public int getAlbum_year() {
		return album_year;
	}
	public void setAlbum_year(int album_year) {
		this.album_year = album_year;
	}
}