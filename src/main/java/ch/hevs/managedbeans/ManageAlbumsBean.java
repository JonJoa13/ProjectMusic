package ch.hevs.managedbeans;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ch.hevs.bankservice.AlbumsBean;
import ch.hevs.bankservice.Singer_BandsBean;
import ch.hevs.bankservice.SongsBean;
import ch.hevs.businessobject.Album;
import ch.hevs.businessobject.Singer_Band;
import ch.hevs.validator.Validator;

public class ManageAlbumsBean {
	
	//text fields
	private Album currentAlbum;
	Singer_Band singer_band;
	private String albumName;
	private String albumYear;
	
	// utilisé pour récupéré l'album courrant dans la liste. 
	private String stringSelectedAlbum;
	private String stringSelectedSinger_band;
	private Set<Album> listAlbums;
	private Singer_BandsBean singer_BandBean;
	private AlbumsBean albumsBean;
	private SongsBean songsBean;
	
	private String currentMessage;
	
	public void initialize() throws NamingException{
		
		InitialContext ctx = new InitialContext();
		albumsBean = (AlbumsBean) ctx.lookup("java:app/TP12-WEB-EJB-PC_EPC_E_0.0.1-SNAPSHOT/AlbumsBean!ch.hevs.bankservice.AlbumsBean");
		singer_BandBean = (Singer_BandsBean) ctx.lookup("java:app/TP12-WEB-EJB-PC_EPC_E_0.0.1-SNAPSHOT/Singer_BandsBean!ch.hevs.bankservice.Singer_BandsBean");
	}
	
	
	//
	//			LIST OF METHODS
	//
	
	public String insertAlbum(){
		redirectIfGuest();
		
		boolean wasNull = false;
		
		if(currentAlbum == null){
			
			currentAlbum = new Album();
			currentMessage = "The album has been created";
			wasNull = true;
		}
		else
		{
			currentMessage = "The athlete has been updated";
		}
		
		currentAlbum.setAlbum_name(albumName);
		currentAlbum.setAlbum_year(albumYear);
		
		if(!isEntryCorrect(currentAlbum)){
			if (wasNull)
				currentAlbum = null;
			return"";
		}
		
		if(albumsBean.isAlbumExist(currentAlbum)){
			currentMessage="This album already exist";
					if(wasNull)
						currentAlbum = null;
					return "";
		}
		
		this.albumsBean.insertAlbum(currentAlbum);
			currentAlbum = null;
				return "listAlbums";
	}
	
	public String displayAlbums(){
		try{
			generateObjectFromField();
			
			albumName = currentAlbum.getAlbum_name();
			albumYear = currentAlbum.getAlbum_year();
		}
		catch (Exception e) {
			return "";
		}
		
		return "manageAlbums";
	}
	
	public String displayAlbumsGuest(){
		try{
			generateObjectFromField();
			
			albumName = currentAlbum.getAlbum_name();
			albumYear = currentAlbum.getAlbum_year();
		}
		catch (Exception e) {
			return "";
		}
		
		return "displayAlbums";
	}
	
	public Set getSinger_BandsList(){
		
		if(currentAlbum == null || stringSelectedAlbum == null)
			return new LinkedHashSet<Singer_Band>();
			
		try{
			generateObjectFromField();
			singer_band = currentAlbum.getSinger_band();
			
			if(singer_band == null){
				LinkedHashSet<String> emptyList= new LinkedHashSet<String>();
				emptyList.add("Pas d'artiste trouvé");
				return emptyList;
			}
			return (Set) singer_band;
		}catch (Exception e) {
			return new LinkedHashSet<Singer_Band>();
		}
	}
	
	public void generateObjectFromField(){
		
		String[] arrayAlbumInfo = stringSelectedAlbum.split("/");
		
		currentAlbum = albumsBean.getAlbum(arrayAlbumInfo[0], arrayAlbumInfo[1]);
	}
	
	public void generaObjectFromSinger_Bands(){
		
		String[] arraySinger_BandInfo = stringSelectedSinger_band.split("/");
		
		singer_band = singer_BandBean.getSingerBandsByNameAndCreationDate(arraySinger_BandInfo[0], arraySinger_BandInfo[1]);
	}
	
	
	/*
	public String deleteAlbums(){
		try{
			currentMessage = "The album has been deleted";
			
			generateObjectFromField();
			
			if(currentAlbum == null){
				currentMessage="No album selected";
			}
			
			albumsBean.deleteAlbum(currentAlbum);
		}catch (Exception e) {
			currentMessage = "Erreur lors de la suppression de l'album, veuillez en choisir un valide!";
			
		}
		
		currentAlbum = null;
		return "";
	}
	
	*/
	
	public void cleanInfo(){
		albumName = "";
		albumYear = "";
	}
	
	
	public void redirectIfGuest(){
		try{
			if(!albumsBean.isAdmin()){
				//albumsBean.getCtx().getRollbackOnly();
			}
		}catch (Exception e) {
			redirectPage403();
		}
	}
	
	public void redirectPage403(){
		try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("errorPage403.xhtml");
		}catch (Exception e) {
			e.printStackTrace();		}
	}

	
	//
	//  Getter and Setter
 	//

	public Album getCurrentAlbum() {
		return currentAlbum;
	}


	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}


	public Singer_Band getSinger_band() {
		return singer_band;
	}


	public void setSinger_band(Singer_Band singer_band) {
		this.singer_band = singer_band;
	}


	public String getAlbumName() {
		return albumName;
	}


	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}


	public String getAlbumYear() {
		return albumYear;
	}


	public void setAlbumYear(String albumYear) {
		this.albumYear = albumYear;
	}


	public String getStringSelectedAlbum() {
		return stringSelectedAlbum;
	}


	public void setStringSelectedAlbum(String stringSelectedAlbum) {
		this.stringSelectedAlbum = stringSelectedAlbum;
	}


	public String getStringSelectedSinger_band() {
		return stringSelectedSinger_band;
	}


	public void setStringSelectedSinger_band(String stringSelectedSinger_band) {
		this.stringSelectedSinger_band = stringSelectedSinger_band;
	}


	public Set<Album> getListAlbums() {
		listAlbums = albumsBean.getListAlbums();
		return listAlbums;
	}


	public void setListAlbums(Set<Album> listAlbums) {
		this.listAlbums = listAlbums;
	}


	public Singer_BandsBean getSinger_BandBean() {
		return singer_BandBean;
	}


	public void setSinger_BandBean(Singer_BandsBean singer_BandBean) {
		this.singer_BandBean = singer_BandBean;
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
		String message = currentMessage;
		currentMessage = "";
		return message;
	}


	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}
	
	public String generateSubmitTextButton(){
		
		if(currentAlbum == null)
			return "Create";
		return "Update";
	}
	
	
	public boolean isEntryCorrect(Album album){
		
		if(!Validator.isValideField(album.getAlbum_name()) || !Validator.isNotEmpty(album.getAlbum_name())){
			currentMessage = "Le nom ne peut pas contenir de caractere tel que '/'";
			return false;
		}
	
		return true;
	}
	
	
	
	

	
	
	
	
	
	
	
	

	
	

}
