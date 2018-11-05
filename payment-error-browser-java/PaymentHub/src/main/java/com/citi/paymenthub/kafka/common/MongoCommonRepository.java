package com.citi.paymenthub.kafka.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MongoCommonRepository<T extends Serializable> extends MongoRepository<T, String>{

/*	@Query("{'_id' : { '$in' : ?0}}")
	List<T> findByIdListAllRecords(List<String>);*/

	@Query(value="{'_id' : { '$in' : ?0}}",delete = true)
	public void deleteByIdListAllRecords(List<String> idList);
	
	@Query("{'regionId' : ?0}")
	public List<T> findByRegionIdAllCountryRecords(String idList);
	
	@Query("{'countryId' : ?0}")
	public List<T> findByCountryIdAllTopicsRecord(String countryId);
	
	@Query("{'regionId' : {'$in' : ?0}}")
	public List<T> findRegionsById(List<String> regionIdList);
	
	@Query(value="{_id : ?0}", fields="{errorMsgData : 1}")
	public List<T> findByIdSingleErrorMessage(String _id);
}

