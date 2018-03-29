package com.taran.controller;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.taran.datamodel.Todo;

@RestController
public class TodoController {
	
	 @Autowired
     MongoOperations mongo;
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody List<Todo> getAllTodo(){
		
		String user=getCurrentUser();

		List<Todo> todos=  mongo.find(new Query(Criteria.where("user").is(user)),  Todo.class); //ye chal rha hai
		return todos;
	}
	
	@RequestMapping( method=RequestMethod.POST,consumes="application/json",produces="application/json")
	public @ResponseBody Todo postAllTodo(@RequestBody Todo body){
		body.setCreated(new Date());
		body.setUpdated(new Date());
		body.setUser(getCurrentUser());
		mongo.save(body,"todos");
	
	return body;
	}
	
	@RequestMapping( method=RequestMethod.PUT,consumes="application/json",produces="application/json")
	public @ResponseBody Todo updateTodo(@RequestBody Todo body){
				body.setUpdated(new Date()); 
				
			Todo	 todo=  mongo.findOne(new Query(Criteria.where("id").is(body.getId())),  Todo.class); //ye chal rha hai
				
				Update update=new Update();
				update.set("state", body.getState());
				update.set("text", body.getText());
				update.set("updated", new Date());
				update.set("created", todo.getCreated());
				todo.setUpdated(new Date());
				todo.setState(body.getState());
				todo.setText(body.getText());
				//mongo.upsert(new Query(Criteria.where("id").is(body.getId())), update, Todo.class);
				mongo.save(todo,"todos");
		//mongo.save(body,"todos");
	
	return todo;
	}
	
	@RequestMapping(method=RequestMethod.DELETE,consumes="application/json",produces="application/json")
	public @ResponseBody String deleteTodo(@RequestParam String id){
		
	
		
		mongo.remove(new Query(Criteria.where("id").is(id)), Todo.class);
		
		return "removed";
		
	
	}
	
	String getCurrentUser(){
		return "todoUser";
	}
}
