package com.note.portal;

import com.note.portal.po.Catalog;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@SpringBootTest
class PortalApplicationTests {

	@Autowired
	MongoTemplate mongoTemplate;
	@Test
	void contextLoads() {

	}
	@Test
	void find(){

	}

	@Test
	public void test(){
		System.out.println(System.currentTimeMillis());
	}

}
