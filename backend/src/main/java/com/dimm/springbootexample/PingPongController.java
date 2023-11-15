package com.dimm.springbootexample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
	private record PingPong(String response){}
	private static int COUNT = 2;

	@GetMapping("/ping")
	public PingPong getPongResponse(){
		++COUNT;
		return new PingPong("pong: " + COUNT);
	}

}
