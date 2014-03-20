package com.bluezero.phaeton.utils;

public interface IAsyncTaskResultHandler {
	<T> void handleResult(AsyncTaskResult<T> result);
}
