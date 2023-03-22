package com.code.exe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.code.exe.utils.*;
@Component
public class ServiceController implements ApplicationContextAware{
	public ExecutionUtils executionUtils;

	public List<String> handleCodeExecution(String language, String encryptedCodeText) {
		String code = null;
		try {
			code = executionUtils.decodeEncryptedCode(encryptedCodeText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> consoleOutputList = new ArrayList<>();
		executionUtils.executeCode(language, code, consoleOutputList);
		return consoleOutputList;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.executionUtils = applicationContext.getBean(ExecutionUtils.class);
	}
}
