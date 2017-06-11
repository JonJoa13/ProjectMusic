package ch.hevs.bankservice;

import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import ch.hevs.businessobject.Singer_Band;

@Local
@Stateful
@Remote
//les transactions sont gérées manuellement, dans les méthodes métier des EJB
@TransactionManagement(TransactionManagementType.BEAN)
public class Singer_BandsBean
{
	@PersistenceContext(name = "bankPU")
	private EntityManager entityManager ;
	@Resource
	private UserTransaction transactionUser;
	
	//retourne la liste de tous les Singers ou Bands
	public Set<Singer_Band> getListAllSingerBands()
	{
		//retourne la liste de tous les Singer ou Bands basé sur
		//un algo de hashage qui retient l'ordre d'insertion des
		//éléments ce qui permet d'itérer dessus de façon prévisible
		return new LinkedHashSet<Singer_Band>(entityManager.createQuery("FROM Singerbands").getResultList());
	}

	//donne en retour le Singer ou Band en fonction de son nom (passé en paramètre de méthode)
	public Singer_Band getSingerBand(String name)
	{
		//Retourne un seul et unique résultat (Utilisation du .getSingleResult)
		return (Singer_Band) entityManager.createQuery("FROM Singerbands s WHERE s.name=:name").setParameter("name",  name).getSingleResult();
	}
	
	//Méthode qui va être utilisée pour les Ajouts (INSERT) ainsi que les Mises à jour (UPDATE) des Singers ou Bands
	public void insertSingerBands(Singer_Band singerBands)
	{
		try
		{
			transactionUser.begin();
			entityManager.merge(entityManager.contains(singerBands) ? singerBands : entityManager.merge(singerBands));
			transactionUser.commit();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	//retourne le Singer ou Band en fonction de son Identifiant
	public Singer_Band getSingerBandsById(int id)
	{
		Query query = entityManager.createQuery("FROM Singerbands s WHERE s.id=:id");
		//caster les ID en long pour éviter les erreurs par la suite
		query.setParameter("id", (long)id);
		//Retourne un seul et unique résultat (Utilisation du .getSingleResult)
		return (Singer_Band) query.getSingleResult();
	}
	
	//méthode qui retourne un Singer ou Band en fonction de son nom ainsi que de sa
	//date de création
	public Singer_Band getSingerBandsByNameAndCreationDate(String date, String name)
	{
		Query query = entityManager.createQuery("FROM Singerbands s WHERE s.name=:name AND s.creation_date=:creation_date");
		query.setParameter("name", name);
		query.setParameter("creation_date", date);
		
		try
		{
			return (Singer_Band) query.getSingleResult();
		}
		catch (Exception exception)
		{
			return null;
		}
	}
	
	//on test si le Singer ou Band existe. On interdit la duplication de Singer
	//ou Band (nom et date de creation) car ce on ne veut pas de doublons 
	public boolean isSingerBandsExist(String name, String date, long id)
	{
		Query query = entityManager.createQuery("FROM Singerbands s WHERE s.name=:name AND s.creation_date=:creation_date AND e.id!==:id");
		query.setParameter("name", name);
		query.setParameter("creation_date", date);
		query.setParameter("id", id);
		
		try
		{
			//retourne true si il trouve un résultat
			return (Singer_Band) query.getSingleResult()!=null;
		}
		catch (Exception exception)
		{
			return false;
		}
	}
	
	/*
	
	//DELETE en commentaire pour le moment
	
	//Delete un Singer ou Band de la Base de données
	public void deleteSingerBands(Singer_Band singerBands)
	{
		try
		{
			transactionUser.begin();
			//on test sur l'EntityManager contient la transaction, si c'est pas le cas
			//on fusionne
			if(!entityManager.contains(singerBands))
			{
				singerBands = entityManager.find(Singer_Band.class, singerBands.getId());
			}
			
			//supprime les relations N - N
			//si je supprime un groupe, je supprime ses albums qui contiennent des chansons
			singerBands.emptyAlbum();
			transactionUser.commit();
			transactionUser.begin();
			entityManager.remove(entityManager.contains(singerBands) ? singerBands : entityManager.merge(singerBands));
			transactionUser.commit();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	*/
	
	
}
