package com.dimm.springbootexample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
	private record PingPong(String response){};

	@GetMapping("/ping")
	public PingPong getPongResponse(){
		return new PingPong("pong");
	}

}
