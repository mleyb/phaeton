package com.bluezero.phaeton.utils;

public class AsyncTaskResult<T> {
	private T _result;
	private Exception _error;

	public T getResult() {
		return _result;
	}
	
	public Exception getError() {
		return _error;
	}

	public AsyncTaskResult(T result) {
		super();
		_result = result;
	}


	public AsyncTaskResult(Exception error) {
		super();
		_error = error;
	}
	
	public AsyncTaskResult() {
		super();
		_result = null;		
		_error = null;
	}
}