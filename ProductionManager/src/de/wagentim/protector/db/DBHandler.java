package de.wagentim.protector.db;


public class DBHandler
{

//	private static final String dbLocation = "D:\\workspaces\\Protector\\db\\info.odb";
//	private EntityManagerFactory emf;
//	private EntityManager em;
//	
//	public DBHandler()
//	{
//		emf = Persistence.createEntityManagerFactory(dbLocation);
//		em = emf.createEntityManager();
//	}
//	
//	public void save(List<InfoEntity> content)
//	{
//		
//		em.getTransaction().begin();
//		
//		for(int i = 0; i < content.size(); i++)
//		{
//			em.persist(content.get(i));
//		}
//		
//		em.getTransaction().commit();
//	}
//	
//	public List<InfoEntity> loadAllData()
//	{
//		em.getTransaction().begin();
//		
//		TypedQuery<InfoEntity> query = em.createQuery(SELECT_ALL, InfoEntity.class);
//		List<InfoEntity> result = query.getResultList();
//		
//		em.getTransaction().commit();
//		
//		return result;
//	}
//	
//	public void close()
//	{
//		em.close();
//		emf.close();
//	}
}
