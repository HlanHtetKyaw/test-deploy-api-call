package com.in28minutes.rest.webservices.restfulwebservices.todo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class TodoService {
	
	private static List<Todo> todos = new ArrayList<>();
	
	private static int todosCount = 0;
	
	RestTemplate restTemplate;
	public TodoService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	static {
		todos.add(new Todo(++todosCount, "in28minutes","Get AWS Certified", 
							LocalDate.now().plusYears(10), false ));
		todos.add(new Todo(++todosCount, "in28minutes","Learn DevOps", 
				LocalDate.now().plusYears(11), false ));
		todos.add(new Todo(++todosCount, "in28minutes","Learn Full Stack Development", 
				LocalDate.now().plusYears(12), false ));
	}
	
	public List<Todo> findByUsername(String username){
		Predicate<? super Todo> predicate = 
				todo -> todo.getUsername().equalsIgnoreCase(username);
		return todos.stream().filter(predicate).toList();
	}
	
	public Todo addTodo(String username, String description, LocalDate targetDate, boolean done) {
		Todo todo = new Todo(++todosCount,username,description,targetDate,done);
		todos.add(todo);
		return todo;
	}
	
	public void deleteById(int id) {
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		todos.removeIf(predicate);
	}

	public Todo findById(int id) {
		Predicate<? super Todo> predicate = todo -> todo.getId() == id;
		Todo todo = todos.stream().filter(predicate).findFirst().get();
		return todo;
	}

	public void updateTodo(Todo todo) {
		deleteById(todo.getId());
		todos.add(todo);
	}
	
	@Scheduled(fixedRate = 60000)
public void CallRestApi() {
    String baseUrl = "https://test-deploy-api-call.onrender.com";
    String url = baseUrl + "/";
    try {
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    } catch (Exception e) {
        // Handle the exception (e.g., log it)
    }
}

}
