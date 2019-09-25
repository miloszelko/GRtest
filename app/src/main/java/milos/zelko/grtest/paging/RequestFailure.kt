package milos.zelko.grtest.paging

data class RequestFailure(val retryable: Retryable, val error: Throwable)