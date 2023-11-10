package com.dimm.springbootexample.exception;

import java.util.Date;

public record ApiError(String path,
					   String message,
					   int statusCode,
					   Date date) {
}
