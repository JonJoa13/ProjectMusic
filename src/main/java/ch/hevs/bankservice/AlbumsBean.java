package ch.hevs.bankservice;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import ch.hevs.businessobject.Album;

@Local
@Stateful
@Remote
// utilisé pour travailler avec une transaction
@TransactionManagement(TransactionManagementType.BEAN)
//utilisé pour la sécurité. Le projet travail avec 2 rôles (guest & amdin)
//@RolesAllowed(value= {"guest", "administrator"})

public class AlbumsBean {

	@PersistenceContext(name = "bankPU")
	private EntityManager entityManager;
	@Resource
	private UserTransaction transactionUser;
	//utilisé pour vérifier les droits de chaque User (admin et guest)
	//car ils ne sont pas égaux
	@Resource
	private SessionContext sessionContext;

	//retourne la liste de tous les albums
	public Set<Album>getListAlbums()
	{
		return new LinkedHashSet<Album>(entityManager.createQuery("FROM Album").getResultList());
	}

	//retourne 1 seul Album basé sur son Identifiant (Id)
	public Album getAlbum(int id)
	{
		Query query = entityManager.createQuery("FROM Album a WHERE a.id=:id");
		//caster le id en long
		query.setParameter("id", (long)id);
		return (Album) query.getSingleResult();
	}

	//retourne 1 seul Album basé sur son nom et l'année de sortie de l'album
	public Album getAlbum(String album_name, String album_year)
	{
		Query query = entityManager.createQuery("FROM Album a WHERE a.album_name=:album_name AND a.album_year=:album_year");
		query.setParameter("album_name", album_name);
		query.setParameter("album_year", album_year);
		return (Album) query.getSingleResult();
	}

	//INSERT un album dans la DB
	public void insertAlbum(Album album)
	{
		try
		{
			transactionUser.begin();
			entityManager.persist(entityManager.contains(album) ? album : entityManager.merge(album));
			transactionUser.commit();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}

	//Méthode qui check si un album axiste deja. Pour ce faire on va check son nom et son année de sortie
	public boolean isAlbumExist(Album album)
	{
		Query query = entityManager.createQuery("FROM Album a WHERE a.album_name=:album_name AND a.album_year=:album_year AND a.id!=:id");
		query.setParameter("album_name", album.getAlbum_name());
		query.setParameter("album_year", album.getAlbum_year());
		query.setParameter("id", album.getId());

		try
		{
			Album albums = (Album)query.getSingleResult();
			return albums != null;
		}
		catch (Exception exception)
		{
			return false;
		}
	}

	//Suppression des albums de la DB
	
	

	//Check si le user loggé est admin ou non (guest)
	public boolean isAdmin()
	{
		if(sessionContext.isCallerInRole("administrator"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * GETTER & SETTER
	 */
	// get the current session context
	public SessionContext getSessionContext()
	{
		return sessionContext;
	}

	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}
}