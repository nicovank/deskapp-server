package com.oswego.reslife.deskapp.api.impl;

import com.oswego.reslife.deskapp.api.CommunicationService;
import org.springframework.stereotype.Service;

@Service("communication")
public class DefaultCommunicationService implements CommunicationService {

	public String listMessages(String body) {
		return body;
	}
}
