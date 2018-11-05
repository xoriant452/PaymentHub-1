package com.citi.paymenthub.kafka.repository;

import org.springframework.stereotype.Repository;

import com.citi.paymenthub.kafka.common.MongoCommonRepository;
import com.citi.paymenthub.kafka.model.ErrorMessageTrans;


/**
 * Error Message Master Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Repository
public interface ErrorMessageTransRepository extends MongoCommonRepository<ErrorMessageTrans>{

}
