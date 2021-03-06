package com.dao;

import java.util.HashMap;
import java.util.List;
import com.entity.Client;
import com.entity.PurchaseOrder;

/**
 * 
 * @author Tomasz Gomoradzki
 * Client Data Access Object interface
 * 
 */

public interface ClientDAO {

	public List<Client> getClients();

	public void saveClient(Client theClient);

	public Client getClient(int clientId);

	public void deleteClient(int clientId);

	public List<Client> searchClients(String srchName);

	public HashMap<String, Double> getStats();

	public List<PurchaseOrder> viewClientPurchaseOrders(Client theClient);
	
}
