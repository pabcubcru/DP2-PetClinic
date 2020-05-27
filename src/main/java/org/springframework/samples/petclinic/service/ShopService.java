
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopService {

	private ShopRepository shopRepository;

	@Autowired
	public ShopService(final ShopRepository shopRepository) {
		this.shopRepository = shopRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Shop> findShops() throws DataAccessException {
		return this.shopRepository.findAll();
	}

	@Transactional
	@CacheEvict(cacheNames = "shopById", allEntries = true)
	public void saveShop(final Shop shop) throws DataAccessException {
		this.shopRepository.save(shop);
	}

	@Transactional(readOnly = true)
	@Cacheable("shopById")
	public Shop findShopById(Integer id) throws DataAccessException {
		return this.shopRepository.findById(id).orElse(null);
	}
}
