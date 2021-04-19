package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class Microservice3ContainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Microservice3ContainerApplication.class, args);
	}

}


@FeignClient(name="microservice-one",url="${microservice-one.ribbon.listOfServers}")
interface ServiceOne{
	 @GetMapping("service1/status")
	String status();
}
// http request through feign client to other microservice
@FeignClient(name="node-service",url="${node-service.ribbon.listOfServers}")
interface NodeService{
	 @GetMapping("/")
	String nodestatus();
}

@CrossOrigin
@RestController
 class FetchMicroserviceController {
    
	@Autowired
	 private ServiceOne serviceOne;
	
	@Autowired
	 private NodeService nodeService;
	
	
	@GetMapping("/statusService3")
    public String statusService3() {
    	return String.format("%s", "Microservice 3 status up");
    }
	
	@HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/status")
    public String status() {
    	return serviceOne.status();
    }
	
	@HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/statusNode")
    public String statusNode() {
    	return nodeService.nodestatus();
    }
	
	private String fallback() {
		 return String.format("%s", "Microservice 3 ERROR!!");
    }
	
}
    
