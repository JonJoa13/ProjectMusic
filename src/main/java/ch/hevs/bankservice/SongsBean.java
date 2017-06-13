package ch.hevs.bankservice;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import ch.hevs.businessobject.Album;
import ch.hevs.businessobject.Song;
import ch.hevs.businessobject.Singer_Band;

@Local
@Stateful
@Remote
//les transactions sont gérées manuellement, dans les méthodes métier des EJB
@TransactionManagement(TransactionManagementType.BEAN)
/*
 * 
 * Cette classe sera utilisée pour faire les insertions de chansons basés sur les Albums et les Singer ou Band
 * 
 * 
 * */

public class SongsBean {

	@PersistenceContext(name = "bankPU")
	private EntityManager entityManager;
	@Resource
	private UserTransaction userTransaction;
	
	//Ajoute la musique dans la DB
	public void insertSong (Song songs, Album albums, Singer_Band singerBands)
	{
		try
		{
			userTransaction.begin();
			//on crée une clé composite (string) basé sur l'ID du Singer ou Band et l'Album
			songs.setCompositeKey(singerBands.getId()+"-" + albums.getId()+" ");
			//on recherche l'entité dans la DB
			Song songFromDB = entityManager.find(Song.class, songs.getCompositeKey());
			
			//on contrôle que ça soit bien un nouveau son/chanson (nouveau ou un simple UPDATE)
			if(songFromDB!=null)
			{
				//on change la valeur de la variable avec la nouvelle valeur
				songFromDB.setTitle(songs.getTitle());
				songFromDB.setStyle(songs.getStyle());
				
				if(!entityManager.contains(songFromDB))
				{
					entityManager.persist(songFromDB);
				}
				
				//on UPDATE le morceau et le sauve dans la DB
				songFromDB.setSingerBand(singerBands);
				songFromDB.setAlbum(albums);
				
				userTransaction.commit();
				return;
			}
			
			if(!entityManager.contains(songs))
			{
				entityManager.persist(songs);
			}
			
			//si c'est une nouvelle entrée, on crée un nouveau morceau dans la DB
			songs.setSingerBand(singerBands);
			songs.setAlbum(albums);
			userTransaction.commit();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
}