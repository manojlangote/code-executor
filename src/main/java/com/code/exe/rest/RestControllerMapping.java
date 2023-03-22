package com.code.exe.rest;

import com.code.exe.service.ServiceController;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RestControllerMapping implements ApplicationContextAware {
	public ServiceController serviceController;
	@PostMapping("/compile")
	public List<String> getMapping(@RequestHeader("language") String language, @RequestHeader("code") String code) {
		return serviceController.handleCodeExecution(language, code);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.serviceController = applicationContext.getBean(ServiceController.class);
	}
	public ServiceController getServiceController() {
		return serviceController;
	}
	public void setServiceController(ServiceController serviceController) {
		this.serviceController = serviceController;
	}
}

