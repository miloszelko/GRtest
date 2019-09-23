package milos.zelko.grtest

data class RequestFailure(val retryable: Retryable, val error: Throwable)