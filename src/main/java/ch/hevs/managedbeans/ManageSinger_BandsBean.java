package ch.hevs.managedbeans;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.persistence.internal.jpa.rs.metadata.model.Link;

import ch.hevs.bankservice.AlbumsBean;
import ch.hevs.bankservice.Singer_BandsBean;
import ch.hevs.bankservice.SongsBean;
import ch.hevs.businessobject.Album;
import ch.hevs.businessobject.Singer_Band;
import ch.hevs.businessobject.Song;

public class ManageSinger_BandsBean {
	
	// text field
	private Singer_Band currentSinger_Band;
	private Album currentAlbum;
	private String singer_BandName;
	private String singer_BandCreation_Date;
	
	// utilisé pour récupéré l'artist courrant dans la liste. 
	private String stringSelectedSinger_Band;
	private String stringSelectedAlbum;
	private Set<Singer_Band> listSinger_Bands;
	private Set<Album> listAlbums;
	private Set<Song> listSongs;
	
	//Bean
	private Singer_BandsBean singer_BandsBean;
	private AlbumsBean albumsBean;
	private SongsBean songsBean;
	
	//utilisé pour afficher le text du boutton submit.
	private String currentMessage;
	
	
	//initialisation du bean
	public void initialize() throws NamingException {
		
		InitialContext ctx = new InitialContext();
		singer_BandsBean = (Singer_BandsBean) ctx.lookup("java:app/TP12-WEB-EJB-PC_EPC_E_0.0.1-SNAPSHOT/Singer_BandsBean!ch.hevs.bankservice.Singer_BandsBean");
		albumsBean = (AlbumsBean) ctx.lookup("java:app/TP12-WEB-EJB-PC_EPC_E_0.0.1-SNAPSHOT/AlbumsBean!ch.hevs.bankservice.AlbumsBean");
		songsBean = (SongsBean) ctx.lookup("java:app/TP12-WEB-EJB-PC_EPC_E_0.0.1-SNAPSHOT/SongsBean!ch.hevs.bankservice.SongsBean");
	}
	
	
	//
	//			LIST OF METHODS
	//
	
	public String insertSinger_Band(){
		
		//redirectIfGuest();
		
		boolean wasNull = false;
		
		if (currentSinger_Band==null){
			
			currentSinger_Band = new Singer_Band();
			currentMessage="The singer_band has been created";
			wasNull= true;
			
		}else{
			currentMessage="The singer_band has been updated";
		}
		
		//insertion des infos de l'artiste courrant
		currentSinger_Band.setName(singer_BandName);
		currentSinger_Band.setCreation_date(singer_BandCreation_Date);

		//check si les infos sont correct, si non, on sort
		if(!isEntryCorrect(currentSinger_Band)){
			if(wasNull)
				currentSinger_Band=null;
			return "";
		}
		
		
		//on vérifie si l'artist à exactement les mêmes informations qu'un autre (ce n'est pas autorisé)
		if(singer_BandsBean.isSingerBandsExist(singer_BandName, singer_BandCreation_Date, currentSinger_Band.getId())){
			
			currentMessage="Singer_Band already exist!";
			//Vide l'artiste courrant
			if(wasNull)
				currentSinger_Band=null;
			return "listSingerBands";
		}
		
		singer_BandsBean.insertSingerBands(currentSinger_Band);
		currentSinger_Band=null;
		
		return "listSingerBands";
		
	}

	/*
	//supprime l'artist de la db
	public String deleteSinger_Band(){
		
		try{
			generateObjectFromStringSinger_Band();
			currentMessage="The singer_band has been deleted";
			
			//ensuite on supprime l'artist
			
		}
		
	}
	*/
	
	//affiche l'artist
	public String displaySinger_band(){
		
		//gère l'arrayOutOfBound
		try{
			
			generateObjectFromStringSinger_Band();
			singer_BandName = currentSinger_Band.getName();
			singer_BandCreation_Date = currentSinger_Band.getCreation_date();
		}
		catch (Exception e) {
			e.printStackTrace();
			return"";
		}
		return "manageSinger_bands";
		
	}
	
	//affiche l'artist en mode guest
	public String displaySinger_bandGuest(){
		
		//gère l'arrayOutOfBound
		try{
			
			generateObjectFromStringSinger_Band();
			singer_BandName = currentSinger_Band.getName();
			singer_BandCreation_Date = currentSinger_Band.getCreation_date();
		}
		catch (Exception e) {
			return"";
		}
		return "manageSinger_bands";
		
	}
	
	public String addAlbumToSinger_Band(){
		try{
			currentMessage="the selected album has been added to the singer_band";
			
			generateObjectFromStringAlbum();
			
			if(currentAlbum==null){
				currentMessage=" you need to create an album first.";
				return "";
			}
			
			//manque une ligne ici
			
		}
		catch (Exception e) {
			currentMessage = "you have to create an album first.";
		}
		
		return "";
	}
	
	public void cleanInfo(){
		currentSinger_Band = null;
		currentAlbum = null;
		currentMessage = "";
	}
		
		
	//Créé l'objet depuis l'item séléctionné
	public void generateObjectFromStringSinger_Band(){
		

		String []arraySingerBands = stringSelectedSinger_Band.split("/");
		currentSinger_Band = singer_BandsBean.getSingerBandsByNameAndCreationDate(arraySingerBands[0], arraySingerBands[1]);
	}
	
	public void generateObjectFromStringAlbum(){
		

		String []arrayAlbumInfo = stringSelectedAlbum.split("/");
		//currentAlbum = albumsBean.getAlbum(arrayAlbumInfo[0], arrayAlbumInfo[1]);
	}
	
	/* 
	 
	 public void redirectIfGuest(){
		try{
			if(!singer_BandsBean.isAdmin()){
				singer_BandsBean.getCtx().getRollbackOnly();
			}
		}catch (Exception e) {
			redirectPage403();
		}
	}
	
	private void redirectPage403(){
		try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("errorPage403.xhtml");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	*/
	
	/*
	 public boolean showAdminContent(){
	 
		
		if(singer_BandsBean.isAdmin())
			return true;
		return false;
	}
	*/
	
	public boolean hideElementFromSinger_Band(){
		if
			(currentSinger_Band!= null){
			return true;
		}
		return false;
	}
	
	public boolean isEntryCorrect(Singer_Band singer_band){
		if(!ch.hevs.validator.Validator.isValideField(singer_band.getName()) || !ch.hevs.validator.Validator.isNotEmpty(singer_band.getName())){
			currentMessage = "Le nom ne peut contenir de '/' and ne peut être vide";
			return false;
		}
		
		if(!ch.hevs.validator.Validator.isValideField(singer_band.getCreation_date()) || !ch.hevs.validator.Validator.isNotEmpty(singer_band.getCreation_date())){
			currentMessage = "La date de création ne peut contenir de '/' and ne peut être vide";
			return false;
		}
		return true;
	}


	public Singer_Band getCurrentSinger_Band() {
		return currentSinger_Band;
	}


	public void setCurrentSinger_Band(Singer_Band currentSinger_Band) {
		this.currentSinger_Band = currentSinger_Band;
	}


	public Album getCurrentAlbum() {
		return currentAlbum;
	}


	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}


	public String getSinger_BandName() {
		return singer_BandName;
	}


	public void setSinger_BandName(String singer_BandName) {
		this.singer_BandName = singer_BandName;
	}


	public String getSinger_BandCreation_Date() {
		return singer_BandCreation_Date;
	}


	public void setSinger_BandCreation_Date(String singer_BandCreation_Date) {
		this.singer_BandCreation_Date = singer_BandCreation_Date;
	}


	public String getStringSelectedSinger_Band() {
		return stringSelectedSinger_Band;
	}


	public void setStringSelectedSinger_Band(String stringSelectedSinger_Band) {
		this.stringSelectedSinger_Band = stringSelectedSinger_Band;
	}


	public String getStringSelectedAlbum() {
		return stringSelectedAlbum;
	}


	public void setStringSelectedAlbum(String stringSelectedAlbum) {
		this.stringSelectedAlbum = stringSelectedAlbum;
	}


	public Set<Singer_Band> getListSinger_Bands() {
		
		if(albumsBean == null || singer_BandsBean == null)
		{
			try
			{
				this.initialize();
			}catch (Exception e) 
			{
			}
		}
		if(singer_BandsBean == null)
			try
			{
				this.initialize();
			}
			catch (Exception e) 
			{
			}
			this.listSinger_Bands = singer_BandsBean.getListAllSingerBands();
			return listSinger_Bands;
	}


	public void setListSinger_Bands(Set<Singer_Band> listSinger_Bands) {
		this.listSinger_Bands = listSinger_Bands;
	}


	public Set<Album> getListAlbums() {
		
		if (currentSinger_Band == null || stringSelectedSinger_Band == null)
			return new LinkedHashSet<Album>();
		try{
			generateObjectFromStringSinger_Band();
			listAlbums = currentSinger_Band.getAlbums();
			return listAlbums;
		}catch (Exception e) {
			return new LinkedHashSet<Album>();
		}
		
	}


	public void setListAlbums(Set<Album> listAlbums) {
		this.listAlbums = listAlbums;
	}


	
	/*
	public Set<Song> getListSongs() {
		
		
		if (currentSinger_Band == null || stringSelectedSinger_Band == null)
			return new LinkedHashSet<Song>();
		try{
			generateObjectFromStringSinger_Band();
			listSongs = currentSinger_Band.getSongs();
			return listSongs;
		}catch (Exception e) {
			return new LinkedHashSet<Song>();
		}
	}

	*/

	public void setListSongs(Set<Song> listSongs) {
		this.listSongs = listSongs;
	}


	public Singer_BandsBean getSinger_BandsBean() {
		return singer_BandsBean;
	}


	public void setSinger_BandsBean(Singer_BandsBean singer_BandsBean) {
		this.singer_BandsBean = singer_BandsBean;
	}


	public AlbumsBean getAlbumsBean() {
		return albumsBean;
	}


	public void setAlbumsBean(AlbumsBean albumsBean) {
		this.albumsBean = albumsBean;
	}


	public SongsBean getSongsBean() {
		return songsBean;
	}


	public void setSongsBean(SongsBean songsBean) {
		this.songsBean = songsBean;
	}


	public String getCurrentMessage() {
		return currentMessage;
	}


	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}
	
	public Set getListAlbumsFromCurrentSinger_Band(){
		
		if(currentSinger_Band.getAlbums()==null || currentSinger_Band.getAlbums().size()==0){
			LinkedHashSet<String> emptylist = new LinkedHashSet<String>();
			emptylist.add("Pas d'album pour l'artiste séléctionné");
			return emptylist;
		}
			
		
		return listAlbums;
		
	}
	

}
